package com.guadou.kt_demo.demo.demo14_mvi.mvvm2

import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo142Binding
import com.guadou.kt_demo.demo.demo14_mvi.mvvm1.DemoViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MVVM2Activity : BaseVDBActivity<DemoViewModel, ActivityDemo142Binding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo14_2, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, clickProxy)
    }

    override fun init() {
    }

    override fun startObserve() {
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun getData() {
            //MVVM直接调用网络请求，结果在xml中自动显示
            mViewModel.requestIndustry()
        }
    }

}