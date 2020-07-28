package com.guadou.kt_demo.ui.demo10

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.commContext
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Span的展示的逻辑
 */
class DemoSpanActivity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoSpanActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo_span

    override fun startObserve() {

    }

    override fun init() {

    }
}