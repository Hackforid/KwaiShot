package com.smilehacker.kwaishot.utils.widget

import android.content.Context
import android.support.v4.view.ViewCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog

/**
 * Created by quan.zhou on 2018/6/23.
 */
class SnapContainer : FrameLayout {


    private var mDraggingDown = false
    private var mDraggingUp = false

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onStartNestedScroll(child: View?, target: View?, nestedScrollAxes: Int): Boolean {
        DLog.i("onStartNestedScroll")
        return (nestedScrollAxes or ViewCompat.SCROLL_AXIS_VERTICAL) != 0
    }

    override fun onNestedPreScroll(target: View?, dx: Int, dy: Int, consumed: IntArray?) {
        DLog.i("onNestedPreScroll $target $dx $dy $consumed")
        super.onNestedPreScroll(target, dx, dy, consumed)
    }

    override fun onNestedScroll(target: View?, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int) {
        DLog.i("onNestedScroll $target $dxConsumed $dyConsumed $dxUnconsumed $dyUnconsumed")
        super.onNestedScroll(target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed)
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return super.onInterceptTouchEvent(ev)
    }

    override fun onStopNestedScroll(child: View?) {
        super.onStopNestedScroll(child)
    }

}