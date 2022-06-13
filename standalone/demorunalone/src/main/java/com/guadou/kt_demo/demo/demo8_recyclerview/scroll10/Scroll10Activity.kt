package com.guadou.kt_demo.demo.demo8_recyclerview.scroll10

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext


class Scroll10Activity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Scroll10Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_scroll10

    override fun init() {

    }

    override fun startObserve() {

    }
}