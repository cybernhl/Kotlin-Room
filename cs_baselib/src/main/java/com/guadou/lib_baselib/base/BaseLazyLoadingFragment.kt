package com.guadou.lib_baselib.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.guadou.lib_baselib.bean.LoadAction
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.Gloading


abstract class BaseLazyLoadingFragment<VM : BaseViewModel> : AbsFragment() {

    protected lateinit var mViewModel: VM
    private var isViewCreated = false//布局是否被创建
    private var isLoadData = false//数据是否加载
    private var isFirstVisible = true//是否第一次可见
    protected lateinit var mGloadingHolder: Gloading.Holder

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true

        mViewModel = initVM()
        //观察网络数据状态
        mViewModel.getActionLiveData().observe(viewLifecycleOwner, stateObserver)

        init()
        startObserve()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (isFragmentVisible(this) && this.isAdded) {

            if (parentFragment == null || isFragmentVisible(parentFragment)) {
                onLazyInitData()
                isLoadData = true
                if (isFirstVisible) isFirstVisible = false
            }
        }
    }


    abstract fun startObserve()
    abstract fun initVM(): VM
    abstract fun init()
    abstract fun onLazyInitData()

    //Loading Create Root View
    override fun transfromRootView(view: View): View {

        mGloadingHolder = Gloading.getDefault().wrap(view).withRetry {
            onGoadingRetry()
        }

        return mGloadingHolder.wrapper
    }

    protected open fun onGoadingRetry() {
    }

    override fun onNetworkConnectionChanged(
        isConnected: Boolean,
        networkType: NetWorkUtil.NetworkType?
    ) {
    }

    // ============================  Lazy Load begin ↓  =============================

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isFragmentVisible(this) && !isLoadData && isViewCreated && this.isAdded) {
            onLazyInitData()
            isLoadData = true
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //onHiddenChanged调用在Resumed之前，所以此时可能fragment被add, 但还没resumed
        if (!hidden && !this.isResumed)
            return
        //使用hide和show时，fragment的所有生命周期方法都不会调用，除了onHiddenChanged（）
        if (!hidden && isFirstVisible && this.isAdded) {
            onLazyInitData()
            isFirstVisible = false
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        isViewCreated = false
        isLoadData = false
        isFirstVisible = true
    }

    /**
     * 当前Fragment是否对用户是否可见
     * @param fragment 要判断的fragment
     * @return true表示对用户可见
     */
    private fun isFragmentVisible(fragment: Fragment?): Boolean {
        return !fragment?.isHidden!! && fragment.userVisibleHint
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