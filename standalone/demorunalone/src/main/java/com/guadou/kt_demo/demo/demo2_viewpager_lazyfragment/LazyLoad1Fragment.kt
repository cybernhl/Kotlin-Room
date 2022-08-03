package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo2Binding
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2.DemoLazy2Activity
import com.guadou.lib_baselib.base.fragment.BaseVDBLazyLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter


/**
 * 滑动的时候最好是把真正的布局先隐藏，然后在加载完成之后再显示出来
 * 不然会有一点延时之后再加载的感觉
 */
class LazyLoad1Fragment : BaseVDBLazyLoadingFragment<EmptyViewModel, FragmentDemo2Binding>() {

    companion object {
        fun obtainFragment(): LazyLoad1Fragment {
            return LazyLoad1Fragment()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo2)
    }

    override fun startObserve() {

    }

    override fun init() {

        YYLogUtils.w("LazyLoad1Fragment - init")

        mBinding.tvPage2.click {
//            Demo2Pager2Activity.startInstance()

            DemoLazy2Activity.startInstance()
        }
    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingRoatingAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }

    override fun onResume() {
        super.onResume()

        YYLogUtils.w("LazyLoad1Fragment - onResume")
    }

    override fun onGoadingRetry() {
        toast("重试一个请求")
        onLazyInitData()
    }

    override fun onLazyInitData() {
        YYLogUtils.w("LazyLoad1Fragment - initData")
        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 2500)

    }

}
