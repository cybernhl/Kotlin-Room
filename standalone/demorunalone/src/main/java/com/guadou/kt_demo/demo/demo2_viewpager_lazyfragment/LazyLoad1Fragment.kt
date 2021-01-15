package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.view.View
import androidx.fragment.app.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseLazyLoadingFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter



/**
 * 滑动的时候最好是把真正的布局先隐藏，然后在加载完成之后再显示出来
 * 不然会有一点延时之后再加载的感觉
 */
class LazyLoad1Fragment : BaseLazyLoadingFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): LazyLoad1Fragment {
            return LazyLoad1Fragment()
        }
    }

    override fun inflateLayoutById(): Int = R.layout.fragment_demo2

    override fun startObserve() {

    }

    override fun init() {

    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingRoatingAdapter()).wrap(view).withRetry {
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
