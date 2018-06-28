package com.smilehacker.kwaishot.utils

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import java.util.*

/**
 * Created by quan.zhou on 2018/6/28.
 */
class RecyclerPagerHelper {

    private var mRecyclerView: RecyclerView? = null

    private val mListeners: MutableList<RecyclerPagerListener> = LinkedList()

    private val mSnapHelper = PagerSnapHelper()
    private var mLastPos = -1

    fun attachToRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView = recyclerView
        mSnapHelper.attachToRecyclerView(recyclerView)
        mRecyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                when(newState) {
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        val pos = getCurrentPos()
                        if (pos >= 0) {
                            notifySelected(pos)
                        }
                    }
                }
            }
        })
    }

    fun setCurrentPos(pos: Int) {
        mRecyclerView?.scrollToPosition(pos)
//        notifySelected(pos)
    }

    fun getCurrentPos(): Int {
        val lm = mRecyclerView?.layoutManager as? LinearLayoutManager
        return lm?.findFirstCompletelyVisibleItemPosition() ?: -1
    }

    fun addListener(listener: RecyclerPagerListener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener)
        }
    }

    fun removeListener(listener: RecyclerPagerListener) {
        mListeners.remove(listener)
    }

    private fun notifySelected(pos: Int) {
        mListeners.forEach { it.onPageSelected(mLastPos, pos) }
        mLastPos = pos
    }

    interface RecyclerPagerListener {
        fun onPageSelected(lastPos: Int, currentPos: Int)
    }
}