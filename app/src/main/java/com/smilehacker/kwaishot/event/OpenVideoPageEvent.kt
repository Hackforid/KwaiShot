package com.smilehacker.kwaishot.event

import com.smilehacker.kwaishot.repository.model.VideoInfo

data class OpenVideoPageEvent(
        val videoInfo: VideoInfo
)