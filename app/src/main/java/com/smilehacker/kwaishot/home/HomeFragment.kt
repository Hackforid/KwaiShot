package com.smilehacker.kwaishot.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.home.feed.FeedAdapter
import com.smilehacker.kwaishot.ui.component.VideoCellComponent
import com.smilehacker.kwaishot.utils.bindView
import com.smilehacker.kwaishot.utils.widget.LegoRefreshHelper
import com.smilehacker.lego.util.NoAlphaDefaultItemAnimator

class HomeFragment : Fragment(), LegoRefreshHelper.OnRefreshListener {

    private val mRvFeed by bindView<RecyclerView>(R.id.rv_feed)
    private val mSwipeRefresher by bindView<SwipeRefreshLayout>(R.id.srl)


    private lateinit var mViewModel: HomeViewModel
    private val mFeedAdapter by lazy { FeedAdapter() }
    private val mRefreshHelper by lazy { LegoRefreshHelper() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_home, container, false)
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
        mRvFeed.itemAnimator = NoAlphaDefaultItemAnimator()

        mRefreshHelper.setSwipeRefreshLayout(mSwipeRefresher)
        mRefreshHelper.setRecyclerView(mRvFeed)
        mRefreshHelper.setLoadMoreEnabled(true)
        mRefreshHelper.setOnRefreshListener(this)
    }

    private fun initViewModel() {
        mViewModel.getVideos().observe(this, Observer {
            list ->

            mRefreshHelper.loadMoreComplete()
            mRefreshHelper.refreshComplete()

            val models = list?.mapIndexed { index, videoInfo ->
                VideoCellComponent.Model(videoInfo.id.toInt(), videoInfo.normalCover!!, videoInfo.title, videoInfo.author?.icon, index == 0) }
                    ?: ArrayList()
            mFeedAdapter.commitData(models)
        })

        mViewModel.getErrorStatus().observe(this, Observer {
            println(it)
        })

        mViewModel.getHasNextPage().observe(this, Observer {
            mRefreshHelper.setLoadMoreEnabled(it ?: true)
        })
    }

    override fun onLoadMore() {
        mViewModel.loadNextPage()
    }

    override fun onRefresh() {
        mViewModel.refreshPage()
    }
}