package com.guadou.lib_baselib.base

import android.os.Bundle
import androidx.lifecycle.Observer
import com.guadou.basiclib.R
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderlAdapter


abstract class BasePlaceHolderActivity<VM : BaseViewModel> : AbsActivity() {

    protected lateinit var mViewModel: VM

    protected val mGloadingHolder by lazy {
        Gloading.from(GloadingPlaceHolderlAdapter(inflatePlaceHolderLayoutRes())).wrap(this)
            .withRetry {
                onGoadingRetry()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = initVM()

        //观察网络数据状态
        mViewModel.getActionLiveData().observe(this, stateObserver)
        init()
        startObserve()
    }

    abstract fun initVM(): VM
    abstract fun startObserve()
    abstract fun init()
    protected open fun onGoadingRetry() {
    }

    protected open fun inflatePlaceHolderLayoutRes(): Int = R.layout.layout_placeholder2


    override fun onNetworkConnectionChanged(
        isConnected: Boolean,
        networkType: NetWorkUtil.NetworkType?
    ) {
    }

    // ================== 网络状态的监听 ======================

    private var stateObserver: Observer<LoadAction> = Observer { loadAction ->
        if (loadAction != null) {

            when {
                LoadAction.STATE_NORMAL == loadAction.action -> showStateNormal()
                LoadAction.STATE_ERROR == loadAction.action -> showStateError(loadAction.message)
                LoadAction.STATE_SUCCESS == loadAction.action -> showStateSuccess()
                LoadAction.STATE_LOADING == loadAction.action -> showStateLoading()
                LoadAction.STATE_NO_DATA == loadAction.action -> showStateNoData()
                LoadAction.STATE_PROGRESS == loadAction.action -> showStateProgress()
                LoadAction.STATE_HIDE_PROGRESS == loadAction.action -> hideStateProgress()
            }

        }
    }

    protected open fun showStateNormal() {}

    protected open fun showStateLoading() {
        mGloadingHolder.showLoading()
    }

    protected open fun showStateSuccess() {
        mGloadingHolder.showLoadSuccess()
    }

    protected open fun showStateError(message: String?) {
        mGloadingHolder.showLoadFailed(message)
    }

    protected open fun showStateNoData() {
        mGloadingHolder.showEmpty()
    }

    protected fun showStateProgress() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun hideStateProgress() {
        LoadingDialogManager.get().dismissLoading()
    }

}