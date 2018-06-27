package com.smilehacker.kwaishot.video

import com.smilehacker.lego.LegoAdapter

class VideoAdapter: LegoAdapter() {

    init {
        setDiffUtilEnabled(true)
        setDiffModelChanged(true)


        register(VideoComponent())
    }

}

