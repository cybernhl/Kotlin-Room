package com.guadou.kt_demo.ui.demo1

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseLoadingFragment
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import com.guadou.lib_baselib.view.gloading.GloadingRoatingAdapter
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 设置为菊花的转动
 */
class NormalLoadingFragment : BaseLoadingFragment<BaseViewModel>() {

    companion object {
        fun obtainFragment(): NormalLoadingFragment {
            return NormalLoadingFragment()
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_loading_normal

    override fun startObserve() {

    }

    //重新生成GLoading对象
    override fun generateGLoading(view: View): Gloading.Holder {
        return  Gloading.from(GloadingRoatingAdapter()).wrap(view).withRetry {
            onGoadingRetry()
        }
    }


    override fun init() {

        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 2500)

    }

    //可选的实现
    //如果要设置空出Title的位置，需要重写这个方法设置Data
    override fun showStateLoading() {
        mGLoadingHolder.withData(GloadingGlobalStatusView.NEED_LOADING_STATUS_MAGRIN_TITLE)
        mGLoadingHolder.showLoading()
    }


}