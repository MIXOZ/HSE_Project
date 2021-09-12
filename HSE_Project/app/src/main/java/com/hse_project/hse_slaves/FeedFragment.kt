package com.hse_project.hse_slaves

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hse_project.hse_slaves.activities.SearchActivity
import com.hse_project.hse_slaves.current.FILTER_SET
import com.hse_project.hse_slaves.current.USER_ROLE
import com.hse_project.hse_slaves.model.Event
import com.hse_project.hse_slaves.model.User
import com.hse_project.hse_slaves.posts.CreatorRecyclerAdapter
import com.hse_project.hse_slaves.posts.EventRecyclerAdapter
import com.hse_project.hse_slaves.posts.TopSpacingItemDecoration
import com.hse_project.hse_slaves.repository.Repository
import kotlinx.android.synthetic.main.fragment_feed.*
import java.util.concurrent.atomic.AtomicBoolean

class FeedFragment : Fragment() {


    private lateinit var eventAdapter: EventRecyclerAdapter
    private lateinit var userAdapter: CreatorRecyclerAdapter
    private lateinit var viewModel: MainViewModel
    private lateinit var myLayoutManager: LinearLayoutManager
    private var offset: Int = 0
    private var isLoading: AtomicBoolean = AtomicBoolean(false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        search.setOnClickListener {
            startActivity(Intent(context, SearchActivity::class.java))
        }
        addFilterListener()
        addScrollListener()
        initRecyclerView()
        initApi()
        addDataSet()
    }

    private fun addFilterListener() {
        art_chip.isChecked = FILTER_SET.contains("ART")
        music_chip.isChecked = FILTER_SET.contains("MUSIC")
        literature_chip.isChecked = FILTER_SET.contains("LITERATURE")
        photography_chip.isChecked = FILTER_SET.contains("PHOTOGRAPHY")

        art_chip.setOnClickListener {
            if (art_chip.isChecked) {
                FILTER_SET.add("ART")
            } else {
                FILTER_SET.remove("ART")
            }
            refresh()
        }

        music_chip.setOnClickListener {
            if (music_chip.isChecked) {
                FILTER_SET.add("MUSIC")
            } else {
                FILTER_SET.remove("MUSIC")
            }
            refresh()
        }

        literature_chip.setOnClickListener {
            if (literature_chip.isChecked) {
                FILTER_SET.add("LITERATURE")
            } else {
                FILTER_SET.remove("LITERATURE")
            }
            refresh()
        }

        photography_chip.setOnClickListener {
            if (photography_chip.isChecked) {
                FILTER_SET.add("PHOTOGRAPHY")
            } else {
                FILTER_SET.remove("PHOTOGRAPHY")
            }
            refresh()
        }
    }

    private fun refresh() {
        if (USER_ROLE == "ORGANIZER") {
            userAdapter.clear()
        } else {
            eventAdapter.clear()
        }

        offset = 0
        addDataSet()
    }


    private fun addScrollListener() {
        myLayoutManager = LinearLayoutManager(context)
        recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        addDataSet()
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

    fun addDataSet() {
        if (!isLoading.compareAndSet(false, true)) {
            return
        }

        var curFilterSet = FILTER_SET
        if (curFilterSet.size == 0) {
            curFilterSet = setOf("ART", "MUSIC", "LITERATURE", "PHOTOGRAPHY") as MutableSet<String>
        }
        if (USER_ROLE == "ORGANIZER") {
            var user: List<User>

            viewModel.getCreatorsResponse = MutableLiveData()
            viewModel.getCreators(offset, 10, curFilterSet)
            viewModel.getCreatorsResponse.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful) {

                    offset += response.body()!!.size
                    user = response.body()!!
                    recycler_view.post {
                        userAdapter.submitList(user)
                    }
                }
                isLoading.set(false)
            })
        } else {
            var event: List<Event>

            viewModel.getEventsResponse = MutableLiveData()
            viewModel.getEvents(offset, 10, curFilterSet)
            viewModel.getEventsResponse.observe(viewLifecycleOwner, { response ->
                if (response.isSuccessful) {

                    offset += response.body()!!.size
                    event = response.body()!!
                    recycler_view.post {
                        eventAdapter.submitList(event)
                    }
                }
                isLoading.set(false)
            })
        }
    }

    private fun initRecyclerView() {
        recycler_view.apply {
            recycler_view.layoutManager = myLayoutManager
            val topSpacingDecoration = TopSpacingItemDecoration(30)
            addItemDecoration(topSpacingDecoration)
            if (USER_ROLE == "ORGANIZER") {
                userAdapter = CreatorRecyclerAdapter()
                recycler_view.adapter = userAdapter
            } else {
                eventAdapter = EventRecyclerAdapter()
                recycler_view.adapter = eventAdapter
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }
}