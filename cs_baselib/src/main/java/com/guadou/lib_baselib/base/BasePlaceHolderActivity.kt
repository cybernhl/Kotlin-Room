package com.guadou.lib_baselib.base

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guadou.basiclib.R
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.ext.getVMCls
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.Gloading
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderlAdapter


abstract class BasePlaceHolderActivity<VM : BaseViewModel> : AbsActivity() {

    protected lateinit var mViewModel: VM

    protected val mGLoadingHolder by lazy {
        generateGLoading()
    }

    //如果要替换GLoading，重写次方法
    open protected fun generateGLoading(): Gloading.Holder {
        return Gloading.from(GloadingPlaceHolderlAdapter(inflatePlaceHolderLayoutRes())).wrap(this)
            .withRetry {
                onGoadingRetry()
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mViewModel = createViewModel()

        //观察网络数据状态
        mViewModel.getActionLiveData().observe(this, stateObserver)
        init()
        startObserve()
    }

    //使用这个方法简化ViewModewl的Hilt依赖注入获取
    protected inline fun <reified VM : BaseViewModel> getViewModel(): VM {
        val viewModel: VM by viewModels()
        return viewModel
    }

    //反射获取ViewModel实例
    private fun createViewModel(): VM {
        return ViewModelProvider(this).get(getVMCls(this))
    }

    abstract override fun inflateLayoutById(): Int
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