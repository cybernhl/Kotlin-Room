package com.guadou.kt_demo.ui.demo2

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.ui.demo1.NormalLoadingFragment
import com.guadou.lib_baselib.base.BaseLazyLoadingFragment
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import com.guadou.lib_baselib.view.gloading.GloadingLoadingAdapter
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter

import org.koin.androidx.viewmodel.ext.android.getViewModel


class LazyLoad2Fragment : BaseLazyLoadingFragment<BaseViewModel>() {

    companion object {
        fun obtainFragment(): LazyLoad2Fragment {
            return LazyLoad2Fragment()
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.fragment_demo2

    override fun startObserve() {

    }

    override fun init() {

    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingLoadingAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
        onLazyInitData()
    }

    override fun onLazyInitData() {
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 2500)

    }

}