package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast

/**
 * Fragment导航
 */
class Demo11Activity : BaseVMActivity<Demo11ViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo11Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_demo_11

    override fun startObserve() {
    }

    override fun init() {
        toast(mViewModel.testToast())
    }

}