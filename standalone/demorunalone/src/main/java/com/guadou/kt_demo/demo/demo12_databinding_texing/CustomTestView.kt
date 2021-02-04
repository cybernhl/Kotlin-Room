package com.guadou.kt_demo.demo.demo12_databinding_texing

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import android.widget.TextView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.CommUtils

class CustomTestView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr) {


    init {
        orientation = VERTICAL

        //传统的方式添加
        val view = CommUtils.inflate(R.layout.layout_custom_databinding_test)
        addView(view)

    }

    //设置属性
    fun setTestBean(bean: TestBindingBean?) {

        bean?.let {
            findViewById<TextView>(R.id.tv_custom_test1).text = it.text1
            findViewById<TextView>(R.id.tv_custom_test2).text = it.text2
            findViewById<TextView>(R.id.tv_custom_test3).text = it.text3
        }


    }

    fun setClickProxy(click: Demo12Activity.ClickProxy?) {
        findViewById<TextView>(R.id.tv_custom_test1).click {
            click?.testToast()
        }
    }

}