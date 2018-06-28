package com.smilehacker.kwaishot.player

import android.content.Context
import android.net.Uri
import android.support.annotation.IntDef
import android.view.SurfaceView
import android.view.TextureView
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.LoopingMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.LeastRecentlyUsedCacheEvictor
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import com.google.android.exoplayer2.util.Util
import com.smilehacker.kwaishot.App
import com.smilehacker.kwaishot.utils.DLog
import java.util.*

/**
 * Created by quan.zhou on 2018/6/22.
 */
class CorePlayer(private val context: Context) {


    companion object {
        /**
         * The player does not have any media to play.
         */
        const val STATE_IDLE = 1
        /**
         * The player is not able to immediately play from its current position. This state typically
         * occurs when more data needs to be loaded.
         */
        const val STATE_BUFFERING = 2
        /**
         * The player is able to immediately play from its current position. The player will be playing if
         * [.getPlayWhenReady] is true, and paused otherwise.
         */
        const val STATE_READY = 3
        /**
         * The player has finished playing the media.
         */
        const val STATE_ENDED = 4


        @IntDef(STATE_IDLE, STATE_BUFFERING, STATE_READY, STATE_ENDED)
        @Retention(AnnotationRetention.SOURCE)
        annotation class STATE

        private val mCache by lazy { SimpleCache(App.instance.cacheDir, LeastRecentlyUsedCacheEvictor(1024 * 1024 * 100)) }
    }

    private var mPlayer: SimpleExoPlayer? = null
    private val mListeners: MutableList<Listener> by lazy { LinkedList<Listener>() }


    fun init() {
        val bandwidthMeter = DefaultBandwidthMeter()
        val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(bandwidthMeter)
        val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
        val player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)
//        player.playWhenReady = true

        player.addListener(object : Player.EventListener {
            override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters?) {
            }

            override fun onSeekProcessed() {
            }

            override fun onTracksChanged(trackGroups: TrackGroupArray?, trackSelections: TrackSelectionArray?) {
            }

            override fun onPlayerError(error: ExoPlaybackException?) {
            }

            override fun onLoadingChanged(isLoading: Boolean) {
            }

            override fun onPositionDiscontinuity(reason: Int) {
            }

            override fun onRepeatModeChanged(repeatMode: Int) {
            }

            override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
            }

            override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {
            }

            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                DLog.i("state $playWhenReady $playbackState")
                val state = when(playbackState) {
                    Player.STATE_BUFFERING -> STATE_BUFFERING
                    Player.STATE_IDLE -> STATE_IDLE
                    Player.STATE_READY -> STATE_READY
                    Player.STATE_ENDED -> STATE_ENDED
                    else -> 0
                }
                mListeners.forEach { it.onPlayerStateChanged(playWhenReady, state) }
            }

        })

        mPlayer = player
    }

    fun attachSurfaceView(surfaceView: SurfaceView?) {
        mPlayer?.setVideoSurfaceView(surfaceView)
    }

    fun attachSurfaceView(surfaceView: TextureView?) {
        mPlayer?.setVideoTextureView(surfaceView)
    }

    fun clearSurface() {
        mPlayer?.clearVideoSurface()
    }


    fun release() {
        mPlayer?.stop()
        mPlayer?.release()
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

    fun stop() {
        mPlayer?.stop()
    }

    fun play() {
        mPlayer?.playWhenReady = true
    }

    fun isReady() : Boolean {
        return mPlayer?.playbackState == Player.STATE_READY
    }

    fun addListener(listener: Listener) {
        if (!mListeners.contains(listener)) {
            mListeners.add(listener)
        }
    }

    fun getVideoRatio() : Float {
        val format = mPlayer?.videoFormat ?: return 1f
        return format.width.toFloat() / format.height
    }


    interface Listener {
        /**
         * @param playState One of the {@code STATE} constants.
         */
        fun onPlayerStateChanged(isPlay: Boolean, @STATE playState: Int)
    }
}