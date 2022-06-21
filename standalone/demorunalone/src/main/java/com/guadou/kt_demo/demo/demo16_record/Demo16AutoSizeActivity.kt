package com.guadou.kt_demo.demo.demo16_record

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo16AutosizeBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import dagger.hilt.android.AndroidEntryPoint

/**
 * 录制
 */
@AndroidEntryPoint
class Demo16AutoSizeActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo16AutosizeBinding>() {


    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo16AutoSizeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo16_autosize)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    override fun init() {
        mBinding.tvIcon.text = "\ue6cc"
    }

}