package com.smilehacker.kwaishot

import android.app.Application
import com.tspoon.traceur.Traceur

/**
 * Created by quan.zhou on 2018/6/15.
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Traceur.enableLogging()
        }
    }
}