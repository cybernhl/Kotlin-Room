package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo2Binding
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2.DemoLazy2Activity
import com.guadou.lib_baselib.base.fragment.BaseVDBLazyLoadingFragment
import com.guadou.lib_baselib.base.fragment.BaseVMLazyLoadingFragment
import com.guadou.lib_baselib.base.fragment.BaseVMLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalAdapter
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter


class Lazy2Fragment3 : BaseVMLoadingFragment<EmptyViewModel>() {

    var isInitDataLoaded = false

    companion object {
        fun obtainFragment(): Lazy2Fragment3 {
            return Lazy2Fragment3()
        }
    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingGlobalAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.lazy2_fragment3


    override fun startObserve() {

    }

    override fun init() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Lazy2Fragment3 - onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        YYLogUtils.w("Lazy2Fragment3 - onViewCreated")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Lazy2Fragment3 - onResume")

        if (!isInitDataLoaded) {
            onLazyInitData()
        }
    }

    override fun onGoadingRetry() {
        onLazyInitData()
    }

    private fun onLazyInitData() {
        YYLogUtils.w("Lazy2Fragment3 - initData")
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()
            isInitDataLoaded = true

        }, 1000)

    }


}
