package com.smilehacker.kwaishot.utils.ext

import com.smilehacker.kwaishot.utils.DLog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * Created by quan.zhou
 * on 2017/7/13.
 */

fun <T> Observable<T>?.subscribeSafely() = this?.subscribe({}, {})

fun <T> Observable<T>?.subscribeSafely(consumer: Consumer<T>)
     = this?.subscribe(consumer, Consumer<Throwable> { t -> DLog.e(t) })


inline fun <reified T> Observable<T>.applyComputeSchedulers(): Observable<T> {
    return this.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
}

inline fun <reified T> Observable<T>.intervalRequest(): Observable<T> {
    return this.throttleFirst(1000, TimeUnit.MILLISECONDS)
}