package com.smilehacker.kwaishot.utils.widget.nest

import android.animation.Animator
import android.content.Context
import android.support.v4.view.ViewCompat
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

    private var mScrollStep = 0

    private var mDragAwayListener: DragAwayListener? = null

    override fun onFinishInflate() {
        super.onFinishInflate()
        mChild = getChildAt(0)
        this.pivotX = 0f
        this.pivotY = 0f
    }


    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        DLog.i("onStartNestedScroll $nestedScrollAxes vertical=${nestedScrollAxes and ViewCompat.SCROLL_AXIS_VERTICAL != 0}")
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
//        DLog.i("$target $dx $dy")
        if (mScrollStep == 0) {
            if (Math.abs(dy) > Math.abs(dx)) {
                mIsDragging = true
            }
        }

        if (mIsDragging) {
            consumed[0] = dx
            consumed[1] = dy

//            DLog.i("${this.translationY}")
//            if (Math.abs(this.translationY) < height / 2) {
//                moveAndScale(dx, dy, mScrollStep == 0)
//            }
//            DLog.i("trans $translationX $translationY")
            moveAndScale(dx, dy, mScrollStep == 0)
        }

        mScrollStep++
    }

    override fun onInterceptHoverEvent(event: MotionEvent): Boolean {
        mLastMotionEvent = event
        return super.onInterceptHoverEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return super.onTouchEvent(event)
    }


    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
        DLog.i("onStopNestedScroll")

        if (mIsDragging) {
            val pos = floatArrayOf(0f, 0f)
            val rect = intArrayOf(width, height)
            var shouldDismiss = mDragAwayListener?.shouldDismiss(mTotalDragY, pos, rect) ?: false
            DLog.i("on Away $shouldDismiss ${pos[0]} ${pos[1]} ${rect[0]} ${rect[1]}")
            this.animate()
                    .translationX(pos[0])
                    .translationY(pos[1])
                    .scaleX(rect[0].toFloat() / width)
                    .scaleY(rect[1].toFloat() / height)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationRepeat(animation: Animator?) {
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            this@DragAwayContainer.post {
                                if (shouldDismiss) {
                                    mDragAwayListener?.onDismiss()
                                }
                            }
                        }

                        override fun onAnimationCancel(animation: Animator?) {
                        }

                        override fun onAnimationStart(animation: Animator?) {
                        }

                    })
                    .start()
        }

        resetState()
    }

    private fun resetState() {
        mTotalDragY = 0f
        mTotalDragX = 0f
        mIsDragging = false
        mLastMotionEvent = null
        mScrollStep = 0
    }

    fun reset() {
        scaleX = 1f
        scaleY = 1f
        translationX = 0f
        translationY = 0f
        resetState()
    }

    private fun moveAndScale(dx: Int, dy: Int, init: Boolean) {
        mTotalDragX += dx
        mTotalDragY += dy
        translationX = -mTotalDragX
        translationY = -mTotalDragY


        val scaleRate = max(1 - Math.abs(mTotalDragY) / height * 0.8f, if (translationY > 0) 0.6f else 0.8f)
        scaleX = scaleRate
        scaleY = scaleRate
    }


    fun setListener(listener: DragAwayListener?) {
        mDragAwayListener = listener
    }

    interface DragAwayListener {
        fun shouldDismiss(dragY: Float, pos: FloatArray, rect: IntArray) : Boolean
        fun onDismiss()
    }
}