package com.example.lu_pipe_inspect.data

import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.util.MyApplication

object BannerData {
    fun setBannerData(): ArrayList<Int> {
        val bannerList = ArrayList<Int>()
        bannerList.apply {
            add(R.drawable.banner1)
            add(R.drawable.banner1)
            add(R.drawable.banner1)
            add(R.drawable.banner1)
            add(R.drawable.banner1)
        }
        return bannerList
    }
}
