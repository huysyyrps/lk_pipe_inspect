package com.example.lu_pipe_inspect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.activity.MainActivity
import com.example.lu_pipe_inspect.util.AdapterCallBack
import com.example.lu_pipe_inspect.util.BaseAdapter
import com.example.lu_pipe_inspect.util.KeyboardHide.hideKeyboard
import com.example.lu_pipe_inspect.util.MyApplication
import com.example.lu_pipe_inspect.util.showToast
import kotlinx.android.synthetic.main.fragment_pressure.*
import kotlinx.android.synthetic.main.fragment_pressure.etLastLand
import kotlinx.android.synthetic.main.fragment_pressure.etMinLand
import kotlinx.android.synthetic.main.fragment_pressure.etNextYear
import kotlinx.android.synthetic.main.fragment_pressure.etUserYear
import kotlinx.android.synthetic.main.fragment_thinning.*
import java.math.BigDecimal

class PressureFragment : Fragment() {
    private lateinit var mainActivity: Activity
    private lateinit var selectSefect: String
//    private lateinit var autoSelect: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pressure, container, false)
    }

    override fun onStart() {
        super.onStart()
        if (activity != null) {
            mainActivity = activity as MainActivity
        }
        setAdapter()
        setClient()
    }

    /**
     * spinner添加适配器
     */
    private fun setAdapter() {
        //管道材料
        BaseAdapter.pipeLeave(mainActivity, spPSelect, MyApplication.context.resources.getStringArray(R.array.pipe), object : AdapterCallBack {
                override fun backData(data: String) {
                    selectSefect = data
                    if (selectSefect == "直管") {
                        linStraightTop.visibility = View.VISIBLE
                        linStraightBot.visibility = View.VISIBLE
                        linElbowTop.visibility = View.GONE
                        linElbowBot.visibility = View.GONE
//                        linAuto.visibility = View.GONE
                    } else if (selectSefect == "弯头") {
                        linStraightTop.visibility = View.GONE
                        linStraightBot.visibility = View.GONE
                        linElbowTop.visibility = View.VISIBLE
                        linElbowBot.visibility = View.VISIBLE
//                        linAuto.visibility = View.VISIBLE
                    }
                    tvCThickness.text = ""
                    tvResult.text = ""
                    tvInI.text = ""
                    tvOutI.text = ""
                    tvInTW.text = ""
                    tvOutTW.text = ""
                    tvWithinResult.text = ""
                    tvOutResult.text = ""
                }
            })
//        //计算选择
//        BaseAdapter.pipeLeave(mainActivity, spSelectCount, MyApplication.context.resources.getStringArray(R.array.select_count), object : AdapterCallBack {
//                @SuppressLint("SetTextI18n")
//                override fun backData(data: String) {
//                    autoSelect = data
//                    if (autoSelect == "自动计算") {
//                        linNextYear.setVisibility(View.GONE)
//                        linNextYearAuto.setVisibility(View.VISIBLE)
//                    } else if (autoSelect == "手动计算") {
//                        linNextYear.setVisibility(View.VISIBLE)
//                        linNextYearAuto.setVisibility(View.GONE)
//                    }
//                    tvCData.text = ""
//                    tvTeData.text = ""
//                    tvPl0Data.text = ""
//                    tvLevel.text = ""
//                    etStrength.setText(strength.toString() + "")
//                }
//            })
    }

    /**
     * 计算按钮点击事件
     */
    @SuppressLint("SetTextI18n")
    private fun setClient() {
        btnPressur.setOnClickListener{
            hideKeyboard()
            if (etWorkMPa.text.toString().trim { it <= ' ' } == "") {
                "允许（监控）使用压力不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etPipeOD.text.toString().trim { it <= ' ' } == "") {
                "管道外径不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etUserMpa.text.toString().trim { it <= ' ' } == "") {
                "金属材料的许用应力不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etCCoefficient.text.toString().trim { it <= ' ' } == "") {
                "计算系数不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etLastLand.text.toString().trim { it <= ' ' } == "") {
                "名义壁厚不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etMinLand.text.toString().trim { it <= ' ' } == "") {
                "本次定期检验缺陷附近壁厚实测最小值不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etUserYear.text.toString().trim { it <= ' ' } == "") {
                "两次定期检验间隔年限或首次定检年限不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            if (etNextYear.text.toString().trim { it <= ' ' } == "") {
                "预测下一周期年限不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            }
            val workMpa = etWorkMPa.text.toString().toDouble()
            val pipeOD = etPipeOD.text.toString().toDouble()
            val userMpa = etUserMpa.text.toString().toDouble()
            val pipeCoefficient = etPipeCoefficient.text.toString().toDouble()
            val cCoefficient = etCCoefficient.text.toString().toDouble()
            val lastLand = etLastLand.text.toString().trim { it <= ' ' }.toDouble()
            val minLand = etMinLand.text.toString().trim { it <= ' ' }.toDouble()
            val userYear = etUserYear.text.toString().trim { it <= ' ' }.toDouble()
            val nextYear = etNextYear.text.toString().trim { it <= ' ' }.toDouble()

            val fsSpeed = BigDecimal((lastLand - minLand)/userYear).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
            tvFSSpeed.text = fsSpeed.toString()

            val fsl = (fsSpeed * nextYear)
            tvNextFSL.text = fsl.toString()

            if (selectSefect == "直管"){
                if (etMinLand.text.toString().trim { it <= ' ' } == "")  {
                    "本次实测直管壁厚最小值".showToast(MyApplication.context)
                    return@setOnClickListener
                }else{
                    val tData = (workMpa * pipeOD) / (2 * (userMpa * pipeCoefficient + workMpa * cCoefficient))
                    val result = BigDecimal(tData).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
                    tvCThickness.text = result.toString() + ""

                    val minStraightLand = etMinStraightLand.text.toString().toDouble()
                    if (minStraightLand - result>fsl) {
                        tvResult.text = "直管强度校核合格"
                        tvResult.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvResult.text = "直管强度校核不合格"
                        tvResult.setTextColor(resources.getColor(R.color.red))
                    }
                }
            }
            if (selectSefect == "弯头"){
                if (etRadius.text.toString().trim { it <= ' ' } == "")  {
                    "弯头的弯曲半径不能为空".showToast(MyApplication.context)
                    return@setOnClickListener
                } else if (etInMin.text.toString().trim { it <= ' ' } == "")  {
                    "实测弯头内侧壁厚最小值".showToast(MyApplication.context)
                    return@setOnClickListener
                } else if (etOutMin.text.toString().trim { it <= ' ' } == "")  {
                    "实测弯头外侧壁厚最小值".showToast(MyApplication.context)
                    return@setOnClickListener
                }else{
                    val radius = etRadius.text.toString().toDouble()
                    val inMin = etInMin.text.toString().toDouble()
                    val outMin = etOutMin.text.toString().toDouble()
                    val moleculeI = 4 * (radius / pipeOD) - 1
                    val denominatorI = 4 * (radius / pipeOD) - 2
                    val inI = BigDecimal(moleculeI / denominatorI).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
                    tvInI.text = inI.toString()

                    val denominatorOut = 4 * (radius / pipeOD) + 2
                    val outI = BigDecimal(moleculeI / denominatorOut).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
                    tvOutI.text = outI.toString()

                    val moleculeTW = workMpa * pipeOD
                    val denominatorInTW = 2 * ((userMpa * pipeCoefficient) / inI + (workMpa * cCoefficient))
                    val inTW = BigDecimal(moleculeTW/denominatorInTW).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
                    tvInTW.text = inTW.toString() + ""

                    val denominatorOutTW = 2 * ((userMpa * pipeCoefficient) / outI + (workMpa * cCoefficient))
                    val outTW = BigDecimal(moleculeTW/denominatorOutTW).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
                    tvOutTW.text = outTW.toString() + ""

                    if (inMin-fsl > inTW) {
                        tvWithinResult.text = "弯头内侧强度校核合格"
                        tvWithinResult.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvWithinResult.text = "弯头内侧强度校核不合格"
                        tvWithinResult.setTextColor(resources.getColor(R.color.red))
                    }

                    if (outMin - fsl > outTW) {
                        tvOutResult.text = "弯头外侧强度校核合格"
                        tvOutResult.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvOutResult.text = "弯头外侧强度校核不合格"
                        tvOutResult.setTextColor(resources.getColor(R.color.red))
                    }
                }
            }
        }
    }
}