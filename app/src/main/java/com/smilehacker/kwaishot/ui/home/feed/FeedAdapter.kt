package com.smilehacker.kwaishot.ui.home.feed

import com.smilehacker.kwaishot.ui.component.VideoCellComponent
import com.smilehacker.lego.LegoAdapter

class FeedAdapter: LegoAdapter() {

    init {
        register(VideoCellComponent())
        setDiffUtilEnabled(true)
        setDiffModelChanged(true)
    }
}