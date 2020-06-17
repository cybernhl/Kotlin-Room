package com.guadou.kt_zoom.ui

import android.view.View
import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.lib_baselib.base.BaseLazyLoadingFragment
import com.guadou.lib_baselib.ext.toast

import kotlinx.android.synthetic.main.fragment_demo_notitle.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class Demo1Fragment : BaseLazyLoadingFragment<MainViewModel>() {

    override fun inflateLayoutById(): Int = R.layout.fragment_demo_notitle

    override fun startObserve() {

    }

    override fun initVM(): MainViewModel = getViewModel()

    override fun init() {
        tv_fragment_content.setText("12345")
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
    }

    override fun onLazyInitData() {
        mViewModel.getIndustry()
    }

}
