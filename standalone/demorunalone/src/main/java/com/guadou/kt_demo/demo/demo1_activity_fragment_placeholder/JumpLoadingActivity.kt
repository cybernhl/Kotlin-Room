package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder

import android.content.Intent
import androidx.activity.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseLoadingActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import dagger.hilt.android.AndroidEntryPoint


/**
 * 默认是加载的上下跳动的动画
 */
class JumpLoadingActivity : BaseLoadingActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, JumpLoadingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_loading_normal


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