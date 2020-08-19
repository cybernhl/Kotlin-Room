package com.guadou.kt_demo.ui.demo1

import android.content.Intent
import androidx.activity.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo_1.*

@AndroidEntryPoint
class Demo1Activity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo1Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): EmptyViewModel {
        val viewModel: EmptyViewModel by viewModels()
        return viewModel
    }

    override fun inflateLayoutById(): Int = R.layout.activity_demo_1

    override fun startObserve() {
    }

    override fun init() {
        YYLogUtils.e("viewmodel:" + mViewModel.toString())

        btn_demo_1.click {
            JumpLoadingActivity.startInstance()
        }

        btn_demo_2.click {
            NormalLoadingActivity.startInstance()
        }

        btn_demo_3.click {
            PlaceHolderLoadingActivity.startInstance()
        }

        btn_demo_4.click {
            EmptyFragmentActivity.startInstance(1)
        }

        btn_demo_5.click {
            EmptyFragmentActivity.startInstance(2)
        }

        btn_demo_6.click {
            EmptyFragmentActivity.startInstance(3)
        }
    }
}