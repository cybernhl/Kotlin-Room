package com.guadou.kt_demo.ui.demo1

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BasePlaceHolderActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import org.koin.androidx.viewmodel.ext.android.getViewModel

class PlaceHolderLoadingActivity : BasePlaceHolderActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, PlaceHolderLoadingActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()


    override fun inflateLayoutById(): Int = R.layout.activity_loading_normal

    //可以选择重写占位布局的id-内部实现了闪光的效果
    override fun inflatePlaceHolderLayoutRes(): Int = R.layout.layout_placeholder1


    override fun startObserve() {

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