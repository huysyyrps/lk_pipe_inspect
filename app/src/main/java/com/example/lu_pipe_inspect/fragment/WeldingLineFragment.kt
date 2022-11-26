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
import kotlinx.android.synthetic.main.fragment_pressure.spPSelect
import kotlinx.android.synthetic.main.fragment_thinning.*
import kotlinx.android.synthetic.main.fragment_welding_line.*
import kotlinx.android.synthetic.main.fragment_welding_line.etLastLand
import kotlinx.android.synthetic.main.fragment_welding_line.etMinLand
import kotlinx.android.synthetic.main.fragment_welding_line.etNextYear
import kotlinx.android.synthetic.main.fragment_welding_line.etUserYear
import kotlinx.android.synthetic.main.fragment_welding_line.spPipeLevel
import kotlinx.android.synthetic.main.fragment_welding_line.tvCData
import kotlinx.android.synthetic.main.fragment_welding_line.tvLevel
import kotlinx.android.synthetic.main.fragment_welding_line.tvTeData
import java.math.BigDecimal

class WeldingLineFragment : Fragment() {
    private lateinit var mainActivity: Activity
    private lateinit var selectSefect: String
    private lateinit var pipeLeave: String
    private lateinit var autoSelect: String
    private lateinit var fusePipeLeave : String
    private var lastLand: Double = 0.0
    private var minLand: Double = 0.0
    private var userYear: Double = 0.0
    private var nextYear: Double = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welding_line, container, false)
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
        //缺陷类型
        BaseAdapter.pipeLeave(mainActivity, spPSelect, MyApplication.context.resources.getStringArray(R.array.weld), object : AdapterCallBack {
                override fun backData(data: String) {
                    selectSefect = data
                    if (selectSefect == "圆形缺陷") {
                        linStripTop.visibility = View.GONE
                        linConstant.visibility = View.VISIBLE
                        linCircular.visibility = View.VISIBLE
                        linLeave1.visibility = View.GONE
                        linLeave2.visibility = View.VISIBLE
                        linFuse.visibility = View.GONE
                        linStripBot.visibility = View.GONE
                        linUndercut.visibility = View.GONE
                        linError.visibility = View.GONE
                        linFuseLeavel.visibility = View.GONE
                    } else if (selectSefect == "条形缺陷") {
                        linStripTop.visibility = View.VISIBLE
                        linConstant.visibility = View.VISIBLE
                        linCircular.visibility = View.GONE
                        linLeave1.visibility = View.GONE
                        linLeave2.visibility = View.VISIBLE
                        linFuse.visibility = View.GONE
                        linStripBot.visibility = View.VISIBLE
                        linUndercut.visibility = View.GONE
                        linError.visibility = View.GONE
                        linFuseLeavel.visibility = View.GONE
                    } else if (selectSefect == "未熔合") {
                        linStripTop.visibility = View.GONE
                        linConstant.visibility = View.VISIBLE
                        linCircular.visibility = View.GONE
                        linLeave1.visibility = View.GONE
                        linLeave2.visibility = View.VISIBLE
                        linFuse.visibility = View.VISIBLE
                        linStripBot.visibility = View.GONE
                        linUndercut.visibility = View.GONE
                        linError.visibility = View.GONE
                        linFuseLeavel.visibility = View.VISIBLE
                    } else if (selectSefect == "咬边") {
                        linStripTop.visibility = View.GONE
                        linConstant.visibility = View.GONE
                        linCircular.visibility = View.GONE
                        linLeave1.visibility = View.VISIBLE
                        linLeave2.visibility = View.GONE
                        linFuse.visibility = View.GONE
                        linUndercut.visibility = View.VISIBLE
                        linStripBot.visibility = View.GONE
                        linError.visibility = View.GONE
                        linFuseLeavel.visibility = View.GONE
                    } else if (selectSefect == "错边") {
                        linStripTop.visibility = View.GONE
                        linConstant.visibility = View.GONE
                        linCircular.visibility = View.GONE
                        linLeave1.visibility = View.VISIBLE
                        linLeave2.visibility = View.GONE
                        linFuse.visibility = View.GONE
                        linStripBot.visibility = View.GONE
                        linUndercut.visibility = View.GONE
                        linError.visibility = View.VISIBLE
                        linFuseLeavel.visibility = View.GONE
                    }
                    tvLevel1.text = ""
                    tvLevel2.text = ""
                    tvFSSL.text = ""
                    tvCData.text = ""
                    tvTeData.text = ""
                    tvLevel.text = ""
                }
            })

        //管道级别
        BaseAdapter.pipeLeave(mainActivity, spPipeLevel, MyApplication.context.resources.getStringArray(R.array.pipelevel), object : AdapterCallBack {
                override fun backData(data: String) {
                    pipeLeave = data
                    tvLevel1.text = ""
                    tvLevel2.text = ""
                    tvFSSL.text = ""
                    tvCData.text = ""
                    tvTeData.text = ""
                    tvLevel.text = ""
                }
            })
        //未熔合管道级别
        BaseAdapter.pipeLeave(mainActivity, spPipeFuseLevel, MyApplication.context.resources.getStringArray(R.array.pipelevel), object : AdapterCallBack {
            override fun backData(data: String) {
                fusePipeLeave = data
                if(fusePipeLeave == "GC1"){
                    linGC1Length.visibility = View.VISIBLE
                    linGC23Length.visibility = View.GONE
                }else{
                    linGC1Length.visibility = View.GONE
                    linGC23Length.visibility = View.VISIBLE
                }
                tvLevel1.text = ""
                tvLevel2.text = ""
                tvFSSL.text = ""
                tvCData.text = ""
                tvTeData.text = ""
                tvLevel.text = ""
            }
        })
    }

    /**
     * 计算按钮点击事件
     */
    private fun setClient() {
        btnOther.setOnClickListener {
            hideKeyboard()
            if (selectSefect == "圆形缺陷" || selectSefect == "条形缺陷" || selectSefect == "未熔合") {
                if (etLastLand.text.toString().trim { it <= ' ' } == "") {
                    "上次定期检验缺陷附近壁厚实测值或名义壁厚不能为空".showToast(MyApplication.context)
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
                    if (autoSelect == "手动计算") {
                        "预测下一周期年限不能为空".showToast(MyApplication.context)
                        return@setOnClickListener
                    }
                }
                lastLand = etLastLand.text.toString().trim { it <= ' ' }.toDouble()
                minLand = etMinLand.text.toString().trim { it <= ' ' }.toDouble()
                userYear = etUserYear.text.toString().trim { it <= ' ' }.toDouble()
                nextYear = etNextYear.text.toString().trim { it <= ' ' }.toDouble()
                countProject(nextYear)
            }
            if (selectSefect == "咬边") {
                if (etUndercutGC1.text.toString().trim { it <= ' ' } == "" && etUndercutGC2.text.toString().trim { it <= ' ' } == "") {
                    "请输入管道咬边深度".showToast(MyApplication.context)
                    return@setOnClickListener
                } else {

                    if (etUndercutGC1.text.toString().trim { it <= ' ' } != "") {
                        val GC1UndercutData = etUndercutGC1.text.toString()
                        if (GC1UndercutData != "") {
                            if (GC1UndercutData.toDouble() > 0.5) {
                                tvLevel1.text = resources.getString(R.string.influence)
                                tvLevel1.setTextColor(resources.getColor(R.color.red))
                            } else {
                                tvLevel1.text = resources.getString(R.string.no_influence)
                                tvLevel1.setTextColor(resources.getColor(R.color.black))
                            }
                        }
                    }
                    if (etUndercutGC2.text.toString().trim { it <= ' ' } != "") {
                        val GC2UndercutData = etUndercutGC2.text.toString()
                        if (GC2UndercutData != "") {
                            if (GC2UndercutData.toDouble() > 0.8) {
                                tvLevel2.text = resources.getString(R.string.influence)
                                tvLevel2.setTextColor(resources.getColor(R.color.red))
                            } else {
                                tvLevel2.text = resources.getString(R.string.no_influence)
                                tvLevel2.setTextColor(resources.getColor(R.color.black))
                            }
                        }
                    }
                }
            }

            if (selectSefect == "错边") {
                if (etErrorGC1.text.toString()
                        .trim { it <= ' ' } == "" && etErrorGC2.text.toString()
                        .trim { it <= ' ' } == ""
                ) {
                    "管道外壁错边量不能为空".showToast(MyApplication.context)
                    return@setOnClickListener
                } else if (etThickness.text.toString().trim { it <= ' ' } == "") {
                    "公称壁厚不能为空".showToast(MyApplication.context)
                    return@setOnClickListener
                } else {
                    if (etErrorGC1.text.toString().trim { it <= ' ' } != "") {
                        val GC1ErrorData = etErrorGC1.text.toString().toDouble()
                        val ThicknessData = etThickness.text.toString().toDouble()
                        if (GC1ErrorData > 3 && GC1ErrorData >= ThicknessData * 0.2) {
                            tvLevel1.text = resources.getString(R.string.there_leave)
                            tvLevel1.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tvLevel1.text = resources.getString(R.string.two_leave)
                            tvLevel1.setTextColor(resources.getColor(R.color.black))
                        }
                    }

                    if (etErrorGC2.text.toString().trim { it <= ' ' } != "") {
                        val GC2ErrorData = etErrorGC1.text.toString().toDouble()
                        val ThicknessData = etThickness.text.toString().toDouble()
                        if (GC2ErrorData > 5 && GC2ErrorData >= ThicknessData * 0.25) {
                            tvLevel2.text = resources.getString(R.string.there_leave)
                            tvLevel2.setTextColor(resources.getColor(R.color.red))
                        } else {
                            tvLevel2.text = resources.getString(R.string.two_leave)
                            tvLevel2.setTextColor(resources.getColor(R.color.black))
                        }
                    }
                }

            }
        }
    }

    private fun countProject(nextYear: Double) {
        val fssl = (lastLand - minLand) / userYear
        val result = BigDecimal(fssl).setScale(4, BigDecimal.ROUND_HALF_UP)
        tvFSSL.text = result.toString() + ""

        val cData = BigDecimal(fssl * nextYear).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
        tvCData.text = cData.toString() + ""

        val teData = BigDecimal(minLand - cData).setScale(4, BigDecimal.ROUND_HALF_UP).toDouble()
        tvTeData.text = teData.toString() + ""

        if (selectSefect == "圆形缺陷") {
            if (etCircularRate.text.toString().trim { it <= ' ' } == "") {
                "圆形缺陷率不能为空".showToast(MyApplication.context)
                return
            }
            if (etCircularLength.text.toString().trim { it <= ' ' } == "") {
                "圆形缺陷长径不能为空".showToast(MyApplication.context)
                return
            }
            val circularRate = etCircularRate.text.toString().toDouble()
            val circularLength = etCircularLength.text.toString().toDouble()
            val halfTe = 0.5 * teData.toDouble()
            if (circularRate <= 0.05) {
                if (halfTe <= 6) {
                    if (circularLength < halfTe) {
                        tvLevel.text = "2级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvLevel.text = "4级"
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                    }
                } else {
                    if (java.lang.Double.valueOf(circularLength) < 6) {
                        tvLevel.text = "2级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvLevel.text = "4级"
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                    }
                }
            } else {
                tvLevel.text = "4级"
                tvLevel.setTextColor(resources.getColor(R.color.red))
            }
        }

        if (selectSefect == "条形缺陷") {
            if (etStripHeightOrWidth.text.toString().trim { it <= ' ' } == "") {
                "条形缺陷自身高度或宽度的最大值".showToast(MyApplication.context)
                return
            }
            if (etStripLength.text.toString().trim { it <= ' ' } == "") {
                "条形缺陷总长度".showToast(MyApplication.context)
                return
            }
            if (etPipeOD.text.toString().trim { it <= ' ' } == "") {
                "管道外径".showToast(MyApplication.context)
                return
            }
            val stripHeightOrWidth = etStripHeightOrWidth.text.toString().toDouble()
            val stripLength = etStripLength.text.toString().toDouble()
            val pipeOD = etPipeOD.text.toString().toDouble()

            if (pipeLeave == "GC1") {
                val halfTe = 0.3 * teData
                if (stripHeightOrWidth <= halfTe && stripHeightOrWidth <= 5) {
                    if (stripLength <= 0.50 * Math.PI * pipeOD) {
                        tvLevel.text = "2级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else if (stripLength > 0.50 * Math.PI * pipeOD && stripLength <= 1.00 * Math.PI * pipeOD) {
                        tvLevel.text = "3级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvLevel.text = "4级"
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                    }
                } else {
                    tvLevel.text = "4级"
                    tvLevel.setTextColor(resources.getColor(R.color.red))
                }
            } else {
                val halfTe: Double = 0.35 * teData
                if (stripHeightOrWidth <= halfTe && stripHeightOrWidth <= 6) {
                    if (stripLength <= 0.50 * Math.PI * pipeOD) {
                        tvLevel.text = "2级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else if (stripLength > 0.50 * Math.PI * pipeOD && stripLength <= 1.00 * Math.PI * pipeOD) {
                        tvLevel.text = "3级"
                        tvLevel.setTextColor(resources.getColor(R.color.black))
                    } else {
                        tvLevel.text = "4级"
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                    }
                } else {
                    tvLevel.text = "4级"
                    tvLevel.setTextColor(resources.getColor(R.color.red))
                }
            }
        }

        if (selectSefect == "未熔合") {
            var onlyMax = 0.0
            var outLength = 0.0
            if (etFuseOnlyMax.text.toString().trim { it <= ' ' } != "") {
                onlyMax = etFuseOnlyMax.text.toString().toDouble()
            } else {
                "单个焊接接头中未熔合自身高度的最大值不能为空".showToast(MyApplication.context)
                return
            }
            if (etOutLength.text.toString().trim { it <= ' ' } != "") {
                outLength = etOutLength.text.toString().toDouble()
            } else {
                "管道外径不能为空".showToast(MyApplication.context)
                return
            }
            if (fusePipeLeave == "GC1") {
                if (etGC1Length.text.toString().trim { it <= ' ' } == "") {
                    "GC1级管道的单个焊接接头未熔合总长度不能为空".showToast(MyApplication.context)
                    return
                }else{
                    val gc1Lenght = etGC1Length.text.toString().toDouble()
                    if (gc1Lenght>0.5*3.1415*outLength){
                        tvLevel.setText(R.string.four_leave)
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                        return
                    }
                }
            }
            if (fusePipeLeave == "GC2"||fusePipeLeave == "GC3") {
                if (etGC1Length.text.toString().trim { it <= ' ' } == "") {
                    "GC2、GC3级管道的单个焊接接头未熔合长度不能为空".showToast(MyApplication.context)
                    return
                }else{
                    val gc23Lenght = etGC23Length.text.toString().toDouble()
                    if (gc23Lenght<0&&gc23Lenght>3.1415*outLength){
                        tvLevel.setText(R.string.four_leave)
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                        return
                    }
                }
            }
            if (teData < 2.5 && onlyMax != 0.0) {
                tvLevel.setText(R.string.four_leave)
                tvLevel.setTextColor(resources.getColor(R.color.red))
            }
            if (teData >= 2.5 && teData < 4) {
                if (onlyMax <= 0.15 * teData && onlyMax <= 0.5) {
                    tvLevel.setText(R.string.no_influence)
                    tvLevel.setTextColor(resources.getColor(R.color.black))
                } else {
                    tvLevel.setText(R.string.four_leave)
                    tvLevel.setTextColor(resources.getColor(R.color.red))
                }
            }
            if (teData >= 4 && teData < 8) {
                val twoLeaveData = if (0.15 * teData < 1) 0.15 * teData else 1.0
                val threeLeaveData = if (0.20 * teData < 1.5) 0.20 * teData else 1.5
                leaveContrast(onlyMax, twoLeaveData, threeLeaveData)
            }
            if (teData >= 8 && teData < 12) {
                val twoLeaveData = if (0.15 * teData < 1.5) 0.15 * teData else 1.5
                val threeLeaveData = if (0.20 * teData < 2.0) 0.20 * teData else 2.0
                leaveContrast(onlyMax, twoLeaveData, threeLeaveData)
            }
            if (teData >= 12 && teData < 20) {
                val twoLeaveData = if (0.15 * teData < 2.0) 0.15 * teData else 2.0
                val threeLeaveData = if (0.20 * teData < 3.0) 0.20 * teData else 3.0
                leaveContrast(onlyMax, twoLeaveData, threeLeaveData)
            }
            if (teData >= 20) {
                if (onlyMax <= 3.0) {
                    tvLevel.setText(R.string.two_leave)
                    tvLevel.setTextColor(resources.getColor(R.color.theme_color))
                } else {
                    val threeLeaveData = if (0.20 * teData < 5.0) 0.20 * teData else 5.0
                    if (onlyMax <= threeLeaveData) {
                        tvLevel.setText(R.string.there_leave)
                        tvLevel.setTextColor(resources.getColor(R.color.theme_color))
                    } else {
                        tvLevel.setText(R.string.four_leave)
                        tvLevel.setTextColor(resources.getColor(R.color.red))
                    }
                }
            }
        }
    }

    private fun leaveContrast(onlyMax: Double, twoLeaveData: Double, threeLeaveData: Double) {
        if (onlyMax <= twoLeaveData) {
            tvLevel.setText(R.string.two_leave)
            tvLevel.setTextColor(resources.getColor(R.color.black))
        } else if (onlyMax <= threeLeaveData) {
            tvLevel.setText(R.string.there_leave)
            tvLevel.setTextColor(resources.getColor(R.color.black))
        } else if (onlyMax > threeLeaveData) {
            tvLevel.setText(R.string.four_leave)
            tvLevel.setTextColor(resources.getColor(R.color.red))
        }
    }
}