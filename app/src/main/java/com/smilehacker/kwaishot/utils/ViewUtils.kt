package com.smilehacker.kwaishot.utils

import com.smilehacker.kwaishot.App

class ViewUtils {
    companion object {

        @JvmStatic
        fun getScreenWidth() : Int{
            return getContext().resources.displayMetrics.widthPixels
        }

        @JvmStatic
        private inline fun getContext() = App.instance.applicationContext
    }
}