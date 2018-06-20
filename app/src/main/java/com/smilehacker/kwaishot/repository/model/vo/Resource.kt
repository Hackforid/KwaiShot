package com.smilehacker.kwaishot.repository.model.vo

import android.support.annotation.IntDef

/**
 * Created by quan.zhou on 2018/6/15.
 */
data class Resource<T>(@Status val status: Int, val data: T? = null, val message: String? = null) {
    companion object {

        const val LOADING = 1
        const val SUCCESS = 0
        const val ERROR = -1

        @IntDef(LOADING, SUCCESS, ERROR)
        @Retention(AnnotationRetention.SOURCE)
        annotation class Status


        fun <T> success(data: T? = null): Resource<T> {
            return Resource(SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T? = null): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data, null)
        }
    }
}


