package com.smilehacker.kwaishot.repository

import android.annotation.SuppressLint
import com.smilehacker.kwaishot.repository.eye.EyeVideoRepository
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.Observable

/**
 * Created by quan.zhou on 2018/6/14.
 */
class VideoRepository  {

    private val mEyeRepo by lazy { EyeVideoRepository() }

    @SuppressLint("CheckResult")
    fun getPage(page: Int): Observable<List<VideoInfo>> {
        return mEyeRepo.getPage(page)
    }

}