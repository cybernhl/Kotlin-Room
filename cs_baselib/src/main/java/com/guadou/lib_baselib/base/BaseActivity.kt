package com.guadou.lib_baselib.base

import android.os.Bundle
import androidx.lifecycle.Observer
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager

abstract class BaseActivity<VM : BaseViewModel> : AbsActivity() {

    protected lateinit var mViewModel: VM

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
                LoadAction.STATE_LOADING == loadAction.action -> showStateLoading(loadAction.message)
                LoadAction.STATE_NO_DATA == loadAction.action -> showStateNoData()
                LoadAction.STATE_PROGRESS == loadAction.action -> showStateProgress()
            }

        }
    }

    protected fun showStateNormal() {}

    protected fun showStateError(message: String?) {
        LoadingDialogManager.get().dismissLoading()
    }

    protected fun showStateSuccess() {
        LoadingDialogManager.get().dismissLoading()
    }

    protected fun showStateLoading(message: String?) {
        LoadingDialogManager.get().showLoading(this)
    }

    protected fun showStateProgress() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun showStateNoData() {
        LoadingDialogManager.get().dismissLoading()
    }
}