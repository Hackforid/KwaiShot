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
                    val newList = if (page == 0) it else mVideos.value!!.toMutableList().also { it.addAll(it) }
                    mVideos.value = newList
                    mCurrentPage = page
                }, {
                    it.printStackTrace()
                    mErrorStatus.value = ErrorStatus(100, it.message)
                })
    }


}