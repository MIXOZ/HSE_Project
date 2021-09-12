package com.hse_project.hse_slaves.activities.pages

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.widget.ImageViewCompat
import androidx.lifecycle.ViewModelProvider
import com.hse_project.hse_slaves.MainViewModel
import com.hse_project.hse_slaves.MainViewModelFactory
import com.hse_project.hse_slaves.R
import com.hse_project.hse_slaves.activities.ApplicationsToEventActivity
import com.hse_project.hse_slaves.activities.MainActivity
import com.hse_project.hse_slaves.current.EVENT_ID
import com.hse_project.hse_slaves.current.IS_TMP_USER
import com.hse_project.hse_slaves.current.TMP_USER_ID
import com.hse_project.hse_slaves.current.USER_ROLE
import com.hse_project.hse_slaves.image.getBitmapByString
import com.hse_project.hse_slaves.model.Event
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.activity_event.*
import kotlinx.android.synthetic.main.activity_event.gallery
import kotlinx.android.synthetic.main.activity_register.*
import java.util.concurrent.atomic.AtomicBoolean


class EventActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel
    private lateinit var data: Event
    private var isLikeSet: Boolean = false
    private var isCheckingLike: AtomicBoolean = AtomicBoolean(false)
    private var likeDelta: Int = 0
    private var isApplied: Boolean = false
    private var isCheckingApply: AtomicBoolean = AtomicBoolean(false)
    private var isAnswering: AtomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        initApi()
        if (USER_ROLE == "CREATOR") {
            checkApply()
            addApplyOnClickListener()
            open_list_of_creators.visibility = GONE

            viewModel.checkIfCreatorHasInvitationToEvent(EVENT_ID)
            viewModel.checkIfCreatorHasInvitationToEventResponse.observe(this, { response ->
                if (response.isSuccessful) {
                    viewModel.getInvitation(EVENT_ID)
                    viewModel.getInvitationResponse.observe(this, { response2 ->
                        if (response2.isSuccessful) {
                            if (!response2.body()!!.accepted) {
                                addAcceptDeclineListener()
                            }
                        }
                    })
                }
            })

        } else {
            if (USER_ROLE == "USER") {
                open_list_of_creators.visibility = GONE
            } else {
                addListOfCreatorsListener()
            }
            accept.visibility = GONE
            decline.visibility = GONE
            edit_text_message.visibility = GONE
            send_application.visibility = GONE

            status_image.visibility = GONE
            status_text.visibility = GONE
        }
        addUserOnClickListeners()
        setData()

        addListenerForLikes()
    }

    private fun addListOfCreatorsListener() {
        open_list_of_creators.setOnClickListener {
            startActivity(Intent(this@EventActivity, ApplicationsToEventActivity::class.java))
        }
    }

    private fun addAcceptDeclineListener() {
        accept.setOnClickListener {
            if (isAnswering.compareAndSet(false, true)) {
                viewModel.answerInvitationToEvent(EVENT_ID, true)
                viewModel.answerInvitationToEventResponse.observe(this, { response ->
                    if (response.isSuccessful) {
                        accept.visibility = GONE
                        decline.visibility = GONE
                    } else {
                        isAnswering.set(false)
                    }
                })
            }
        }
        decline.setOnClickListener {
            if (isAnswering.compareAndSet(false, true)) {
                viewModel.answerInvitationToEvent(EVENT_ID, false)
                viewModel.answerInvitationToEventResponse.observe(this, { response ->
                    if (response.isSuccessful) {
                        accept.visibility = GONE
                        decline.visibility = GONE
                    } else {
                        isAnswering.set(false)
                    }
                })
            }
        }
    }

    private fun checkApply() {
        //TODO этот метод точно делает то что надо ??????
        viewModel.checkIfCreatorHasApplicationFromEvent(EVENT_ID)
        viewModel.checkIfCreatorHasApplicationFromEventResponse.observe(this, { response ->
            if (response.isSuccessful) {
                if (response.body() == true) {
                    //TODO когда появится метод проверить прииняли ли на мероприятие и если да то поменять текст и картинку
                    deleteIfApplied()
                } else {
                    status_image.visibility = GONE
                    status_text.visibility = GONE
                }
            }
        })
    }

    private fun addApplyOnClickListener() {
        send_application.setOnClickListener {
            if (isCheckingApply.compareAndSet(false, true)) {
                if (!isApplied) {
                    val message: String =
                        edit_text_message.text.toString().trim { it <= ' ' }
                    viewModel.sendApplicationToEvent(data.id, message)
                    viewModel.sendApplicationToEventResponse.observe(this, { response ->
                        if (response.isSuccessful) {
                            isApplied = true
                            deleteIfApplied()
                            //onBackPressed()
                        } else {
                            Toast.makeText(
                                this@EventActivity,
                                "Something went wrong, try again later",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        isCheckingLike.set(false)
                    })
                }
            }
        }
    }

    private fun deleteIfApplied() {
        edit_text_message.visibility = GONE
        send_application.visibility = GONE

        status_image.visibility = VISIBLE
        status_text.visibility = VISIBLE
    }

    private fun addUserOnClickListeners() {
        organizer_nik.setOnClickListener {
            IS_TMP_USER = true
            startActivity(Intent(this@EventActivity, MainActivity::class.java))
        }
        organizer.setOnClickListener {
            IS_TMP_USER = true
            startActivity(Intent(this@EventActivity, MainActivity::class.java))
        }
    }

    private fun addListenerForLikes() {
        likes_icon.setOnClickListener {
            if (isCheckingLike.compareAndSet(false, true)) {
                if (isLikeSet) {
                    viewModel.deleteLike(data.id)
                    viewModel.deleteLikeResponse.observe(this, { response1 ->
                        if (response1.isSuccessful) {
                            isLikeSet = false
                            changeLikeColor()
                        }
                        isCheckingLike.set(false)
                    })
                } else {
                    viewModel.postLike(data.id)
                    viewModel.postLikeResponse.observe(this, { response1 ->
                        if (response1.isSuccessful) {
                            isLikeSet = true
                            changeLikeColor()
                        }
                        isCheckingLike.set(false)
                    })
                }
            }
        }
    }

    private fun update() {
        val inflater = LayoutInflater.from(this)
        nik_name.text = data.name
        description.text = data.description
        date.text = data.date.substring(0, 10)
        specialization.text = data.specialization
        ratio.text = data.rating.toString()
        geo.text = data.geoData

        changeLikeColor()

        val likesString = data.likes.size.toString() + " likes"
        likes_number.text = likesString

        for (i in data.images.indices) {
            val view = inflater.inflate(R.layout.item_event_photo, gallery, false)

            val imageView: ImageView = view.findViewById<ImageButton>(R.id.imageView)


            imageView.setImageBitmap(getBitmapByString(data.images[i]))
            gallery.addView(view)
        }

    }

    private fun changeLikeColor() {
        val likesString: String = if (isLikeSet) {
            ImageViewCompat.setImageTintList(
                likes_icon,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red))
            )
            (data.likes.size + likeDelta).toString() + " likes"
        } else {
            ImageViewCompat.setImageTintList(
                likes_icon,
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey))
            )
            (data.likes.size + likeDelta - 1).toString() + " likes"
        }


        likes_number.text = likesString
    }

    fun setData() {
        viewModel.getEvent(EVENT_ID)
        viewModel.eventResponse.observe(this, { response ->
            if (response.isSuccessful) {
                data = response.body()!!
                getOrganizer()
                checkLike()
            }
        })

    }

    private fun getOrganizer() {
        viewModel.getUser(data.organizerId)
        TMP_USER_ID = data.organizerId
        viewModel.userResponse.observe(this, { response ->
            if (response.isSuccessful) {
                organizer_nik.text = response.body()?.username
            }
        })
    }

    private fun checkLike() {
        viewModel.checkLike(data.id)
        viewModel.checkLikeResponse.observe(this, { response ->
            if (response.isSuccessful) {
                isLikeSet = response.body()!!
                likeDelta = if (isLikeSet) {
                    0
                } else {
                    1
                }
                update()
            }
        })
    }

    private fun initApi() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }
}