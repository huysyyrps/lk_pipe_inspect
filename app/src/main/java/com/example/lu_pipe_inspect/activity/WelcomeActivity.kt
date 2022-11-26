package com.example.lu_pipe_inspect.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.adapter.ImageAdapter
import com.example.lu_pipe_inspect.data.BannerData
import com.example.lu_pipe_inspect.util.BaseActivity
import com.example.lu_pipe_inspect.util.MyApplication
import com.example.lu_pipe_inspect.util.showToast
import com.youth.banner.indicator.CircleIndicator
import com.youth.banner.listener.OnBannerListener
import kotlinx.android.synthetic.main.activity_welcome.*
import java.io.BufferedReader
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class WelcomeActivity : BaseActivity(),OnBannerListener<Int>{
    var i:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        val imgList = BannerData.setBannerData()
        banner.apply {
            setIndicator(CircleIndicator(MyApplication.context))
                .setAdapter(ImageAdapter(imgList), true)
                .setOnBannerListener(this@WelcomeActivity)
        }
        btnIn.setOnClickListener {
//            MainActivity.actionStart(this)
//            finish()
//            ActivityCode.actionStart(this)
//            finish()
            val s: String? = load()
            val LNowDate = Date().time
            Log.e("X", LNowDate.toString() + "")
            val intent: Intent
            if (s == null || s == "") {
                ActivityCode.actionStart(this)
                finish()
            } else if (LNowDate < java.lang.Long.valueOf(s)) {
                MainActivity.actionStart(this)
                finish()
            } else if (LNowDate == java.lang.Long.valueOf(s)) {
                "激活码即将到期".showToast(this)
                MainActivity.actionStart(this)
                finish()
            } else if (LNowDate > java.lang.Long.valueOf(s)) {
                ActivityCode.actionStart(this)
                finish()
            }

        }
    }

    //从“data/data/com.example.项目名/files/data”中读取String
    private fun load(): String? {
        var `in`: FileInputStream? = null
        var reader: BufferedReader? = null
        val content = StringBuilder()
        try {
            `in` = openFileInput("data") //文件名
            reader = BufferedReader(InputStreamReader(`in`))
            var line: String? = ""
            while (reader.readLine().also { line = it } != null) {
                content.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            if (reader != null) {
                try {
                    reader.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return content.toString()
    }

    override fun OnBannerClick(data: Int?, position: Int) {
        var url = when(position){
            0-> "http://www.lkndt.com/jldcc/"
            1-> "http://www.lkndt.com/cftsy/"
            2-> "http://www.lkndt.com/cftsy/"
            3-> "http://www.lkndt.com/jkchy/"
            4-> "http://www.lkndt.com/cftsy/"
            else -> {
                "http://www.lkndt.com/"
            }
        }
        val intent =  Intent();
        intent.action = Intent.ACTION_VIEW;//为Intent设置动作
        intent.data = Uri.parse(url);//为Intent设置数据
        startActivity(intent);//将Intent传递给Activity
    }

}
