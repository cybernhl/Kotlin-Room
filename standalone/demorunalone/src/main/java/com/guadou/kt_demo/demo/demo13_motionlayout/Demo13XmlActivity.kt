package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext

class Demo13XmlActivity : BaseVMActivity<EmptyViewModel>() {

    private var toggle = true

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13XmlActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo13_xml
    }

    override fun startObserve() {
    }

    override fun init() {


    }

}