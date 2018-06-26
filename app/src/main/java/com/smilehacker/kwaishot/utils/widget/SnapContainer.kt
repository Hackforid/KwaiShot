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

    private var mIsAnimting = false

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
            return !mIsAnimting
        }
        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
//            DLog.d("clampViewPositionVertical $child $top $dy")
            mIsDragging = true
            return top
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
//            handleDragRelease()
//            DLog.d("reset tranY ${mPrimaryChild.translationY}")
            resetPrimaryChild()
        }

    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if(mDragHelper.shouldInterceptTouchEvent(ev)) {
            return true
        }
        return super.onInterceptTouchEvent(ev)
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
        DLog.d("from tranY ${mPrimaryChild.translationY}")
        mIsAnimting = true
        mPrimaryChild
                .animate()
                .x(0f)
                .y(0f)
//                .translationX(0f)
//                .translationY(0f)
                .setListener(object: Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        mPrimaryChild.post {
//                            mPrimaryChild.translationX = 0f
//                            mPrimaryChild.translationY = 0f
                            DLog.d("to tranY ${mPrimaryChild.translationY}")
                            mIsAnimting = false
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })
                .start()
    }

    fun reset() {
//        DLog.d("${mPrimaryChild.x} ${mPrimaryChild.y}")
//        mDragHelper.smoothSlideViewTo(mPrimaryChild, 0, 0)
//        ViewCompat.postInvalidateOnAnimation(this)
//        postInvalidate()
        if (this::mPrimaryChild.isInitialized) {
            mPrimaryChild.x = 0f
            mPrimaryChild.y = 0f
            mPrimaryChild.translationX = 0f
            mPrimaryChild.translationY = 0f
//            mPrimaryChild.postInvalidate()
        }
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        DLog.d("$visibility ${mPrimaryChild.y}")
    }
}