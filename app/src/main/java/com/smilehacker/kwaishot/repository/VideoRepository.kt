package com.smilehacker.kwaishot.repository

import android.annotation.SuppressLint
import com.smilehacker.kwaishot.repository.eye.EyeVideoRepository
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.Observable

/**
 * Created by quan.zhou on 2018/6/14.
 */
class VideoRepository: DataProvider  {
    private val mEyeRepo by lazy { EyeVideoRepository() }

    @SuppressLint("CheckResult")
    override fun getPage(page: Int): Observable<List<VideoInfo>> {
        return mEyeRepo.getPage(page)
    }

    override fun nextPage(init: Boolean): Observable<List<VideoInfo>> {
        return mEyeRepo.nextPage(init)
    }

}