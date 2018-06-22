package com.smilehacker.kwaishot.common.mvvm

import android.arch.lifecycle.ViewModel
import android.support.annotation.CallSuper
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

/**
 * Created by quan.zhou on 2018/6/22.
 */
open class BaseViewModel: ViewModel() {
    private var mCompositeDisposable = CompositeDisposable()

    @CallSuper
    override fun onCleared() {
        super.onCleared()
        dispose()
    }

    fun Disposable?.autoDispose() {
        this?.let { addDispose(it) }
    }

    protected fun addDispose(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    protected fun dispose() {
        if (!mCompositeDisposable.isDisposed) {
            mCompositeDisposable.dispose()
        }
    }
}