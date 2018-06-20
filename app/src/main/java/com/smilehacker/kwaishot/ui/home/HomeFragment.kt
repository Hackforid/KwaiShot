package com.smilehacker.kwaishot.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.ui.component.VideoCellComponent
import com.smilehacker.kwaishot.ui.home.feed.FeedAdapter
import com.smilehacker.kwaishot.utils.bindView

class HomeFragment : Fragment() {

    private val mRvFeed by bindView<RecyclerView>(R.id.rv_feed)


    private lateinit var mViewModel: HomeViewModel
    private val mFeedAdapter by lazy { FeedAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("onCreateView")
        val view = inflater.inflate(R.layout.frg_home, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initUI()

        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initViewModel()
        mViewModel.refreshPage()
    }

    private fun initUI() {
        mRvFeed.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mRvFeed.adapter = mFeedAdapter
    }

    private fun initViewModel() {
        mViewModel.getVideos().observe(this, Observer {
            list -> println(list)
            val models = list?.mapIndexed { index, videoInfo ->
                VideoCellComponent.Model(videoInfo.id.toInt(), videoInfo.normalCover!!, videoInfo.title, videoInfo.author?.icon, index == 0) }
                    ?: ArrayList()
            mFeedAdapter.commitData(models)
        })

        mViewModel.getErrorStatus().observe(this, Observer {
            println(it)
        })
    }
}