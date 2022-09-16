package com.guadou.kt_demo.demo.demo7_imageload_glide

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.ActivityDemo7Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext

/**
 * 加载各种图片Glide
 *
 */
class Demo7Activity : BaseVDBActivity<Demo7ViewModel, ActivityDemo7Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo7Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo7, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {

        mViewModel.img1LiveData.value = "http://i01piccdn.sogoucdn.com/7f62e43d48cb78d9"

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun setRoundImage() {
            RoundImageActivity.startInstance()
        }

        fun setRoundLayout() {
            RoundLayoutActivity.startInstance()
        }

    }
}