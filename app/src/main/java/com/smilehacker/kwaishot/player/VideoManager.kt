package com.smilehacker.kwaishot.player

import android.annotation.SuppressLint
import com.smilehacker.kwaishot.App
import com.smilehacker.kwaishot.repository.model.VideoInfo

/**
 * Created by quan.zhou on 2018/6/28.
 */
object VideoManager: CorePlayer.Listener {

    private val mCurrentVideoList: MutableList<VideoInfo> = ArrayList()
    private var mCurrentVideoPos: Int = -1
    private var mCurrentVideo: VideoInfo? = null

    @SuppressLint("StaticFieldLeak")
    private var mPlayer: CorePlayer? = null


    fun prepareVideos(videos: List<VideoInfo>, currentPos: Int) {
        mCurrentVideoPos = currentPos
        mCurrentVideo = videos[currentPos]
        mCurrentVideoList.clear()
        mCurrentVideoList.addAll(videos)
        if (mPlayer == null) {
            mPlayer = CorePlayer(App.instance.applicationContext)
            mPlayer!!.init()
            mPlayer!!.addListener(this)
        }
        mPlayer!!.prepare(videos[currentPos].videos[0].urls[0].url, true)
    }

    fun prepareVideoInList(pos: Int) {
        mCurrentVideoPos = pos
        mCurrentVideo = mCurrentVideoList[pos]
        mPlayer!!.prepare(mCurrentVideo!!.videos[0].urls[0].url, true)
    }

    override fun onPlayerStateChanged(isPlay: Boolean, playState: Int) {
        if (playState == CorePlayer.STATE_READY) {
//            applyVideoAspectRatio()
        }
    }

    fun getPlayer() : CorePlayer? = mPlayer

    fun getVideoList() = mCurrentVideoList
    fun getCurrentVideoPos() = mCurrentVideoPos
    fun getCUrrentVideoInfo() = mCurrentVideo
}