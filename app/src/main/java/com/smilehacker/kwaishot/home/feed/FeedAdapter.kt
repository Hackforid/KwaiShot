package com.smilehacker.kwaishot.home.feed

import com.smilehacker.kwaishot.ui.component.VideoCellComponent
import com.smilehacker.lego.LegoAdapter

class FeedAdapter(private val videoCellListener: VideoCellComponent.VideoCellListener? = null): LegoAdapter() {

    init {
        register(VideoCellComponent().also { it.setListener(videoCellListener) })
        setDiffUtilEnabled(true)
        setDiffModelChanged(true)
    }
}