package com.smilehacker.kwaishot.utils.widget

import android.animation.Animator
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

    private lateinit var mPrimaryChild: View

    private var mIsDragging = false

    private var mSnapCallback: SnapCallback? = null

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
//            DLog.d("clampViewPositionVertical $child $top $dy")
            mIsDragging = true
            return top
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return super.clampViewPositionHorizontal(child, left, dx)
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            super.onViewReleased(releasedChild, xvel, yvel)
//            handleDragRelease()
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            DLog.d("view state $state")
            if (state == ViewDragHelper.STATE_IDLE) {
                handleDragRelease()
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return super.getViewHorizontalDragRange(child)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            DLog.d("getViewVerticalDragRange")
            if (child == mPrimaryChild) {
                return 10000
            }
            return super.getViewVerticalDragRange(child)
        }
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return mDragHelper.shouldInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mDragHelper.processTouchEvent(event)
        return true
    }


    interface SnapCallback {
        fun onStartDrag()
        fun onSwipeAway()
    }

    private fun handleDragRelease() {
        val y = mPrimaryChild.y
        if (y < this.height / 3) {
            resetPrimaryChild()
        } else {
            mSnapCallback = null
        }
    }

    private fun resetPrimaryChild() {
        mPrimaryChild
                .animate()
                .x(0f)
                .y(0f)
                .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        mPrimaryChild.postInvalidate()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                .start()
    }

    fun reset() {
        if (this::mPrimaryChild.isInitialized) {
            mPrimaryChild.x = 0f
            mPrimaryChild.y = 0f
            mPrimaryChild.postInvalidate()
        }
    }
}