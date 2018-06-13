package com.smilehacker.kwaishot

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.smilehacker.kwaishot.ui.home.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment())
                    .commitNow()
        }
    }

}
