package com.guadou.kt_demo.demo.demo8_recyclerview.rv5

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.vertical
import kotlinx.android.synthetic.main.activity_demo_rv_normal.*

/**
 * 多布局
 */
class DemoRVMuliteActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVMuliteActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_demo_rv_normal

    override fun startObserve() {

    }

    override fun init() {

        val datas = mutableListOf(
            MuliteTestBean("1", "1.1", 0),
            MuliteTestBean("2", "1.2", 1),
            MuliteTestBean("3", "1.3", 0),
            MuliteTestBean("4", "1.4", 0),
            MuliteTestBean("5", "1.5", 1),
            MuliteTestBean("6", "1.6", 0),
            MuliteTestBean("7", "1.7", 0),
            MuliteTestBean("8", "1.8", 1),
            MuliteTestBean("9", "1.9", 0),
            MuliteTestBean("10", "1.1", 0),
            MuliteTestBean("11", "1.2", 1),
            MuliteTestBean("12", "1.3", 0)
        )

        recyclerView.vertical().adapter = DemoMuliteAdapter(datas)

    }
}