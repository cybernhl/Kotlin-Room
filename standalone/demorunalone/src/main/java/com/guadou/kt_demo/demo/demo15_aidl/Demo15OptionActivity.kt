package com.guadou.kt_demo.demo.demo15_aidl

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo15MainBinding
import com.guadou.kt_demo.demo.demo15_aidl.service1.Service1Activity
import com.guadou.kt_demo.demo.demo15_aidl.service2.Service2Activity
import com.guadou.kt_demo.demo.demo15_aidl.service3.Service3Activity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import dagger.hilt.android.AndroidEntryPoint

/**
 * AIDL选择
 */
@AndroidEntryPoint
class Demo15OptionActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo15MainBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo15OptionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo15_main)
            .addBindingParams(BR.click, clickProxy)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    override fun init() {

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun service1() {
            Service1Activity.startInstance()
        }

        fun service2() {
            Service2Activity.startInstance()
        }

        fun service3() {
            Service3Activity.startInstance()
        }
    }

}