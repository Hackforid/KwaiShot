package com.smilehacker.kwaishot

import android.os.Bundle
import com.smilehacker.kwaishot.common.BaseActivity
import com.smilehacker.kwaishot.event.OpenVideoPageEvent
import com.smilehacker.kwaishot.home.HomeFragment
import com.smilehacker.kwaishot.repository.model.VideoInfo
import com.smilehacker.kwaishot.utils.RxBus
import com.smilehacker.kwaishot.video.VideoFragment

class MainActivity : BaseActivity() {

    private val mVideoFragment by lazy { VideoFragment() }
    private val mHomeFragment by lazy { HomeFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        floatStatusBar()


        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, mHomeFragment)
                    .commitNow()
        }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        RxBus.toObservable(OpenVideoPageEvent::class.java)
                .subscribe {
                    openVideoPage(it.videoInfo)
                }
                .autoDispose()
    }

    override fun onBackPressed() {
        if (mVideoFragment.isVisible) {
            supportFragmentManager.beginTransaction()
                    .hide(mVideoFragment)
                    .show(mHomeFragment)
                    .commitNowAllowingStateLoss()
        } else {
            super.onBackPressed()
        }
    }

    private fun openVideoPage(videoInfo: VideoInfo) {
        mVideoFragment.setVideoInfo(videoInfo)
        val trans = supportFragmentManager.beginTransaction()
        trans.hide(mHomeFragment)
        if (supportFragmentManager.fragments.contains(mVideoFragment)) {
            trans.show(mVideoFragment)
        } else {
            trans.add(R.id.container, mVideoFragment)
        }
        trans.commitNowAllowingStateLoss()
    }

}
