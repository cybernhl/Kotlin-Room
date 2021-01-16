package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.fragment.BaseLazyLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils



class LazyLoad3Fragment : BaseLazyLoadingFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): LazyLoad3Fragment {
            return LazyLoad3Fragment()
        }
    }

    override fun inflateLayoutById(): Int = R.layout.fragment_demo2

    override fun startObserve() {
    }

    override fun init() {
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
