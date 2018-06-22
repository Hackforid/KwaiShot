package com.smilehacker.kwaishot.home

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.smilehacker.kwaishot.common.mvvm.BaseViewModel
import com.smilehacker.kwaishot.common.mvvm.DiffMutableLiveData
import com.smilehacker.kwaishot.common.mvvm.ErrorStatus
import com.smilehacker.kwaishot.repository.VideoRepository
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel: BaseViewModel() {


    private val mRepo by lazy { VideoRepository() }
    private val mVideos by lazy { MutableLiveData<List<VideoInfo>>().also { it.value = ArrayList() } }
    private val mErrorStatus by lazy { MutableLiveData<ErrorStatus>() }

    private val mHasNextPage by lazy { DiffMutableLiveData<Boolean>() }

    fun getVideos() : LiveData<List<VideoInfo>> {
        return mVideos
    }

    fun getErrorStatus() : LiveData<ErrorStatus> {
        return mErrorStatus
    }

    fun getHasNextPage() : LiveData<Boolean> = mHasNextPage

    fun loadNextPage() {
        nextPage()
    }

    fun refreshPage() {
        nextPage(true)
    }


    @SuppressLint("CheckResult")
    private fun nextPage(init: Boolean = false) {
        mRepo.nextPage(init)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    videos ->
                    mHasNextPage.value = videos.isNotEmpty()
                    val newList = if (init) videos else mVideos.value!!.toMutableList().also { it.addAll(videos) }
                    mVideos.value = newList
                }, {
                    it.printStackTrace()
                    mErrorStatus.value = ErrorStatus(100, it.message)
                })
                .autoDispose()
    }

}