package com.guadou.kt_demo.demo.demo18_customview.viewgroup


import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.dp2px
import com.guadou.lib_baselib.ext.gotoActivity


class ViewGroup3Activity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<ViewGroup3Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_viewgroup3

    override fun startObserve() {

    }

    override fun init() {

        val imgs = listOf("1", "2", "3", "4", "5")

        findViewById<AbstractNineGridLayout>(R.id.nine_grid).run {
            setSingleModeSize(dp2px(200f), dp2px(400f))
            setAdapter(ImageNineGridAdapter(imgs))
        }

    }

}