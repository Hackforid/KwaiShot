package com.smilehacker.kwaishot.video

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.player.CorePlayer
import com.smilehacker.kwaishot.repository.model.VideoInfo
import com.smilehacker.kwaishot.utils.bindView

/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoFragment: Fragment() {

    private val mSurfaceView by bindView<SurfaceView>(R.id.surface)

    private lateinit var mVideoViewModel: VideoViewModel

    private var mVideoInfo: VideoInfo? = null
    private var mPlayer: CorePlayer? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        if (mVideoInfo != null) {
            mVideoViewModel.setVideoInfo(mVideoInfo!!)
        }
        initData()
    }

    fun setVideoInfo(videoInfo: VideoInfo) {
        if (this::mVideoViewModel.isInitialized) {
            mVideoViewModel.setVideoInfo(videoInfo)
        } else {
            mVideoInfo = videoInfo
        }
    }

    private fun initData() {
        mVideoViewModel.getVideoInfo().observe(this, Observer {
            println("VideoInfo[$it]")
            if (it != null) {
                playVideo(it)
            }
        })
    }

    private fun playVideo(videoInfo: VideoInfo) {
        mVideoInfo = videoInfo

        if (activity == null || activity!!.isDestroyed) {
            return
        }
        if (mPlayer != null) {
            mPlayer?.detachSurfaceView(mSurfaceView)
            mPlayer?.release()
        }

        mPlayer = CorePlayer(activity!!)
        mPlayer?.init()
        mPlayer?.attachSurfaceView(mSurfaceView)
        mPlayer?.prepare(videoInfo.videos[0].urls[0].url, true)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            mPlayer?.detachSurfaceView(mSurfaceView)
            mPlayer?.pause()
        }
    }
}