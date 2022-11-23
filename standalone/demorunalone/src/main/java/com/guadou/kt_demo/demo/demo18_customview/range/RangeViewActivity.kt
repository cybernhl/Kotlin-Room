package com.guadou.kt_demo.demo.demo18_customview.range


import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast


class RangeViewActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RangeViewActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_range_view

    override fun startObserve() {

    }

    override fun init() {

        findViewById<RangeView>(R.id.range_view).setupData(0, 100, 1) { leftValue, rightValue ->

            toast("leftValue：$leftValue rightValue:$rightValue")
        }

    }


}