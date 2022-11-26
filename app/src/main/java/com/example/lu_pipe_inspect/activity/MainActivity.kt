package com.example.lu_pipe_inspect.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.fragment.PressureFragment
import com.example.lu_pipe_inspect.fragment.ThinningFragment
import com.example.lu_pipe_inspect.fragment.WeldingLineFragment
import com.example.lu_pipe_inspect.util.BaseActivity
import kotlinx.android.synthetic.main.fragment_left.*

class MainActivity : BaseActivity() {
    private val idList = ArrayList<Int>()

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setClient()
//        replaceFragment(PressureFragment())
        idList.apply {
            add(R.id.tvThinning)
            add(R.id.tvPressure)
            add(R.id.tvWeldingLine)
        }
        replaceFragment(ThinningFragment())
    }

    private fun setClient() {
        tvThinning.setOnClickListener {
            replaceFragment(ThinningFragment())
            setColor(tvThinning)
        }
        tvPressure.setOnClickListener {
            replaceFragment(PressureFragment())
            setColor(tvPressure)
        }
        tvWeldingLine.setOnClickListener {
            replaceFragment(WeldingLineFragment())
            setColor(tvWeldingLine)
        }
    }

    private fun setColor(id: TextView) {
        when(id){
            tvThinning->{
                tvThinning.setBackgroundColor(resources.getColor(R.color.theme_color))
                tvPressure.setBackgroundColor(resources.getColor(R.color.back_color))
                tvWeldingLine.setBackgroundColor(resources.getColor(R.color.back_color))
            }
            tvPressure->{
                tvThinning.setBackgroundColor(resources.getColor(R.color.back_color))
                tvPressure.setBackgroundColor(resources.getColor(R.color.theme_color))
                tvWeldingLine.setBackgroundColor(resources.getColor(R.color.back_color))
            }
            tvWeldingLine->{
                tvThinning.setBackgroundColor(resources.getColor(R.color.back_color))
                tvPressure.setBackgroundColor(resources.getColor(R.color.back_color))
                tvWeldingLine.setBackgroundColor(resources.getColor(R.color.theme_color))
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.rightLayout, fragment)
        transaction.commit()
    }
}