package com.smilehacker.kwaishot.utils.widget.nest

import android.content.Context
import android.support.v4.view.NestedScrollingChild
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog


class BottomSliderContainer: FrameLayout, NestedScrollingChild {

    private var mDownY = 0f
    private var mDownX = 0f
    private var mLastX = 0f
    private var mLastY = 0f
    private var mFirstMove = true
    private var mMoveStep = 0
    private var mParentStartNest = false

    private val mConsumed = IntArray(2)
    private val mOffsetInWindow = IntArray(2)

    private val mChildHelper = NestedScrollingChildHelper(this)


    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isNestedScrollingEnabled = true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        DLog.i("onInterceptTouchEvent ${ev.action}")
        return super.onInterceptTouchEvent(ev)
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        DLog.i("onTouchEvent ${event.action}")

        val actionMasked = event.actionMasked
        val pointerID = event.getPointerId(0)
        val pointIndex = event.findPointerIndex(pointerID)
        if (pointIndex < 0) {
            return false
        }

        when(actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                resetState()
                val x = event.rawX
                val y = event.rawY
                mDownX = x
                mDownY = y
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.rawX
                val y = event.rawY


                if (mMoveStep == 1) {
                    mParentStartNest = if (Math.abs(y - mDownY) > Math.abs((x - mDownX))) {
                        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
                    } else {
                        startNestedScroll(ViewCompat.SCROLL_AXIS_HORIZONTAL)
                    }
                }


                if (mParentStartNest) {
                    var deltaX = mLastX - x
                    var deltaY =  mLastY - y

                    dispatchNestedPreScroll(deltaX.toInt(), deltaY.toInt(), mConsumed, mOffsetInWindow)
                    dispatchNestedScroll(0, 0, deltaX.toInt(), deltaY.toInt(), mOffsetInWindow)
                }
                mLastX = x
                mLastY = y
                mMoveStep++
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                stopNestedScroll()
                resetState()
            }
        }

        return true
    }

    private fun resetState() {
        mParentStartNest = false
        mFirstMove = true
        mMoveStep = 0
        mDownY = 0f
        mDownX = 0f
        mLastX = 0f
        mLastY = 0f
    }

    override fun setNestedScrollingEnabled(enabled: Boolean) {
        mChildHelper.isNestedScrollingEnabled = enabled
    }

    override fun isNestedScrollingEnabled(): Boolean {
        return mChildHelper.isNestedScrollingEnabled
    }

    override fun startNestedScroll(axes: Int): Boolean {
        return mChildHelper.startNestedScroll(axes)
    }

    override fun stopNestedScroll() {
        mChildHelper.stopNestedScroll()
    }

    override fun hasNestedScrollingParent(): Boolean {
        return mChildHelper.hasNestedScrollingParent()
    }

    override fun dispatchNestedScroll(dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, offsetInWindow: IntArray?): Boolean {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow)
    }

    override fun dispatchNestedPreScroll(dx: Int, dy: Int, consumed: IntArray?, offsetInWindow: IntArray?): Boolean {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow)
    }

    override fun dispatchNestedFling(velocityX: Float, velocityY: Float, consumed: Boolean): Boolean {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed)
    }

    override fun dispatchNestedPreFling(velocityX: Float, velocityY: Float): Boolean {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY)
    }

    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        DLog.i("onStartNestedScroll $child")
        return super.onStartNestedScroll(child, target, nestedScrollAxes)
    }

}