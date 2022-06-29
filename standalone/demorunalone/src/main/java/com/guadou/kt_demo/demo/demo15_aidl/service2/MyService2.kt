package com.guadou.kt_demo.demo.demo15_aidl.service2

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.guadou.lib_baselib.utils.DeviceUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils

class MyService2 : Service() {

    private val mBinder: MyBinder2 = MyBinder2(this)

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    fun doSomeThing(message: String?): String {

        //做什么事情呢 ...
        YYLogUtils.w("MyService2 - Recive Message: $message")

        return "Hello Activity2"
    }

    fun shotdown() {

        //测试杀死进程
        DeviceUtils.killProcess("test")
    }


}