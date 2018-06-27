package com.smilehacker.kwaishot.video

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R
import com.smilehacker.kwaishot.utils.bindView
import com.smilehacker.lego.LegoComponent
import com.smilehacker.lego.annotation.LegoIndex

class VideoComponent : LegoComponent<VideoComponent.ViewHolder, VideoComponent.Model>() {

    override fun onBindData(viewHolder: ViewHolder, model: Model) {
    }

    override fun getViewHolder(parent: ViewGroup): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.component_video, parent, false)
        return ViewHolder(view)
    }


    class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val surfaceView by bindView<SurfaceView>(R.id.surface_view)
    }

    data class Model(
            @LegoIndex @JvmField val videoUrl: String
    )
}