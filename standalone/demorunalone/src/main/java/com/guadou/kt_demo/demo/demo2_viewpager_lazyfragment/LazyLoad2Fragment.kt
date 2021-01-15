package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import androidx.fragment.app.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseLazyLoadingFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingLoadingAdapter



class LazyLoad2Fragment : BaseLazyLoadingFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): LazyLoad2Fragment {
            return LazyLoad2Fragment()
        }
    }

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
