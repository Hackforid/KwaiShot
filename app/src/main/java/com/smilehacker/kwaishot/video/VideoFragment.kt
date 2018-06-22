package com.smilehacker.kwaishot.video

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.utils.bindView

/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoFragment: Fragment() {

    private val mSurfaceView by bindView<SurfaceView>(R.id.surface)

    private lateinit var mVideoViewModel: VideoViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_video, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
    }

}