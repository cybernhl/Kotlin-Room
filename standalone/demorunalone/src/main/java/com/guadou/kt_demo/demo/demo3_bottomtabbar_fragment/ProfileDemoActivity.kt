package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.content.Intent
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo3LoginBinding
import com.guadou.kt_demo.databinding.ActivityDemo3ProfileBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils

/**
 * 个人信息页面
 */
class ProfileDemoActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo3ProfileBinding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, ProfileDemoActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo3_profile)
    }

    override fun startObserve() {

    }

    override fun init() {

    }

}