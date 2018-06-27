package com.smilehacker.kwaishot.video

import android.animation.Animator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Point
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity.CENTER
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewPropertyAnimator
import android.widget.FrameLayout
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.player.CorePlayer
import com.smilehacker.kwaishot.repository.model.VideoInfo
import com.smilehacker.kwaishot.utils.DLog
import com.smilehacker.kwaishot.utils.bindView
import com.smilehacker.kwaishot.utils.widget.nest.DragAwayContainer


/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoFragment: Fragment(), CorePlayer.Listener {
//    private val mSurfaceView by bindView<SurfaceView>(R.id.surface)
    private val mPlayerContainer by bindView<FrameLayout>(R.id.player_container)
    private val mAnimContainer by bindView<FrameLayout>(R.id.anim_container)
    private val mIvCover by bindView<SimpleDraweeView>(R.id.iv_cover)
    private val mContainer by bindView<ConstraintLayout>(R.id.container)
    private val mSnapContainer by bindView<DragAwayContainer>(R.id.snap_container)
    private val mRvVideo by bindView<RecyclerView>(R.id.rv_video)
    private val mRvAdapter by lazy { VideoAdapter() }

    private lateinit var mVideoViewModel: VideoViewModel

    private var mVideoInfo: VideoInfo? = null
    private var mPlayer: CorePlayer? = null

    private var mAnimStart: ValueAnimator? = null
    private var mAnimEnd: ViewPropertyAnimator? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_video, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        DLog.d("isVisibleToUser $isVisibleToUser")
        DLog.d("mContainer y = ${mContainer.y} ${mContainer.translationY}")
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        if (mVideoInfo != null) {
            mVideoViewModel.setVideoInfo(mVideoInfo!!)
        }
        initUI()
        initData()
    }

    fun setVideoInfo(videoInfo: VideoInfo) {
        mVideoInfo = videoInfo
        if (this::mVideoViewModel.isInitialized) {
            mVideoViewModel.setVideoInfo(videoInfo)
        }
    }

    private fun initUI() {
        mRvVideo.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRvVideo.adapter = mRvAdapter
    }

    private fun initData() {
        mVideoViewModel.getVideoInfo().observe(this, Observer {
//            println("VideoInfo[$it]")
            if (it != null) {
                playVideo(it)
            }
            mRvAdapter.commitData(arrayListOf(VideoComponent.Model("aaa"), VideoComponent.Model("bbb")) as List<Any>?)
        })
    }

    private fun playVideo(videoInfo: VideoInfo) {
        mVideoInfo = videoInfo

        if (activity == null || activity!!.isDestroyed) {
            return
        }
        if (mPlayer != null) {
            releasePlayer()
        }

        mPlayer = CorePlayer(activity!!)
        mPlayer!!.init()
        mPlayer!!.addListener(this)
//        mSurfaceView.visibility = View.INVISIBLE
//        mPlayer!!.attachSurfaceView(mSurfaceView)
        mPlayer!!.prepare(videoInfo.videos[0].urls[0].url, true)
    }

    private fun applyVideoAspectRatio() {
        val videoRatio = mPlayer?.getVideoRatio() ?: return
        DLog.i("VideoRatio = $videoRatio")
        val display = activity?.windowManager?.defaultDisplay ?: return
        val size = Point()
        display.getSize(size)
        val displayRatio = size.x.toFloat() / size.y

        if (videoRatio > displayRatio) {
            mPlayerContainer.layoutParams.height = Math.round(mPlayerContainer.measuredWidth / videoRatio)
            mPlayerContainer.requestLayout()
        } else {
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            params.gravity = CENTER
            mPlayerContainer.layoutParams = params
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
//            mPlayer?.detachSurfaceView(mSurfaceView)
            mPlayer?.pause()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    private fun releasePlayer() {
//        mPlayer?.detachSurfaceView(mSurfaceView)
        mPlayer?.release()
        mPlayer = null
    }

    /**
     * player listener
     */

    override fun onPlayerStateChanged(isPlay: Boolean, playState: Int) {
        if (playState == CorePlayer.STATE_READY) {
            applyVideoAspectRatio()
//            mSurfaceView.visibility = View.VISIBLE

            if (isPlay) {
                animEnd()
            }
        }
    }

    /**
     * end player listener
     */


    fun startCoverAnimator(startX: Int, startY: Int, startWidth: Int, startHeight: Int) {
        mVideoInfo ?: return
        mAnimContainer.post { animStart(startX, startY, startWidth, startHeight) }
    }

    private fun animStart(startX: Int, startY: Int, startWidth: Int, startHeight: Int) {
        mPlayerContainer.visibility = View.INVISIBLE
        mAnimContainer.visibility = View.VISIBLE
        mContainer.alpha = 0f

        val coverLp = mIvCover.layoutParams
        coverLp.width = startWidth
        coverLp.height = startHeight
        mIvCover.translationX = startX.toFloat()
        mIvCover.translationY = startY.toFloat()
        mIvCover.setImageURI(mVideoInfo!!.normalCover)

        val endWidth = mAnimContainer.width
        val endHeight = (startWidth.toFloat() / startHeight * endWidth).toInt()

        val centerY = mAnimContainer.height / 2
        val endX = 0
        val endY = centerY - endHeight / 2

//        DLog.i("x $startX to $endX, y $startY to $endY, height $startHeight to $endHeight, width $startWidth to $endWidth")

        val anim = ValueAnimator.ofInt(100)
        mAnimStart = anim
        val stepX = (endX - startX) / 100f
        val stepY = (endY - startY) / 100f
        val heightStep = (endHeight - startHeight) / 100f
        val widthStep = (endWidth - startWidth) / 100f
//        DLog.i("heightStep $heightStep   widthStep $widthStep")
        anim.duration = 500
        anim.addUpdateListener {
            val value = it.animatedValue as Int

            mIvCover.translationX = (startX + stepX * value)
            mIvCover.translationY = (startY + stepY * value)

            mContainer.alpha = 0.01f * value

            val lp = mIvCover.layoutParams
            lp.height = (startHeight + heightStep * value).toInt()
            lp.width = (startWidth + widthStep * value).toInt()
            mIvCover.requestLayout()
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                mAnimContainer.post {
                    mPlayer?.play()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        anim.start()
    }

    private fun animEnd() {
        mPlayerContainer.visibility = View.VISIBLE
        mAnimEnd = mAnimContainer.animate()
        mAnimEnd!!.alpha(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        mAnimContainer.post {
                            mAnimContainer.visibility = View.GONE
                            mAnimContainer.alpha = 1f
                        }
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        mAnimContainer.post {
                            mAnimContainer.visibility = View.GONE
                            mAnimContainer.alpha = 1f
                        }
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                    override fun onAnimationRepeat(animation: Animator?) {
                    }


                })
                .setDuration(1000)
                .start()

    }

    fun onBackPress() {
        stopAnim()
        releasePlayer()
        mContainer.alpha = 0f
//        mSnapContainer.reset()
    }

    private fun stopAnim() {
        mAnimEnd?.cancel()
        mAnimStart?.cancel()
    }
}