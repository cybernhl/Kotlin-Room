package com.guadou.kt_zoom.ui

import android.view.View
import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.lib_baselib.base.BaseLazyLoadingFragment
import com.guadou.lib_baselib.base.BaseLoadingFragment
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.view.gloading.GloadingGlobalAdapter
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import org.koin.androidx.viewmodel.ext.android.getViewModel


class Demo2Fragment : BaseLazyLoadingFragment<MainViewModel>() {


    override fun inflateLayoutById(): Int = R.layout.fragment_demo_notitle

    override fun startObserve() {

    }

    override fun initVM(): MainViewModel = getViewModel()

    override fun init() {

    }

    override fun initViews(view: View) {
//        view.findViewById<TextView>(R.id.tv_fragment_content).setText("1234")
//        tv_fragment_content.setText("12345")
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
    }

    override fun onLazyInitData() {
        mViewModel.getIndustry()
    }

}
