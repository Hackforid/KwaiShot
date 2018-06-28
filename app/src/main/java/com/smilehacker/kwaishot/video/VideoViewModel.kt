package com.smilehacker.kwaishot.video

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.smilehacker.kwaishot.common.mvvm.BaseViewModel
import com.smilehacker.kwaishot.player.CorePlayer
import com.smilehacker.kwaishot.player.VideoManager
import com.smilehacker.kwaishot.repository.model.VideoInfo

/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoViewModel: BaseViewModel() {

    private val mVideoInfo by lazy { MutableLiveData<VideoInfo>() }

    private val mVideoInfos by lazy { MutableLiveData<List<VideoInfo>>() }

    private val mPlayer by lazy { MutableLiveData<CorePlayer>() }

    fun getVideoInfo() : LiveData<VideoInfo> = mVideoInfo

    fun getVideoList() : LiveData<List<VideoInfo>> = mVideoInfos

    fun getPlayer() : LiveData<CorePlayer> = mPlayer

    fun readCurrentVideo() {
        val videos = VideoManager.getVideoList()
        val video = videos.get(VideoManager.getCurrentVideoPos())

        mVideoInfos.value = videos
        mVideoInfo.value = video

        mPlayer.value = VideoManager.getPlayer()
    }

    fun changeVideo(pos: Int) {
        val video = mVideoInfos.value!![pos]
        mVideoInfo.value = video
        VideoManager.prepareVideoInList(pos)

    }

}
