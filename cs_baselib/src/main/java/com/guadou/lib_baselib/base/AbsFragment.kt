package com.guadou.lib_baselib.base

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

    /**
     * 获取Context对象
     */
    protected lateinit var mActivity: Activity
    protected lateinit var mContext: Context

    /**
     * 获取layout的id，具体由子类实现
     */
    abstract fun inflateLayoutById(): Int


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity!!
        mContext = activity!!.applicationContext

        if (needRegisterNetworkChangeObserver()) {
            ConnectivityReceiver.registerObserver(this)
            ConnectivityReceiver.registerAnnotationObserver(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(inflateLayoutById(), container, false)
        initViews(view)
        return transfromRootView(view)
    }

    //用于转换根数图View
    protected open fun transfromRootView(view: View): View {
        return view
    }

    protected open fun initViews(view: View) {
    }

    override fun onDestroyView() {
        super.onDestroyView()
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
    }

    /**
     * 是否需要注册监听网络变换
     */
    protected fun needRegisterNetworkChangeObserver(): Boolean {
        return true
    }

}
