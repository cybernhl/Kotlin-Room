package com.guadou.kt_demo.demo.demo16_record

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo16HomeBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.AndroidEntryPoint

/**
 * 录制
 */
@AndroidEntryPoint
class Demo16RecordActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo16HomeBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo16RecordActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo16_home)
            .addBindingParams(BR.click, clickProxy)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    override fun init() {
        mBinding.tvIcon.text = "\ue6cc"
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun intent() {
            toast("intent")
        }

        fun mediaRecord() {
            toast("mediaRecord")
        }

        fun mediaCodec() {
            toast("mediaCodec")
        }
    }

}