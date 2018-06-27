package com.smilehacker.kwaishot.utils.widget.nest

import android.content.Context
import android.support.v4.view.ViewCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog
import kotlin.math.max

class DragAwayContainer: FrameLayout {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private lateinit var mChild: View

    private var mTotalDragY = 0f
    private var mTotalDragX = 0f
    private var mIsDragging = false

    private var mLastMotionEvent: MotionEvent? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        mChild = getChildAt(0)
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
//        DLog.i("onStartNestedScroll $nestedScrollAxes vertical=${nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0}")
//        DLog.i("aaaa = ${ViewCompat.SCROLL_AXIS_HORIZONTAL} ${ViewCompat.SCROLL_AXIS_VERTICAL} ${ViewCompat.SCROLL_AXIS_NONE}")
        if (target is RecyclerView) {
            if (target.layoutManager != null && target.layoutManager is LinearLayoutManager) {
                val layoutManager = target.layoutManager as LinearLayoutManager
                if (layoutManager.orientation == LinearLayoutManager.HORIZONTAL) {
                    return nestedScrollAxes and ViewCompat.SCROLL_AXIS_HORIZONTAL != 0
                }
            }
        }
        return nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)

        consumed[0] = dx
        consumed[1] = dy


        moveAndScale(dx, dy, !mIsDragging)
        mIsDragging = true
    }

    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        mLastMotionEvent = event
        return super.onInterceptHoverEvent(event)
    }


    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        DLog.i("onStopNestedScroll")
        mTotalDragY = 0f
        mTotalDragX = 0f
        mIsDragging = false
        mLastMotionEvent = null

        this.animate()
                .translationX(0f)
                .translationY(0f)
                .scaleX(1f)
                .scaleY(1f)
                .start()
    }

    private fun moveAndScale(dx: Int, dy: Int, init: Boolean) {
        mTotalDragX += dx
        mTotalDragY += dy
        translationX = -mTotalDragX
        translationY = -mTotalDragY


        val scaleRate = max(1 - Math.abs(mTotalDragY) / height * 0.8f, 0.4f)
        scaleX = scaleRate
        scaleY = scaleRate
    }

}