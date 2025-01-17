package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo21Binding
import com.guadou.kt_demo.databinding.FragmentDemo2Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBLazyLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalAdapter


class LazyLoad2Fragment : BaseVDBLazyLoadingFragment<EmptyViewModel, FragmentDemo21Binding>() {

    companion object {
        fun obtainFragment(): LazyLoad2Fragment {
            return LazyLoad2Fragment()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo2_1)
    }

    override fun startObserve() {

    }

    override fun init() {
        YYLogUtils.w("LazyLoad2Fragment - init")

        mBinding.tvPage2.click {
            Demo2PagerCustomActivity.startInstance()
        }
    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingGlobalAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun onResume() {
        super.onResume()

        YYLogUtils.w("LazyLoad2Fragment - onResume")
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
        onLazyInitData()
    }

    override fun onLazyInitData() {
        YYLogUtils.w("LazyLoad2Fragment - initData")
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 1000)

    }

}
