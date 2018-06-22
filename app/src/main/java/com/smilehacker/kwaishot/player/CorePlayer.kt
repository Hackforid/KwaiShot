package com.smilehacker.kwaishot.player

import android.content.Context
import android.net.Uri
import android.view.SurfaceView
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util

/**
 * Created by quan.zhou on 2018/6/22.
 */
class CorePlayer(private val context: Context) {

    private var mPlayer: SimpleExoPlayer? = null
    private val mCache by lazy { SimpleCache(context.cacheDir, LeastRecentlyUsedCacheEvictor(1024 * 1024 * 100)) }


    fun init() {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
        player.playWhenReady = true

        mPlayer = player
    }

    fun attachSurfaceView(surfaceView: SurfaceView) {
        mPlayer?.setVideoSurfaceView(surfaceView)
    }

    fun detachSurfaceView(surfaceView: SurfaceView) {
        mPlayer?.clearVideoSurfaceView(surfaceView)
    }

    fun release() {
        mPlayer?.let {
            it.release()
        }
    }

    fun prepare(url: String, loop: Boolean = false) {
        val dataSourceFactory = DefaultDataSourceFactory(context, Util.getUserAgent(context, "KwaiShot"))
        val extractorsFactory = DefaultExtractorsFactory()
        val cacheDataSource = CacheDataSourceFactory(mCache, dataSourceFactory)


        val mediaSource = ExtractorMediaSource.Factory(cacheDataSource)
                .setExtractorsFactory(extractorsFactory)
                .createMediaSource(Uri.parse(url))

        if (loop) {
            val loopSource = LoopingMediaSource(mediaSource)
            mPlayer?.prepare(loopSource)
        } else {
            mPlayer?.prepare(mediaSource)
        }
    }

    fun seek(posMills: Long) {
        mPlayer?.seekTo(posMills)
    }

    fun pause() {
        mPlayer?.playWhenReady = false
    }

    fun play() {
        mPlayer?.playWhenReady = true
    }
}