package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import android.os.Bundle
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils

class Demo13Cons01Activity : BaseVMActivity<EmptyViewModel>() {

    private var start = 0L
    private var end = 0L
    private var toggle = true

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13Cons01Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo13_cons01
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        start = System.currentTimeMillis()

        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        end = System.currentTimeMillis()

        YYLogUtils.w("耗时：" + (end - start))
    }

    override fun startObserve() {
    }

    override fun init() {


    }

}