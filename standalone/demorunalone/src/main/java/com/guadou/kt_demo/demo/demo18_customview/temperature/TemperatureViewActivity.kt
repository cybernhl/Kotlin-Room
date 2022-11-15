package com.guadou.kt_demo.demo.demo18_customview.temperature


import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity


class TemperatureViewActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<TemperatureViewActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_temperature

    override fun startObserve() {

    }

    override fun init() {
        findViewById<View>(R.id.set_progress).click {

           val temperatureView = findViewById<TemperatureView>(R.id.temperature_view)
            temperatureView .setupTemperature(70f)
        }
    }


}