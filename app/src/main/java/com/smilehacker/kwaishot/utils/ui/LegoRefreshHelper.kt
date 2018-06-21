package com.smilehacker.kwaishot.utils.ui

import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import com.smilehacker.kwaishot.ui.component.LoadMoreComponent
import com.smilehacker.kwaishot.utils.DLog
import com.smilehacker.lego.LegoAdapter

/**
 * Created by quan.zhou on 2017/12/11.
 */

class LegoRefreshHelper : SwipeRefreshLayout.OnRefreshListener {

    private var mRecyclerView: RecyclerView? = null
    private var mSwipeRefreshLayout: SwipeRefreshLayout? = null

    private val mRefreshOnScrollListener: RefreshOnScrollListener
    var isLoadingMore = false
        private set
    private val mLoadMoreModel = LoadMoreComponent.Model()

    private var mOnRefreshListener: OnRefreshListener? = null
    private var mEnableLoadMore = true

    val isRefreshing: Boolean
        get() = if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.isRefreshing
        } else false

    init {
        mRefreshOnScrollListener = RefreshOnScrollListener()
    }

    fun setOnRefreshListener(listener: OnRefreshListener) {
        mOnRefreshListener = listener
    }

    fun setSwipeRefreshLayout(layout: SwipeRefreshLayout) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.setOnRefreshListener(null)
        }
        mSwipeRefreshLayout = layout
        mSwipeRefreshLayout!!.setOnRefreshListener(this)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        if (mRecyclerView != null) {
            mRecyclerView!!.removeOnScrollListener(mRefreshOnScrollListener)
        }
        mRecyclerView = recyclerView
        mRecyclerView!!.addOnScrollListener(mRefreshOnScrollListener)
        if (mRecyclerView!!.adapter == null || mRecyclerView!!.adapter !is LegoAdapter) {
            throw IllegalStateException("recycler must have a lego adapter")
        }
        (mRecyclerView!!.adapter as LegoAdapter).register(LoadMoreComponent())
    }

    override fun onRefresh() {
        if (mOnRefreshListener != null) {
            mOnRefreshListener!!.onRefresh()
        }
    }

    private inner class RefreshOnScrollListener : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (mRecyclerView == null
                    //                    || !(mRecyclerView.getLayoutManager() instanceof LinearLayoutManager)
                    || mRecyclerView!!.adapter !is LegoAdapter) {
                return
            }


            val layoutManager = mRecyclerView!!.layoutManager
            val itemCount = layoutManager.itemCount
            val lastItemIndex : Int

            if (layoutManager is LinearLayoutManager) {
                lastItemIndex = layoutManager.findLastCompletelyVisibleItemPosition()
            } else if (layoutManager is StaggeredGridLayoutManager) {
                val pos = IntArray(2)
                lastItemIndex = layoutManager.findLastCompletelyVisibleItemPositions(null).max()!!
            } else {
                lastItemIndex = -100
            }
            if (itemCount == lastItemIndex + 1 && !isLoadingMore) {
                // loadmore
                DLog.i("load more")
                recyclerView!!.post { loadMore() }
            }
        }
    }

    fun setLoadMoreEnabled(enabled: Boolean) {
        if (mEnableLoadMore == enabled) {
            return
        }
        mEnableLoadMore = enabled
        if (!mEnableLoadMore) {
            loadMoreComplete()
        }
    }

    fun loadMore() {
        if (!mEnableLoadMore || mRecyclerView == null) {
            return
        }
        isLoadingMore = true

        val adapter = mRecyclerView!!.adapter as LegoAdapter
        val list = adapter.data
        if (!list.contains(mLoadMoreModel)) {
            list.add(mLoadMoreModel)
            adapter.notifyItemInserted(list.size)
        }

        if (mOnRefreshListener != null) {
            mOnRefreshListener!!.onLoadMore()
        }
    }

    fun loadMoreComplete() {
        isLoadingMore = false
        if (mRecyclerView == null) {
            return
        }
        val adapter = mRecyclerView!!.adapter as LegoAdapter
        val list = adapter.data
        val index = list.indexOf(mLoadMoreModel)
        if (index >= 0) {
            list.remove(mLoadMoreModel)
            adapter.notifyItemRemoved(index)
        }
    }

    fun refresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.isRefreshing = true
        }
    }

    fun refreshComplete() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout!!.isRefreshing = false
        }
    }

    interface OnRefreshListener {
        fun onLoadMore()
        fun onRefresh()
    }


}
