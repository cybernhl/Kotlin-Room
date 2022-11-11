package com.guadou.kt_demo.demo.demo18_customview.circleView

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity


class DemoCircleViewActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<DemoCircleViewActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_circle_view

    override fun startObserve() {

    }

    override fun init() {

        findViewById<Button>(R.id.btn_back).click {
            setResult(-1, Intent().putExtra("text", "测试返回的数据"))
            finish()
        }

        //设置进度
        findViewById<Button>(R.id.set_progress).click {

            findViewById<MyCircleProgressView>(R.id.progress_view).setValue("60", 100f)

        }


    }


}