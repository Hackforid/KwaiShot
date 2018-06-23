package com.smilehacker.kwaishot.event

import com.smilehacker.kwaishot.repository.model.VideoInfo
import com.smilehacker.kwaishot.ui.component.VideoCellComponent

data class OpenVideoPageEvent(
        val videoInfo: VideoInfo,
        val videoPostInfo: VideoCellComponent.VideoCellPosInfo? = null
)