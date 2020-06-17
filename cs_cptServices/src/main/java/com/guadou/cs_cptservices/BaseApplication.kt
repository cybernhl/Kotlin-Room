package com.guadou.cs_cptservices

import android.app.Application
import android.os.Handler
import com.google.gson.Gson
import com.guadou.lib_baselib.core.BaseLibCore
import com.guadou.lib_baselib.receiver.ConnectivityReceiver


open class BaseApplication : Application() {

    companion object {
        val mGson = Gson()
    }

    override fun onCreate() {
        super.onCreate()

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