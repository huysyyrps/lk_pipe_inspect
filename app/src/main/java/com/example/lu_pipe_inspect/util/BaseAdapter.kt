package com.example.lu_pipe_inspect.util

import android.app.Activity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.lu_pipe_inspect.R

object BaseAdapter {
    fun pipeLeave(activity: Activity, spPipeLevel: Spinner, dataList: Array<String>, param: AdapterCallBack) {
        val pipeLevelAdapter: ArrayAdapter<String> = ArrayAdapter<String>(activity, R.layout.adapter_item_layout, dataList)
        spPipeLevel.adapter = pipeLevelAdapter
        spPipeLevel.setSelection(0)
        spPipeLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, pos: Int, id: Long) {
                val pipeLeave = dataList[pos]
                param.backData(pipeLeave)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Another interface callback
            }
        }
    }
}