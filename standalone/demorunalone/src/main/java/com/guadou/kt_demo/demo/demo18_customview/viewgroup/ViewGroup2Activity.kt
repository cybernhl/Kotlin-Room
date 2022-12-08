package com.guadou.kt_demo.demo.demo18_customview.viewgroup


import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity


class ViewGroup2Activity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<ViewGroup2Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_viewgroup2

    override fun startObserve() {

    }

    override fun init() {

    }

}