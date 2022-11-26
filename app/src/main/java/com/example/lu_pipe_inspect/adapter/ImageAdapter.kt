package com.example.lu_pipe_inspect.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.util.MyApplication
import com.youth.banner.adapter.BannerAdapter

class ImageAdapter(val datas: ArrayList<Int>?) : BannerAdapter<Int, ImageAdapter.ViewHolder>(datas) {

    //定义ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        }

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder? {
        val view = LayoutInflater.from(MyApplication.context).inflate(R.layout.adapter_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindView(holder: ViewHolder?, data: Int?, position: Int, size: Int) {
        if (data != null) {
            holder?.imageView?.setImageResource(data)
        }
    }

}