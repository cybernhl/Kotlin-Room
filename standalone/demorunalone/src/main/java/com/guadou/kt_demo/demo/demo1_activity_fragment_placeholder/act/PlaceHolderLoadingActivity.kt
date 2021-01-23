package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.act

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMLoadingActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderlAdapter

/**
 * 重写生成GLoading的方法就行了
 */
class PlaceHolderLoadingActivity : BaseVMLoadingActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, PlaceHolderLoadingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_loading_normal


    override fun generateGLoading(): Gloading.Holder {
        return Gloading.from(GloadingPlaceHolderlAdapter(R.layout.layout_placeholder1)).wrap(this)
            .withRetry {
                onGoadingRetry()
            }
    }

    override fun startObserve() {

    }

    override fun init() {
        toast("ViewModel: $mViewModel")

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