package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo13MotionlayoutBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext

class Demo13MotionActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo13MotionlayoutBinding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13MotionActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo13_motionlayout)
            .addBindingParams(BR.click, clickProxy)
    }

    override fun startObserve() {
    }

    override fun init() {

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun gotoJavaContrcol() {
            Demo13JavaActivity.startInstance()
        }

        fun gotoXmlContrcol() {
            Demo13XmlActivity.startInstance()
        }

    }

}