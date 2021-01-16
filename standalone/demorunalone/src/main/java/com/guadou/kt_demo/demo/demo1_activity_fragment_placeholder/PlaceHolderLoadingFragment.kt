package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.fragment.BaseVMLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderlAdapter
import dagger.hilt.android.AndroidEntryPoint


/**
 * 占位布局的加载
 */
@AndroidEntryPoint
class PlaceHolderLoadingFragment : BaseVMLoadingFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): PlaceHolderLoadingFragment {
            return PlaceHolderLoadingFragment()
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_loading_normal

    override fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingPlaceHolderlAdapter( R.layout.layout_placeholder1)).wrap(view)
            .withRetry {
                onGoadingRetry()
            }
    }

    override fun startObserve() {

    }

    override fun init() {
        YYLogUtils.e("viewmodel:" + mViewModel.toString())

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