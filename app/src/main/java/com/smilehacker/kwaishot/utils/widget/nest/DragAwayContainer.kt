package com.smilehacker.kwaishot.utils.widget.nest

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.smilehacker.kwaishot.utils.DLog

class DragAwayContainer: FrameLayout {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        DLog.i("onStartNestedScroll $child $nestedScrollAxes")
        return true
    }

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray) {
        super.onNestedPreScroll(target, dx, dy, consumed)
        consumed[1] = dy / 2
    }

    override fun onStopNestedScroll(child: View) {
        super.onStopNestedScroll(child)
    }
}