package com.hse_project.hse_slaves.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hse_project.hse_slaves.MainViewModel
import com.hse_project.hse_slaves.MainViewModelFactory
import com.hse_project.hse_slaves.R
import com.hse_project.hse_slaves.current.USER_ROLE
import com.hse_project.hse_slaves.model.Event
import com.hse_project.hse_slaves.model.User
import com.hse_project.hse_slaves.posts.CreatorRecyclerAdapter
import com.hse_project.hse_slaves.posts.EventRecyclerAdapter
import com.hse_project.hse_slaves.posts.TopSpacingItemDecoration
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.activity_search.*
import java.util.concurrent.atomic.AtomicBoolean

class SearchActivity : AppCompatActivity() {

    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var userAdapter: CreatorRecyclerAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var myLayoutManager: LinearLayoutManager
    private var name: String = ""
    private var type: Boolean = false
    private var offset: Int = 0
    private var isLoading: AtomicBoolean = AtomicBoolean(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        addScrollListener()
        initApi()
        addListeners()
    }

    private fun addListeners() {
        search_users.setOnClickListener {
            name =
                edit_text_name.text.toString().trim { it <= ' ' }
            type = true
            initRecyclerView(type)
            addDataSet(type)
            refresh(type)
        }

        search_events.setOnClickListener {
            name =
                edit_text_name.text.toString().trim { it <= ' ' }
            type = false
            initRecyclerView(type)
            addDataSet(type)
            refresh(type)
        }
    }

    private fun refresh(type: Boolean) {
        if (type) {
            userAdapter.clear()
        } else {
            eventAdapter.clear()
        }

        offset = 0
        addDataSet(type)
    }

    private fun initRecyclerView(type: Boolean) {
        recyclerView.apply {
            recyclerView.layoutManager = myLayoutManager
            val topSpacingDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecoration)
            if (type) {
                userAdapter = CreatorRecyclerAdapter()
                recyclerView.adapter = userAdapter
            } else {
                eventAdapter = EventRecyclerAdapter()
                recyclerView.adapter = eventAdapter
            }
        }
    }

    fun addDataSet(type: Boolean) {
        if (!isLoading.compareAndSet(false, true)) {
            return
        }

        if (type) {
            var user: List<User>

            viewModel.searchUsersResponse = MutableLiveData()
            viewModel.searchUsers(name, offset, 10)
            viewModel.searchUsersResponse.observe(this, { response ->
                if (response.isSuccessful) {

                    offset += response.body()!!.size
                    user = response.body()!!
                    recyclerView.post {
                        userAdapter.submitList(user)
                    }
                }
                isLoading.set(false)
            })
        } else {
            var event: List<Event>

            viewModel.searchEventsResponse = MutableLiveData()
            viewModel.searchEvents(name, offset, 10)
            viewModel.searchEventsResponse.observe(this, { response ->
                if (response.isSuccessful) {

                    offset += response.body()!!.size
                    event = response.body()!!
                    recyclerView.post {
                        eventAdapter.submitList(event)
                    }
                }
                isLoading.set(false)
            })
        }
    }


    private fun addScrollListener() {
        myLayoutManager = LinearLayoutManager(this)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val visibleItemCount = myLayoutManager.childCount
                val pastVisibleItem = myLayoutManager.findFirstVisibleItemPosition()
                val total = if (USER_ROLE == "ORGANIZER") {
                    userAdapter.itemCount
                } else {
                    eventAdapter.itemCount
                }
                if (!isLoading.get()) {
                    if (visibleItemCount + pastVisibleItem >= total) {
                        addDataSet(type)
                    }
                }
                super.onScrolled(recyclerView, dx, dy)
            }

        })
    }

    private fun initApi() {
        val repository = Repository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
    }

}