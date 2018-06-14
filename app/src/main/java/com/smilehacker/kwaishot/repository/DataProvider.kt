package com.smilehacker.kwaishot.repository

import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.Observable

/**
 * Created by quan.zhou on 2018/6/14.
 */
interface DataProvider {
    fun getPage(page: Int) : Observable<List<VideoInfo>>
}
