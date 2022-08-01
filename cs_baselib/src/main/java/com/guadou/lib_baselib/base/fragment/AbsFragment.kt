package com.guadou.lib_baselib.base.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.guadou.lib_baselib.receiver.ConnectivityReceiver


/**
 * 普通的Fragment，基类Fragment
 */

abstract class AbsFragment : Fragment(), ConnectivityReceiver.ConnectivityReceiverListener {

    private var isRootViewInit = false
    private var rootView: View? = null

    /**
     * 获取Context对象
     */
    protected lateinit var mActivity: Activity
    protected lateinit var mContext: Context


    abstract fun setContentView(container: ViewGroup?): View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = requireActivity()
        mContext = requireActivity().applicationContext

        if (needRegisterNetworkChangeObserver()) {
            ConnectivityReceiver.registerObserver(this)
            ConnectivityReceiver.registerAnnotationObserver(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        if (rootView == null) {
            rootView = transformRootView(setContentView(container))
        }

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!isRootViewInit) {//初始化过视图则不再进行view和data初始化
            super.onViewCreated(view, savedInstanceState)
            initViews(view)
            isRootViewInit = true
        }

        initViews(view)
    }

    //用于转换根数图View(可以对其做一些别的操作,例如加入GLoading)
    protected open fun transformRootView(view: View): View {
        return view
    }

    protected open fun initViews(view: View) {
    }

    /**
     * 只映射了onDestroy方法 取消任务
     */
    override fun onDestroy() {
        super.onDestroy()

        if (needRegisterNetworkChangeObserver()) {
            ConnectivityReceiver.unregisterObserver(this)
            ConnectivityReceiver.unregisterAnnotationObserver(this)
        }

        isRootViewInit = false
        rootView = null
    }

    /**
     * 是否需要注册监听网络变换
     */
    open protected fun needRegisterNetworkChangeObserver(): Boolean {
        return false
    }

}
