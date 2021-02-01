package com.guadou.kt_demo.demo.demo12_databinding_texing

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo11Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext

/**
 * 测试DataBinding的高级特效
 */
class Demo12Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo11Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo12Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo12)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun putACache() {

        }

    }
}