package com.smilehacker.kwaishot.video

import android.animation.Animator
import android.animation.ValueAnimator
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.graphics.Point
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.view.Gravity.CENTER
import android.widget.FrameLayout
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.event.CloseVideoPageEvent
import com.smilehacker.kwaishot.player.CorePlayer
import com.smilehacker.kwaishot.repository.model.PosInfo
import com.smilehacker.kwaishot.repository.model.VideoInfo
import com.smilehacker.kwaishot.utils.DLog
import com.smilehacker.kwaishot.utils.RecyclerPagerHelper
import com.smilehacker.kwaishot.utils.RxBus
import com.smilehacker.kwaishot.utils.bindView
import com.smilehacker.kwaishot.utils.widget.nest.DragAwayContainer
import com.ushowmedia.mipha.hyrule.utils.ext.nullOr
import java.lang.ref.WeakReference


/**
 * Created by quan.zhou on 2018/6/22.
 */
class VideoFragment: Fragment(), CorePlayer.Listener {
    private val mAnimContainer by bindView<FrameLayout>(R.id.anim_container)
    private val mIvCover by bindView<SimpleDraweeView>(R.id.iv_cover)
    private val mContainer by bindView<FrameLayout>(R.id.container)
    private val mSnapContainer by bindView<DragAwayContainer>(R.id.snap_container)
    private val mRvVideo by bindView<RecyclerView>(R.id.rv_video)
    private val mRvAdapter by lazy { VideoAdapter() }
    private val mLayoutManager by lazy { LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false) }

    private var mTextureViewRef : WeakReference<TextureView>? = null

    private lateinit var mVideoViewModel: VideoViewModel

    private var mVideoInfo: VideoInfo? = null
    private var mPlayer: CorePlayer? = null

    private var mAnimStart: ValueAnimator? = null
    private var mAnimEnd: ViewPropertyAnimator? = null

    private val mRecyclerPagerHelper by lazy { RecyclerPagerHelper() }
//    private var mSurfaceView: SurfaceView? = null
    private var mPostInfo: PosInfo? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frg_video, container, false)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        DLog.d("isVisibleToUser $isVisibleToUser")
        if (isVisibleToUser) {
            mSnapContainer.reset()
            mVideoViewModel.readCurrentVideo()
        } else {
            releasePlayer()
            stopAnim()
            mContainer.alpha = 0f
        }
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mVideoViewModel = ViewModelProviders.of(this).get(VideoViewModel::class.java)
        initUI()
        initData()
    }


    private fun initUI() {
        mRvVideo.layoutManager = mLayoutManager
        mRvVideo.adapter = mRvAdapter

        mRecyclerPagerHelper.attachToRecyclerView(mRvVideo)

        mRecyclerPagerHelper.addListener(object : RecyclerPagerHelper.RecyclerPagerListener {
            override fun onPageSelected(lastPos: Int, currentPos: Int) {
                DLog.i("onItemSelected $lastPos $currentPos")
                if (currentPos >= 0 && currentPos != lastPos) {
                    mVideoViewModel.changeVideo(currentPos)
//                    showVideoAtPage(currentPos)
                }
            }
        })

        mSnapContainer.setListener(object : DragAwayContainer.DragAwayListener {
            override fun shouldDismiss(dragY: Float, pos: FloatArray, rect: IntArray): Boolean {
                DLog.i("dragY = ${dragY}")
                if (Math.abs(dragY) > mSnapContainer.height / 3) {
                    mPostInfo?.let {
                        pos[0] = it.x.toFloat()
                        pos[1] = it.y.toFloat()
                        rect[0] = it.width
                        rect[1] = it.height
                    }
                    return true
                }
                return false
            }

            override fun onDismiss() {
                RxBus.post(CloseVideoPageEvent())
            }

        })

    }

    private fun initData() {
        mVideoViewModel.getVideoList().observe(this, Observer {
            list ->
            list.nullOr(ArrayList())
                    .map { VideoComponent.Model(it.videos[0].urls[0].url) }
                    .let { mRvAdapter.commitData(it) }
        })
        mVideoViewModel.getVideoInfo().observe(this, Observer {
            if (it != null) {
                mVideoInfo = it
                val pos = mVideoViewModel.getVideoList().value!!.indexOf(it)
                mRecyclerPagerHelper.setCurrentPos(pos)
                showVideoAtPage(pos)
            }
        })
        mVideoViewModel.getPlayer().observe(this, Observer {
            mPlayer = it
            mPlayer!!.addListener(this)
        })
    }

    private fun showVideoAtPage(pos: Int) {
        mRvVideo.post {
            val itemView = mLayoutManager.findViewByPosition(pos)
            DLog.i("find itemView $itemView")

            val textureView = itemView?.findViewById<TextureView>(R.id.surface_view)
            mPlayer?.attachSurfaceView(textureView)

            mTextureViewRef = if (textureView != null) {
                WeakReference(textureView)
            } else {
                null
            }

            if (textureView != null) {
                applyVideoAspectRatio(textureView)
            }
        }

    }


    private fun applyVideoAspectRatio(view: View) {
        val player = mPlayer ?: return
        if (!player.isReady()) {
            return
        }
        val videoRatio = player.getVideoRatio() ?: return
        DLog.i("VideoRatio = $videoRatio")
        val display = activity?.windowManager?.defaultDisplay ?: return
        val size = Point()
        display.getSize(size)
        val displayRatio = size.x.toFloat() / size.y

        if (videoRatio > displayRatio) {
            view.layoutParams.height = Math.round(view.measuredWidth / videoRatio)
            view.requestLayout()
        } else {
            val params = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            params.gravity = CENTER
            view.layoutParams = params
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        releasePlayer()
    }

    private fun releasePlayer() {
        mPlayer?.clearSurface()
        mPlayer?.stop()
    }

    /**
     * player listener
     */

    override fun onPlayerStateChanged(isPlay: Boolean, playState: Int) {
        if (playState == CorePlayer.STATE_READY) {
            mTextureViewRef?.get()?.let {
                applyVideoAspectRatio(it)
            }
        }
    }

    /**
     * end player listener
     */


    fun startCoverAnimator(startX: Int, startY: Int, startWidth: Int, startHeight: Int) {
        DLog.i("start CoverAnimator")
        mPostInfo = PosInfo(startX, startY, startWidth, startHeight)
        mAnimContainer.post { animStart(startX, startY, startWidth, startHeight) }
    }

    private fun animStart(startX: Int, startY: Int, startWidth: Int, startHeight: Int) {
        mRvVideo.visibility = View.INVISIBLE
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


        val anim = ValueAnimator.ofInt(100)
        mAnimStart = anim
        val stepX = (endX - startX) / 100f
        val stepY = (endY - startY) / 100f
        val heightStep = (endHeight - startHeight) / 100f
        val widthStep = (endWidth - startWidth) / 100f
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
            DLog.d("anim $value")
        }
        anim.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                animEnd()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        anim.start()
    }

    private fun animEnd() {
        mRvVideo.visibility = View.VISIBLE
        mAnimEnd = mAnimContainer.animate()
        mAnimEnd!!.alpha(0f)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationEnd(animation: Animator?) {
                        mAnimContainer.post {
                            mAnimContainer.visibility = View.GONE
                            mAnimContainer.alpha = 1f
                        }
                        mPlayer?.play()
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
    }

    private fun stopAnim() {
        mAnimEnd?.cancel()
        mAnimStart?.cancel()
    }
}