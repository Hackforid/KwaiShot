package com.smilehacker.kwaishot.repository

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.smilehacker.kwaishot.repository.eye.EyeVideoRepository
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by quan.zhou on 2018/6/14.
 */
class VideoRepository  {

    private val mEyeRepo by lazy { EyeVideoRepository() }

    @SuppressLint("CheckResult")
    fun getPage(page: Int): LiveData<List<VideoInfo>> {
        val data = MutableLiveData<List<VideoInfo>>()
        mEyeRepo.getPage(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    data.value = it
                }, {
                    Log.e("", "", it)
                })
        return data
    }

}