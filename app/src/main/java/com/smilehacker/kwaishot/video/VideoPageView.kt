package com.smilehacker.kwaishot.video

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

/**
 * Created by quan.zhou on 2018/6/28.
 */
class VideoPageView: FrameLayout {

    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
}