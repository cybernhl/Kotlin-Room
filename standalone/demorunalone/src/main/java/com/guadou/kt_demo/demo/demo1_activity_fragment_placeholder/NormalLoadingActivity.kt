package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMLoadingActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import com.guadou.lib_baselib.view.gloading.GloadingLoadingAdapter

/**
 * 换成一种菊花转动的Loading加载
 */
class NormalLoadingActivity : BaseVMLoadingActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, NormalLoadingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_loading_normal

    //重新生成GLoading对象
    override fun generateGLoading(): Gloading.Holder {

        return Gloading.from(GloadingLoadingAdapter()).wrap(this).withRetry {
            onGoadingRetry()
        }
    }

    override fun startObserve() {

    }

    override fun init() {
        toast("ViewModel: $mViewModel")

        //其他的使用的方法和默认的GLoading很类似
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