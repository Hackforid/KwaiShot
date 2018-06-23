package com.smilehacker.kwaishot.ui.component

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.facebook.drawee.view.SimpleDraweeView
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.utils.ViewUtils
import com.smilehacker.kwaishot.utils.bindView
import com.smilehacker.lego.LegoComponent
import com.smilehacker.lego.annotation.LegoField
import com.smilehacker.lego.annotation.LegoIndex

/**
 * Created by quan.zhou on 2018/6/15.
 */
class VideoCellComponent : LegoComponent<VideoCellComponent.ViewHolder, VideoCellComponent.Model>() {

    private val mScreenWidth by lazy { ViewUtils.getScreenWidth() }

    override fun onBindData(vh: ViewHolder, model: Model) {
        vh.cover.setImageURI(model.coverUrl)
        val width = mScreenWidth / 2
        val height = if (model.isFirst) {
            width
        } else {
            (width * 1.5).toInt()
        }

        vh.itemView.layoutParams.height = height

        vh.title.text = model.title
        vh.avatar.setImageURI(model.avatar)

        vh.itemView.setOnClickListener {
            val pos = IntArray(2)
            vh.cover.getLocationInWindow(pos)
//            DLog.i("itemView: ${vh.cover.height} ${vh.cover.width} ${pos[0]} ${pos[1]}")
            val posInfo = VideoCellPosInfo(pos[0], pos[1], vh.cover.width, vh.cover.height)
            mListener?.onVideoCellClick(model.id, posInfo)
        }
    }

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_video_cell, parent, false)
        return ViewHolder(view)
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover by bindView<SimpleDraweeView>(R.id.iv_cover)
        val title by bindView<TextView>(R.id.tv_title)
        val avatar by bindView<SimpleDraweeView>(R.id.iv_avatar)
    }

    data class Model(@LegoIndex @JvmField val id: Long,
                     @LegoField @JvmField val coverUrl: String,
                     @LegoField @JvmField val title: String?,
                     @LegoField @JvmField val avatar: String?,
                     val isFirst: Boolean = false)

    interface VideoCellListener {
        fun onVideoCellClick(id: Long, posInfo: VideoCellPosInfo? = null)
    }

    private var mListener: VideoCellListener? = null

    fun setListener(listener: VideoCellListener?) {
        mListener = listener
    }

    data class VideoCellPosInfo(val x: Int, val y: Int, val width: Int, val height: Int)
}