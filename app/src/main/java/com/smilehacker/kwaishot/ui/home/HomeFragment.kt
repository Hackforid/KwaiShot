package com.smilehacker.kwaishot.ui.home

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smilehacker.kwaishot.R

class HomeFragment : Fragment() {
    private lateinit var mViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        println("onCreateView")
        val view = inflater.inflate(R.layout.frg_home, container, false)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        initViewModel()
        mViewModel.refreshPage()
    }

    private fun initViewModel() {
        mViewModel.getVideos().observe(this, Observer {
            list -> println(list)
        })

        mViewModel.getErrorStatus().observe(this, Observer {
            println(it)
        })
    }
}