package com.guadou.kt_demo.demo.demo14_mvi.mvvm1

import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MVVMActivity : BaseVMActivity<DemoViewModel>() {

    override fun getLayoutIdRes(): Int = R.layout.activity_demo14_1

    override fun init() {
        //自动注入ViewModel，调用接口通过LiveData回调
        mViewModel.requestIndustry()
    }

    override fun startObserve() {
        //获取到网络数据之后改变xml对应的值
        mViewModel.liveData.observe(this) {
            it?.let {
                // popopIndustryData
            }
        }
    }
}