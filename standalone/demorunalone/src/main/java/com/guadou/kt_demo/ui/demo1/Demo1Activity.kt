package com.guadou.kt_demo.ui.demo1

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import kotlinx.android.synthetic.main.activity_demo_1.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class Demo1Activity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo1Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo_1

    override fun startObserve() {
    }

    override fun init() {

        btn_demo_1.click {
            NormalJumpLoadingActivity.startInstance()
        }

        btn_demo_2.click {
            NormalLoadingActivity.startInstance()
        }

        btn_demo_3.click {
            PlaceHolderLoadingActivity.startInstance()
        }
    }
}