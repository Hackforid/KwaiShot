package com.smilehacker.kwaishot.utils.widget

import android.content.Context
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog

/**
 * Created by quan.zhou on 2018/6/23.
 */
class SnapContainer : FrameLayout {

    private val mDragHelper = ViewDragHelper.create(this, 1.0f, DragCallback())

    private var mDraggingDown = false
    private var mDraggingUp = false

    private lateinit var mPrimaryChild: View

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        mPrimaryChild = getChildAt(0)
    }

    inner class DragCallback: ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            DLog.d("tryCaptureView $child $pointerId")
            return child == mPrimaryChild
        }
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            DLog.d("clampViewPositionVertical $child $top $dy")
            return top
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {

        val action = ev.action
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mDragHelper.cancel()
            return false
        }

        return mDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }


}