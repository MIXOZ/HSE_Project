package com.hse_project.hse_slaves

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.hse_project.hse_slaves.activities.pages.EventActivity
import com.hse_project.hse_slaves.activities.pages.chats.event.CreateEventActivity
import com.hse_project.hse_slaves.current.EVENT_ID
import com.hse_project.hse_slaves.current.USER_ROLE
import com.hse_project.hse_slaves.image.getBitmapByString
import com.hse_project.hse_slaves.model.Event
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.fragment_chat_for_organizer.*
import kotlinx.android.synthetic.main.item_chats.view.*


class ChatForOrganizerFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private var eventsFuture: ArrayList<Event> = ArrayList()
    private var eventsPast: ArrayList<Event> = ArrayList()
    private var eventsInvite: ArrayList<Event> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (USER_ROLE != "ORGANIZER") {
            addEvent.visibility = GONE
        }
        if (USER_ROLE != "CREATOR") {
            grid_view_invite_events.visibility = GONE
            name_of_invite_events.visibility = GONE
        }
        if (USER_ROLE == "ORGANIZER") {
            addEvent.setOnClickListener {
                startActivity(Intent(context, CreateEventActivity::class.java))
            }
        } else {
            addEvent.visibility = GONE
        }

        initApi()

        addGridFuture()
        addGridPast()
        if (USER_ROLE == "CREATOR") {
            addGridInvite()
        }
    }

    private fun addGridFuture() {
        when {
            (USER_ROLE == "ORGANIZER") -> {
                viewModel.getFutureEventsOfOrganizer()
                viewModel.getFutureEventsOfOrganizerResponse.observe(
                    viewLifecycleOwner,
                    { response ->
                        if (response.isSuccessful) {
                            eventsFuture.clear()
                            eventsFuture.addAll(response.body()!!)

                            grid_view_future_events.adapter = FutureAdapter()

                            grid_view_future_events.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->
                                    EVENT_ID = eventsFuture[position].id
                                    startActivity(Intent(context, EventActivity::class.java))
                                }
                        }
                    })
                return
            }
            (USER_ROLE == "CREATOR") -> {
                viewModel.getFutureEventsOfCreator()
                viewModel.getFutureEventsOfCreatorResponse.observe(viewLifecycleOwner, { response ->
                    if (response.isSuccessful) {
                        eventsFuture.clear()
                        eventsFuture.addAll(response.body()!!)

                        grid_view_future_events.adapter = FutureAdapter()

                        grid_view_future_events.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                EVENT_ID = eventsFuture[position].id
                                startActivity(Intent(context, EventActivity::class.java))
                            }
                    }
                })
                return
            }
            else -> {
                viewModel.getFutureEventsOfUser()
                viewModel.getFutureEventsOfUserResponse.observe(viewLifecycleOwner, { response ->
                    if (response.isSuccessful) {
                        eventsFuture.clear()
                        eventsFuture.addAll(response.body()!!)

                        grid_view_future_events.adapter = FutureAdapter()

                        grid_view_future_events.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                EVENT_ID = eventsFuture[position].id
                                startActivity(Intent(context, EventActivity::class.java))
                            }
                    }
                })
            }
        }
    }

    private fun addGridInvite() {
        if (USER_ROLE == "CREATOR") {
            viewModel.getInviteEventsOfCreator()
            viewModel.getInviteEventsOfCreatorResponse.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful) {
                    eventsInvite.clear()
                    eventsInvite.addAll(response.body()!!)

                    grid_view_invite_events.adapter = InviteAdapter()

                    grid_view_invite_events.onItemClickListener =
                        AdapterView.OnItemClickListener { _, _, position, _ ->
                            EVENT_ID = eventsInvite[position].id
                            startActivity(Intent(context, EventActivity::class.java))
                        }
                }
            })
        }
    }

    private fun addGridPast() {
        when {
            (USER_ROLE == "ORGANIZER") -> {
                viewModel.getPassedEventsOfOrganizer()
                viewModel.getPassedEventsOfOrganizerResponse.observe(
                    viewLifecycleOwner,
                    { response ->
                        if (response.isSuccessful) {
                            eventsPast.clear()
                            eventsPast.addAll(response.body()!!)

                            grid_view_past_events.adapter = PastAdapter()

                            grid_view_past_events.onItemClickListener =
                                AdapterView.OnItemClickListener { _, _, position, _ ->
                                    EVENT_ID = eventsPast[position].id
                                    startActivity(Intent(context, EventActivity::class.java))
                                }
                        }
                    })
                return
            }
            (USER_ROLE == "CREATOR") -> {
                viewModel.getPassedEventsOfCreator()
                viewModel.getPassedEventsOfCreatorResponse.observe(viewLifecycleOwner, { response ->
                    if (response.isSuccessful) {
                        eventsPast.clear()
                        eventsPast.addAll(response.body()!!)

                        grid_view_past_events.adapter = PastAdapter()

                        grid_view_past_events.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                EVENT_ID = eventsPast[position].id
                                startActivity(Intent(context, EventActivity::class.java))
                            }
                    }
                })
                return
            }
            else -> {
                viewModel.getPassedEventsOfUser()
                viewModel.getPassedEventsOfUserResponse.observe(viewLifecycleOwner, { response ->
                    if (response.isSuccessful) {
                        eventsPast.clear()
                        eventsPast.addAll(response.body()!!)

                        grid_view_past_events.adapter = PastAdapter()

                        grid_view_past_events.onItemClickListener =
                            AdapterView.OnItemClickListener { _, _, position, _ ->
                                EVENT_ID = eventsPast[position].id
                                startActivity(Intent(context, EventActivity::class.java))
                            }
                    }
                })
            }
        }
    }


    private fun initApi() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat_for_organizer, container, false)
    }

    private inner class FutureAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return eventsFuture.size
        }

        override fun getItem(position: Int): Any {
            return Any()
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.item_chats, null)
            view.item_chat_text.text = eventsFuture[position].name
            if (eventsFuture[position].images.isNotEmpty()) {
                view.item_chat_button.setImageBitmap(
                    getRoundedCornerBitmap(
                        getBitmapByString(
                            eventsFuture[position].images[0]
                        ), 500
                    )
                )
            }
            return view
        }

    }

    private inner class PastAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return eventsPast.size
        }

        override fun getItem(position: Int): Any {
            return Any()
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.item_chats, null)
            view.item_chat_text.text = eventsPast[position].name
            if (eventsPast[position].images.isNotEmpty()) {
                view.item_chat_button.setImageBitmap(
                    getRoundedCornerBitmap(
                        getBitmapByString(
                            eventsPast[position].images[0]
                        ), 500
                    )
                )
            }
            return view
        }

    }

    private inner class InviteAdapter : BaseAdapter() {
        override fun getCount(): Int {
            return eventsInvite.size
        }

        override fun getItem(position: Int): Any {
            return Any()
        }

        override fun getItemId(position: Int): Long {
            return 0
        }

        @SuppressLint("ViewHolder", "InflateParams")
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = layoutInflater.inflate(R.layout.item_chats, null)
            view.item_chat_text.text = eventsInvite[position].name
            if (eventsInvite[position].images.isNotEmpty()) {
                view.item_chat_button.setImageBitmap(
                    getRoundedCornerBitmap(
                        getBitmapByString(
                            eventsInvite[position].images[0]
                        ), 500
                    )
                )
            }
            return view
        }

    }

    fun getRoundedCornerBitmap(bitmap: Bitmap, roundPixelSize: Int): Bitmap? {
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)
        val rectF = RectF(rect)
        val roundPx = roundPixelSize.toFloat()
        paint.isAntiAlias = true
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }
}