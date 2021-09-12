package com.hse_project.hse_slaves

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hse_project.hse_slaves.activities.SettingsActivity
import com.hse_project.hse_slaves.current.IS_TMP_USER
import com.hse_project.hse_slaves.current.TMP_USER_ID
import com.hse_project.hse_slaves.current.USER_ID
import com.hse_project.hse_slaves.current.USER_ROLE
import com.hse_project.hse_slaves.image.getBitmapByString
import com.hse_project.hse_slaves.model.User
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.concurrent.atomic.AtomicBoolean

class HomeFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var data: User
    private var isFollowSet: Boolean = false
    private var isCheckingSubscription: AtomicBoolean = AtomicBoolean(false)
    private var eventId: Int = 0
    private var isInvited: Boolean = false
    private var tmpUserRole: String = ""
    private var spinnerPosition: Int = 0
    private val nameToEventId: MutableMap<String, Int> = HashMap()
    private val optionsEvents = ArrayList<String>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewDependsOnUserType()

        initApi()
    }

    private fun addInvite() {
        if (!IS_TMP_USER || !(tmpUserRole == "CREATOR" && USER_ROLE == "ORGANIZER")) {
            sub_main_layout.removeView(edit_text_message)
            sub_main_layout.removeView(invite_button)
            sub_main_layout.removeView(events_spinner)
        } else {
            addSpinner()
            invite_button.setOnClickListener {
                if (!isInvited) {
                    isInvited = true
                    val message: String =
                        edit_text_message.text.toString().trim { it <= ' ' }
                    viewModel.inviteCreatorToEvent(TMP_USER_ID, eventId, message)
                    viewModel.inviteCreatorToEventResponse.observe(viewLifecycleOwner, { response ->
                        if (response.isSuccessful) {
                            optionsEvents.remove(optionsEvents[spinnerPosition])
                        } else {
                            Toast.makeText(
                                context,
                                "Something went wrong, try again later.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        }
    }

    private fun addSpinner() {
        viewModel.getFutureEventsOfOrganizer()
        viewModel.getFutureEventsOfOrganizerResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                val tmpList = response.body()!!

                for (event in tmpList) {
                    optionsEvents.add(event.name)
                    nameToEventId[event.name] = event.id
                }


                events_spinner.adapter =
                    ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        optionsEvents
                    )
                events_spinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            eventId = nameToEventId[optionsEvents[position]]!!
                            spinnerPosition = position
                        }

                        override fun onNothingSelected(parent: AdapterView<*>?) {
                            eventId = nameToEventId[optionsEvents[0]]!!
                            spinnerPosition = 0
                        }
                    }
            }
        })
    }

    private fun checkSubscription() {
        if (!IS_TMP_USER || TMP_USER_ID == USER_ID || data.userRole == "USER") {
            IS_TMP_USER = false
            update()
            return
        }
        IS_TMP_USER = false
        viewModel.getAllSubscriptions(TMP_USER_ID)
        viewModel.getAllSubscriptionsResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                isFollowSet = response.body()?.contains(data)!!
                addFollowListener()
                update()
            }
        })
    }

    private fun addFollowListener() {
        follow.setOnClickListener {
            if (isCheckingSubscription.compareAndSet(false, true)) {
                if (isFollowSet) {
                    viewModel.deleteSubscription(data.id)
                    viewModel.deleteSubscriptionResponse.observe(viewLifecycleOwner, { response1 ->
                        if (response1.isSuccessful) {
                            isFollowSet = false
                            follow.text = ("follow").toString()
                            follow.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.purple_500
                                )
                            )
                        }
                        isCheckingSubscription.set(false)
                    })
                } else {
                    viewModel.postSubscription(data.id)
                    viewModel.postSubscriptionResponse.observe(viewLifecycleOwner, { response1 ->
                        if (response1.isSuccessful) {
                            isFollowSet = true
                            follow.text = ("unfollow").toString()
                            follow.setBackgroundColor(
                                ContextCompat.getColor(
                                    requireContext(),
                                    R.color.grey
                                )
                            )

                        }
                        isCheckingSubscription.set(false)
                    })
                }
            }
        }
    }

    private fun initViewDependsOnUserType() {
        if (!IS_TMP_USER || TMP_USER_ID == USER_ID) {
            follow.visibility = View.INVISIBLE

            settings.setOnClickListener {
                startActivity(Intent(context, SettingsActivity::class.java))
            }
        } else {

            ImageViewCompat.setImageTintList(
                settings,
                ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
            )
            main_layout.removeView(settings)
        }
    }

    private fun initApi() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        if (IS_TMP_USER) {
            viewModel.getUser(TMP_USER_ID)
        } else {
            viewModel.getMyUser()
        }


        viewModel.userResponse.observe(viewLifecycleOwner, { response ->
            if (response.isSuccessful) {
                data = response.body()!!
                if (USER_ROLE == "") {
                    USER_ROLE = data.userRole
                }
                tmpUserRole = data.userRole
                addInvite()
                if (data.userRole == "USER") {
                    follow.visibility = View.INVISIBLE
                }
                checkSubscription()
            } else {
                throw RuntimeException(response.toString())
            }
        })
    }

    private fun update() {
        val inflater = LayoutInflater.from(context)

        nik_name.text = data.username
        first_name.text = data.firstName
        second_name.text = data.lastName
        third_name.text = data.patronymic
        description.text = data.description
        type.text = data.userRole
        specialization.text = data.specialization
        ratio.text = data.rating.toString()

        if (isFollowSet) {
            follow.text = ("unfollow").toString()
            follow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.grey))
        } else {
            follow.text = ("follow").toString()
            follow.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.purple_500))
        }

        for (i in 0 until data.images.size) {
            val view = inflater.inflate(R.layout.item_event_photo, gallery, false)

            val imageView: ImageView = view.findViewById<ImageButton>(R.id.imageView)

            if (i == 0) {
                image.setImageBitmap(getBitmapByString(data.images[i]))
            } else {
                imageView.setImageBitmap(getBitmapByString(data.images[i]))
                gallery.addView(view)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
}