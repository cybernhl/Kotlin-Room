package com.guadou.lib_baselib.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.Gloading

abstract class BaseLoadingFragment<VM : BaseViewModel> : AbsFragment() {

    protected lateinit var mViewModel: VM

    protected lateinit var mGloadingHolder: Gloading.Holder


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mViewModel = initVM()
        //观察网络数据状态
        mViewModel.getActionLiveData().observe(viewLifecycleOwner, stateObserver)

        init()
        startObserve()
        super.onViewCreated(view, savedInstanceState)
    }



    override fun transfromRootView(view: View): View {

        mGloadingHolder = Gloading.getDefault().wrap(view).withRetry {
            onGoadingRetry()
        }

        return mGloadingHolder.wrapper
    }

    protected open fun onGoadingRetry() {
    }

    abstract fun startObserve()
    abstract fun initVM(): VM
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