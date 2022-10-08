package com.guadou.kt_demo.demo.demo17_softinput

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.ActivityDemo17SoftinputBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.AndroidEntryPoint


/**
 * 软键盘相关
 */
@AndroidEntryPoint
class Demo17SoftInputActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo17SoftinputBinding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo17SoftInputActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo17_softinput)
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

        fun testLayout() {

            gotoActivity<SoftInputLayoutActivity>()
        }

        fun testScrollView() {

            gotoActivity<SoftInputScrollActivity>()
        }

        fun testRV() {

            gotoActivity<SoftInputListActivity>()
        }

        fun testMoment() {

            gotoActivity<SoftInputMomentActivity>()
        }


    }

}