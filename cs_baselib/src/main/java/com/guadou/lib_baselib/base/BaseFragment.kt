package com.guadou.lib_baselib.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.ext.getVMCls
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager

/**
 * 加入ViewModel与LoadState
 */
abstract class BaseFragment<VM : BaseViewModel> : AbsFragment() {

    protected lateinit var mViewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mViewModel = createViewModel()
        //观察网络数据状态
        mViewModel.getActionLiveData().observe(viewLifecycleOwner, stateObserver)

        init()
        startObserve()
    }

    //使用这个方法简化ViewModel的初始化
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

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
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

    protected fun showStateNormal() {}

    protected fun showStateError(message: String?) {
        LoadingDialogManager.get().dismissLoading()
    }

    protected fun showStateSuccess() {
        LoadingDialogManager.get().dismissLoading()
    }

    protected fun showStateLoading() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun showStateNoData() {
        LoadingDialogManager.get().dismissLoading()
    }

    protected fun showStateProgress() {
        LoadingDialogManager.get().showLoading(mActivity)
    }

    protected fun hideStateProgress() {
        LoadingDialogManager.get().dismissLoading()
    }
}