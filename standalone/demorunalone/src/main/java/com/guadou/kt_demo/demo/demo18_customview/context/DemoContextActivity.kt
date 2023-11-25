package com.guadou.kt_demo.demo.demo18_customview.context

import android.content.MutableContextWrapper
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import com.guadou.kt_demo.R

import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.log.YYLogUtils
import java.util.*


class DemoContextActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<DemoContextActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_context

    override fun startObserve() {

    }

    override fun init() {


        val flBox1 = findViewById<ViewGroup>(R.id.fl_box1)
        LayoutInflater.from(mActivity).inflate(R.layout.inflate_context_item, flBox1, true)
        val test1Str = resources.getString(R.string.test1)
        YYLogUtils.w("test1Str:$test1Str")  //打印默认值

        // 创建一个自定义的 MutableContextWrapper，在特定的范围内上下文语言属性因为英文
        val wrapper = MutableContextWrapper(baseContext).apply {
            resources.updateConfiguration(Configuration().apply {
                setLocale(Locale.ENGLISH)
            }, resources.displayMetrics)
        }

        val flBox2 = findViewById<ViewGroup>(R.id.fl_box2)
        LayoutInflater.from(wrapper).inflate(R.layout.inflate_context_item, flBox2, true)
        val test2Str = wrapper.resources.getString(R.string.test1)
        YYLogUtils.w("test2Str:$test2Str")  //打印指定限定上下文范围的值
    }

}