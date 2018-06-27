package com.smilehacker.kwaishot

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.common.BaseActivity
import com.smilehacker.kwaishot.utils.bindView

class TestActivity: BaseActivity() {

    private val mVp by bindView<ViewPager>(R.id.vp)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_test)
        initVP()
    }

    private fun initVP() {
        mVp.adapter = vpAdapter
    }

    private val vpAdapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return 2
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(container.context).inflate(R.layout.test_page_view, null, false)
//            val view = TestView(this@TestActivity)
            val lp = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            view.layoutParams = lp
            view.background = getDrawable(R.drawable.fade_bg)
            container.addView(view)

            return view
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    class TestView: View {
        constructor(context: Context) : this(context, null, 0)
        constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
        constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


        override fun onTouchEvent(event: MotionEvent?): Boolean {
            return false
        }
    }
}