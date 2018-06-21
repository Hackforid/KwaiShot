package com.smilehacker.kwaishot.ui.home

import android.annotation.SuppressLint
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.smilehacker.kwaishot.common.mvvm.ErrorStatus
import com.smilehacker.kwaishot.repository.VideoRepository
import com.smilehacker.kwaishot.repository.model.VideoInfo
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class HomeViewModel: ViewModel() {


    private val mRepo by lazy { VideoRepository() }
    private val mVideos by lazy { MutableLiveData<List<VideoInfo>>().also { it.value = ArrayList() } }
    private val mErrorStatus by lazy { MutableLiveData<ErrorStatus>() }

    private var mCurrentPage = 0

    fun getVideos() : LiveData<List<VideoInfo>> {
        return mVideos
    }

    fun getErrorStatus() : LiveData<ErrorStatus> {
        return mErrorStatus
    }

    fun loadNextPage() {
        println("load page ${mCurrentPage + 1}")
        loadPage(mCurrentPage + 1)
    }

    fun refreshPage() {
        loadPage(0)
    }

    @SuppressLint("CheckResult")
    private fun loadPage(page: Int) {
        mRepo.getPage(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    videos ->
                    println(videos)
                    val newList = if (page == 0) videos else mVideos.value!!.toMutableList().also { it.addAll(videos) }
                    mVideos.value = newList
                    mCurrentPage = page
                }, {
                    it.printStackTrace()
                    mErrorStatus.value = ErrorStatus(100, it.message)
                })
    }


}