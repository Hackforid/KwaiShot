package com.smilehacker.kwaishot

import android.app.Application
import android.content.Context
import com.facebook.drawee.backends.pipeline.Fresco
import com.tspoon.traceur.Traceur

/**
 * Created by quan.zhou on 2018/6/15.
 */
class App: Application() {

    companion object {
        @JvmStatic
        lateinit var instance : App
            private set
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        instance = this
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Traceur.enableLogging()
        }
        Fresco.initialize(this)
    }
}