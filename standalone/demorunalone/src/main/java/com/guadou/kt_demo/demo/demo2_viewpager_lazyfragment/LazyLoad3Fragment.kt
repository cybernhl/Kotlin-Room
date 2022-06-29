package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo2Binding
import com.guadou.lib_baselib.base.fragment.BaseVDBLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingLoadingAdapter

class LazyLoad3Fragment : BaseVDBLoadingFragment<EmptyViewModel, FragmentDemo2Binding>() {

    var isLoaded = false

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
        return Gloading.from(GloadingLoadingAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun startObserve() {
    }

    override fun init() {
        YYLogUtils.w("LazyLoad3Fragment - init")
    }

    private fun initData() {
        YYLogUtils.w("LazyLoad3Fragment - initData")
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 2500)

        isLoaded = true
    }

    override fun onResume() {
        super.onResume()
        YYLogUtils.w("LazyLoad3Fragment - onResume")
        if (!isLoaded) initData()
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
        initData()
    }

}
