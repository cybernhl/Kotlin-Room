package com.guadou.lib_baselib.base

import android.app.Application
import android.os.Handler
import com.google.gson.Gson
import com.guadou.lib_baselib.core.BaseLibCore
import com.guadou.lib_baselib.receiver.ConnectivityReceiver
import com.guadou.lib_baselib.utils.NetWorkUtil


open class BaseApplication : Application() {

    //全局的静态Gson对象
    companion object {
        val mGson = Gson()
        lateinit var networkType: NetWorkUtil.NetworkType

        //检查当前是否有网络
        fun checkHasNet(): Boolean {
            return networkType != NetWorkUtil.NetworkType.NETWORK_NO && networkType != NetWorkUtil.NetworkType.NETWORK_UNKNOWN
        }
    }

    override fun onCreate() {
        super.onCreate()

        //获取到全局的网络状态
        networkType = NetWorkUtil.getNetworkType(this@BaseApplication.applicationContext)

        //全局的 CommUtil的初始化
        BaseLibCore.init(this, Handler(), android.os.Process.myTid())

        //网络监听
        ConnectivityReceiver.registerReceiver(this)

    }

    override fun onTerminate() {
        super.onTerminate()
        ConnectivityReceiver.unregisterReceiver(this)
    }

}