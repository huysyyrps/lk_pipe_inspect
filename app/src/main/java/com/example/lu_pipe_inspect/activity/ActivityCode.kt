package com.example.lu_pipe_inspect.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.lu_pipe_inspect.R
import com.example.lu_pipe_inspect.util.LogUtil
import com.example.lu_pipe_inspect.util.MyApplication
import com.example.lu_pipe_inspect.util.StatusBarUtils
import kotlinx.android.synthetic.main.activity_code.*
import java.io.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*
import kotlin.experimental.and

class ActivityCode : AppCompatActivity() {
    companion object{
        fun actionStart(context: Context) {
            val intent = Intent(context, ActivityCode::class.java)
            context.startActivity(intent)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code)
        StatusBarUtils.setWindowStatusBarColor(this@ActivityCode, R.color.black)
        val deviceId = Settings.System.getString(contentResolver, Settings.Secure.ANDROID_ID)
        val time = Date().time.toString()
        var strRand = ""
        for (i in 0..19) {
            val num = (0..10).shuffled().last()
            strRand+=num.toString()
        }
        //android ID+当前时间+10位随机数
        //android ID+当前时间+10位随机数
        val code1 = deviceId + time + strRand
        LogUtil.e("TAG",code1)
        tvCode.text = code1
        setClient()
    }

    /**
     * 点击事件
     */
    private fun setClient() {
        btnCopy.setOnClickListener {
            val cm = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            // 创建普通字符型ClipData
            // 创建普通字符型ClipData
            val mClipData = ClipData.newPlainText("Label", tvCode.text.toString())
            // 将ClipData内容放到系统剪贴板里。
            // 将ClipData内容放到系统剪贴板里。
            cm.setPrimaryClip(mClipData)
        }

        btnSend.setOnClickListener {
            //md5编码
            val strmd5: String = md5(tvCode.text.toString())
            val buf = StringBuffer(strmd5)
            val code = buf.reverse().toString()

            val writeData = etCode.text.toString()
            var decryptCode: String? = null
            var userDate: String? = null
            try {
                decryptCode = writeData.split(";;".toRegex()).toTypedArray()[0]
                userDate = writeData.split(";;".toRegex()).toTypedArray()[1]
            } catch (e: Exception) {
                Toast.makeText(this, R.string.leavecode_error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (decryptCode == code) {
                val writeDateBuf = StringBuffer(userDate)
                val writeDate = writeDateBuf.reverse().toString()
                //如果sp为空或者文件夹不存在文件就保存
                save(writeDate)
                MainActivity.actionStart(this)
                finish()
            } else {
                Toast.makeText(this, R.string.leavecode_error, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun md5(string: String): String {
        if (TextUtils.isEmpty(string)) {
            return ""
        }
        var md5: MessageDigest? = null
        try {
            md5 = MessageDigest.getInstance("MD5")
            val bytes = md5.digest(string.toByteArray())
            var result: String? = ""
            for (b in bytes) {
                var temp = Integer.toHexString(b.toInt() and(0xff))
                if (temp.length == 1) {
                    temp = "0$temp"
                }
                result += temp
            }
            return result.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return ""
    }

    private fun save(inputText: String) {
        var out: FileOutputStream? = null
        var writer: BufferedWriter? = null
        try {
            out = openFileOutput("data", MODE_PRIVATE)
            writer = BufferedWriter(OutputStreamWriter(out))
            writer.write(inputText)
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            try {
                writer?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}