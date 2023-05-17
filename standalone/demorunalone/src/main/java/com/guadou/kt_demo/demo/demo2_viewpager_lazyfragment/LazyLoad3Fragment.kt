package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo2Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBLazyLoadingFragment
import com.guadou.lib_baselib.base.fragment.BaseVDBLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalAdapter
import com.guadou.lib_baselib.view.gloading.GloadingLoadingAdapter

class LazyLoad3Fragment : BaseVDBLazyLoadingFragment<EmptyViewModel, FragmentDemo2Binding>() {

    companion object {
        fun obtainFragment(): LazyLoad3Fragment {
            return LazyLoad3Fragment()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo2)
    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingGlobalAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun startObserve() {

    }

    override fun init() {
        YYLogUtils.w("LazyLoad2Fragment - init")
    }

    override fun onResume() {
        super.onResume()

        YYLogUtils.w("LazyLoad3Fragment - onResume")
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
        onLazyInitData()
    }

    override fun onLazyInitData() {
        YYLogUtils.w("LazyLoad3Fragment - initData")
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 1000)

    }

}
