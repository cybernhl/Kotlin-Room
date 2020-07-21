package com.guadou.lib_baselib.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.guadou.basiclib.R
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderlAdapter

abstract class BasePlaceHolderFragment<VM : BaseViewModel> : AbsFragment() {

    protected lateinit var mViewModel: VM

    protected lateinit var mGLoadingHolder: Gloading.Holder


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = initVM()
        //观察网络数据状态
        mViewModel.getActionLiveData().observe(viewLifecycleOwner, stateObserver)

        init()
        startObserve()
    }


    override fun transformRootView(view: View): View {

        mGLoadingHolder = generateGLoading(view)

        return mGLoadingHolder.wrapper
    }

    //如果要替换GLoading，重写次方法
    private fun generateGLoading(view: View): Gloading.Holder {
        return Gloading.from(GloadingPlaceHolderlAdapter(inflatePlaceHolderLayoutRes())).wrap(view)
            .withRetry {
                onGoadingRetry()
            }
    }

    protected open fun inflatePlaceHolderLayoutRes(): Int = R.layout.layout_placeholder2

    protected open fun onGoadingRetry() {
    }

    abstract fun initVM(): VM
    abstract override fun inflateLayoutById(): Int
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

            when (loadAction.action) {
                LoadAction.STATE_NORMAL -> showStateNormal()
                LoadAction.STATE_ERROR -> showStateError(loadAction.message)
                LoadAction.STATE_SUCCESS -> showStateSuccess()
                LoadAction.STATE_LOADING -> showStateLoading()
                LoadAction.STATE_NO_DATA -> showStateNoData()
                LoadAction.STATE_PROGRESS -> showStateProgress()
                LoadAction.STATE_HIDE_PROGRESS -> hideStateProgress()
            }

        }
    }

    protected open fun showStateNormal() {}

    protected open fun showStateLoading() {
        mGLoadingHolder.showLoading()
    }

    protected open fun showStateSuccess() {
        mGLoadingHolder.showLoadSuccess()
    }

    protected open fun showStateError(message: String?) {
        mGLoadingHolder.showLoadFailed(message)
    }

    protected open fun showStateNoData() {
        mGLoadingHolder.showEmpty()
    }

    protected fun showStateProgress() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun hideStateProgress() {
        LoadingDialogManager.get().dismissLoading()
    }

}