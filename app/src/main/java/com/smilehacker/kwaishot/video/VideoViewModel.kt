package com.smilehacker.kwaishot.video

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.smilehacker.kwaishot.common.mvvm.BaseViewModel
import com.smilehacker.kwaishot.repository.model.VideoInfo

/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoViewModel: BaseViewModel() {

    private val mVideoInfo by lazy { MutableLiveData<VideoInfo>() }

    fun getVideoInfo() : LiveData<VideoInfo> = mVideoInfo

    fun setVideoInfo(videoInfo: VideoInfo) {
        mVideoInfo.value = videoInfo
    }
}
