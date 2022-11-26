package com.example.lu_pipe_inspect.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.activity.MainActivity
import com.example.lu_pipe_inspect.util.*
import com.example.lu_pipe_inspect.util.KeyboardHide.hideKeyboard
import kotlinx.android.synthetic.main.fragment_left.*
import kotlinx.android.synthetic.main.fragment_thinning.*
import java.math.BigDecimal
import kotlin.math.ln
import kotlin.math.sqrt

class ThinningFragment : Fragment() {
    private lateinit var mainActivity: Activity
    private lateinit var pipeLeave: String
    private lateinit var pipeMaterial: String
    private lateinit var autoSelect: String
    private var paramData: Double = 0.0
    private var strength: Double = 0.0
    private lateinit var maxDefectLength: String
    private lateinit var maxOutLength: String
    private lateinit var lastLand: String
    private lateinit var minLand: String
    private lateinit var userYear: String
    private lateinit var nextYear: String
    private lateinit var maxMpa: String
    private var cData: Double = 0.0
    private var teData: Double = 0.0
    private var pl0Data: Double = 0.0
    private var leave: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_thinning, container, false)
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
        //管道级别
        BaseAdapter.pipeLeave(
            mainActivity,
            spPipeLevel,
            MyApplication.context.resources.getStringArray(R.array.pipelevel),
            object : AdapterCallBack {
                override fun backData(data: String) {
                    pipeLeave = data
                    tvCData.text = ""
                    tvTeData.text = ""
                    tvPl0Data.text = ""
                    tvLevel.text = ""
                    tvNextYearAuto.text = ""
                }
            })
        //管道材料
        BaseAdapter.pipeLeave(
            mainActivity,
            spPipeMaterial,
            MyApplication.context.resources.getStringArray(R.array.pipematerial),
            object : AdapterCallBack {
                @SuppressLint("SetTextI18n")
                override fun backData(data: String) {
                    pipeMaterial = data
                    if (pipeMaterial == "20#钢") {
                        strength = 245.0
                    } else if (pipeMaterial == "奥氏体不锈钢") {
                        strength = 310.0
                    } else if (pipeMaterial == "16MnR") {
                        strength = 320.0
                    }
                    tvCData.text = ""
                    tvTeData.text = ""
                    tvPl0Data.text = ""
                    tvLevel.text = ""
                    tvNextYearAuto.text = ""
                    etStrength.setText(strength.toString() + "")
                }
            })
        //计算选择
        BaseAdapter.pipeLeave(
            mainActivity,
            spSelectCount,
            MyApplication.context.resources.getStringArray(R.array.select_count),
            object : AdapterCallBack {
                @SuppressLint("SetTextI18n")
                override fun backData(data: String) {
                    autoSelect = data
                    if (autoSelect == "自动计算") {
                        linNextYear.setVisibility(GONE)
                        linNextYearAuto.setVisibility(View.VISIBLE)
                    } else if (autoSelect == "手动计算") {
                        linNextYear.setVisibility(VISIBLE)
                        linNextYearAuto.setVisibility(GONE)
                    }
                    tvCData.text = ""
                    tvTeData.text = ""
                    tvPl0Data.text = ""
                    tvLevel.text = ""
                    tvNextYearAuto.text = ""
                    etStrength.setText(strength.toString() + "")
                }
            })
    }

    /**
     * 计算按钮点击事件
     */
    @SuppressLint("SetTextI18n")
    private fun setClient() {
        btnThinning.setOnClickListener {
            hideKeyboard()
            if (etStrength.text.toString().trim { it <= ' ' } == "") {
                "屈服强度不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etMaxDefectLength.text.toString().trim { it <= ' ' } == "") {
                "缺陷环向长度实测最大值不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etMaxOutLength.text.toString().trim { it <= ' ' } == "") {
                "缺陷附近管道外径实测最大值不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etLastLand.text.toString().trim { it <= ' ' } == "") {
                "名义壁厚不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etMinLand.text.toString().trim { it <= ' ' } == "") {
                "本次定期检验缺陷附近壁厚实测最小值不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etUserYear.text.toString().trim { it <= ' ' } == "") {
                "两次定期检验间隔年限或首次定检年限不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else if (etMaxMPa.text.toString().trim { it <= ' ' } == "") {
                "管道最大工作压力不能为空".showToast(MyApplication.context)
                return@setOnClickListener
            } else {
                maxDefectLength = etMaxDefectLength.text.toString().trim { it <= ' ' }
                maxOutLength = etMaxOutLength.text.toString().trim { it <= ' ' }
                lastLand = etLastLand.text.toString().trim { it <= ' ' }
                minLand = etMinLand.text.toString().trim { it <= ' ' }
                userYear = etUserYear.text.toString().trim { it <= ' ' }
                maxMpa = etMaxMPa.text.toString().trim { it <= ' ' }
                if (autoSelect == "手动计算") {
                    if (etNextYear.text.toString().trim { it <= ' ' } == "") {
                        "预测下一周期年限不能为空".showToast(MyApplication.context)
                        return@setOnClickListener
                    }
                    nextYear = etNextYear.text.toString().trim { it <= ' ' }
                    if (nextYear.toDouble() > 9 || nextYear.toDouble() < 0) {
                        resources.getString(R.string.true_data).showToast(MyApplication.context)
                        return@setOnClickListener
                    }
                    cData = countCData(nextYear.toInt())
                    teData = counTeData(nextYear.toInt())
                    pl0Data = counPL0Data(nextYear.toInt())
                    getLeaveTable()
                }

                if (autoSelect == "自动计算") {
                    var leaveOne = 0
                    var leaveOther = 0
                    for (i in 1 until 10) {
                        if (i == 1) {
                            cData = countCData(1)
                            teData = counTeData(1)
                            pl0Data = counPL0Data(1)
                            leaveOne = getLeaveTable()
                            if (leaveOne == 4) {
                                tvTeData.text = teData.toString()
                                tvCData.text = cData.toString()
                                tvPl0Data.text = pl0Data.toString()
                                tvLevel.text = "${leaveOne}级"
                                tvLevel.setTextColor(resources.getColor(R.color.red))
                                return@setOnClickListener
                            }
                        }
                        cData = countCData(i)
                        teData = counTeData(i)
                        pl0Data = counPL0Data(i)
                        leaveOther = getLeaveTable()
                        if (leaveOther > leaveOne) {
                            cData = countCData(i-1)
                            teData = counTeData(i-1)
                            pl0Data = counPL0Data(i-1)
                            tvTeData.text = teData.toString()
                            tvCData.text = cData.toString()
                            tvPl0Data.text = pl0Data.toString()
                            tvLevel.text = "${leaveOne}级"
                            tvNextYearAuto.text = "${i - 1}年"
                            tvLevel.setTextColor(resources.getColor(R.color.black))
                            return@setOnClickListener
                        }
                    }
                }
            }
        }
    }

    /**
     * 计算C值
     * 至下一检验周期局部减薄深度扩展量的估计值
     */
    private fun countCData(i: Int): Double {
        cData =
            BigDecimal((lastLand.toDouble() - minLand.toDouble()) / userYear.toDouble() * i).setScale(
                4,
                BigDecimal.ROUND_HALF_UP
            ).toDouble()
        return cData
    }

    /**
     * 计算Te
     * 有效厚度，缺陷附近壁厚实测最小值减去至下一检验周期的腐蚀量
     */
    private fun counTeData(i: Int): Double {
        teData =
            BigDecimal(minLand.toDouble() - cData.toDouble()).setScale(4, BigDecimal.ROUND_HALF_UP)
                .toDouble()
        return teData
    }

    /**
     * 计算Pl
     * p-管道最大工作压力，MPa
     */
    private fun counPL0Data(i: Int): Double {
        val halfD = maxOutLength.toDouble() / 2
        pl0Data =
            BigDecimal(2 / sqrt(3.0) * strength.toDouble() * ln(halfD / (halfD - teData.toDouble()))).setScale(
                4,
                BigDecimal.ROUND_HALF_UP
            ).toDouble()
        return pl0Data
    }

    /**
     * 计算等级
     */
    private fun getLeaveTable(): Int {
        var leftOther = maxDefectLength.toDouble() / maxOutLength.toDouble() / 3.141592
        var leaveTable = 0
        if (pipeLeave == "GC2" || pipeLeave == "GC3") {
            if (leftOther <= 0.25) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.33, 0.4)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.2, 0.25)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            } else if (leftOther > 0.25 && leftOther <= 0.75) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.25, 0.33)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.15, 0.2)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            } else if (leftOther > 0.75 && leftOther <= 1.00) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.2, 0.25)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.15, 0.2)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            }
        }
        if (pipeLeave == "GC1") {
            if (leftOther <= 0.25) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.3, 0.35)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.15, 0.2)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            } else if (leftOther > 0.25 && leftOther <= 0.75) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.2, 0.3)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.1, 0.15)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            } else if (leftOther > 0.75 && leftOther <= 1.00) {
                if (maxMpa.toDouble() < 0.3 * pl0Data) {
                    leaveTable = getLeave(0.15, 0.2)
                } else if (maxMpa.toDouble() > 0.3 * pl0Data && maxMpa.toDouble() <= 0.5 * pl0Data) {
                    leaveTable = getLeave(0.1, 0.15)
                } else {
                    leaveTable = -1
                    resources.getString(R.string.parameter_error)
                        .showToast(MyApplication.context)
                }
            }
        }
        return leaveTable
    }

    /**
     * 计算等级
     */
    private fun getLeave(leave1: Double, leave2: Double): Int {
        if (leave1 * teData - cData > 0) {
            leave = 2
            tvLevel.setTextColor(resources.getColor(R.color.black))
        } else if (leave1 * teData - cData < 0 && leave2 * teData - cData > 0) {
            leave = 3
            tvLevel.setTextColor(resources.getColor(R.color.black))
        } else if (leave2 * teData - cData < 0) {
            leave = 4
            tvLevel.setTextColor(resources.getColor(R.color.red))
        }
        if (autoSelect == "手动计算") {
            tvTeData.text = teData.toString()
            tvCData.text = cData.toString()
            tvPl0Data.text = pl0Data.toString()
            tvLevel.text = "${leave}级"
        }
        return leave
    }

}
