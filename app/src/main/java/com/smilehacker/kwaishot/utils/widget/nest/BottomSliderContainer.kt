package com.smilehacker.kwaishot.utils.widget.nest

import android.content.Context
import android.support.v4.view.NestedScrollingChild
import android.support.v4.view.NestedScrollingChildHelper
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog


class BottomSliderContainer: FrameLayout, NestedScrollingChild {

    private var mDownY = 0f

    private val mConsumed = IntArray(2)
    private val mOffsetInWindow = IntArray(2)

    private val mChildHelper = NestedScrollingChildHelper(this)


    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        isNestedScrollingEnabled = true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
//        DLog.i("onTouchEvent ${event.action}")

        val actionMasked = event.actionMasked
        val pointerID = event.getPointerId(0)

        when(actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                val y = getPointerY(event, pointerID)
                if (y == -1f) {
                    return false
                }
                mDownY = y
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL)
            }
            MotionEvent.ACTION_MOVE -> {
                val y = getPointerY(event, pointerID)
                if (y == -1f) {
                    return false
                }

                var deltaY = y - mDownY
                DLog.i("onMove start $deltaY")

                if (dispatchNestedPreScroll(0, deltaY.toInt(), mConsumed, mOffsetInWindow)) {
                    deltaY -= mConsumed[1]
                }
                DLog.i("onMove end $deltaY")
                dispatchNestedScroll(0, 0, 0, deltaY.toInt(), mOffsetInWindow)
                mDownY = y
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                stopNestedScroll()
            }
        }

//        return super.onTouchEvent(event)
        return true
    }

    private fun getPointerY(event: MotionEvent, pointerId: Int): Float {
        val pointerIndex = event.findPointerIndex(pointerId)
        return if (pointerIndex < 0) {
            -1f
        } else {
            event.getY(pointerIndex)
        }
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
}