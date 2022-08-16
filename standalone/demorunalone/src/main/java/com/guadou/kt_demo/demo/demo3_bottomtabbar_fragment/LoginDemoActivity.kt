package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.content.Intent
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo3LoginBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * 登录页面
 */
class LoginDemoActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo3LoginBinding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, LoginDemoActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo3_login)
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

        fun doLogin() {
            showStateLoading()

            CommUtils.getHandler().postDelayed({
                showStateSuccess()
                SP().putString(Constants.KEY_TOKEN, "abc")
                LiveEventBus.get("login").post(true)
                finish()
            }, 500)

        }

    }
}