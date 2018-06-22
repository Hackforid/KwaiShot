package com.smilehacker.kwaishot

import android.os.Bundle
import com.smilehacker.kwaishot.common.BaseActivity
import com.smilehacker.kwaishot.home.HomeFragment

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        floatStatusBar()


        setContentView(R.layout.main_activity)
        supportActionBar?.hide()
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment())
                    .commitNow()
        }
    }

}
