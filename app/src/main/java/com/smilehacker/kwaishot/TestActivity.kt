package com.smilehacker.kwaishot

import android.os.Bundle
import com.smilehacker.kwaishot.common.BaseActivity

class TestActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_test)
    }
}