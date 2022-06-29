package com.guadou.kt_demo.demo.demo15_aidl.service1

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.guadou.lib_baselib.utils.log.YYLogUtils

class MyService1 : Service() {

    private val mBinder: MyBinder1 = MyBinder1(this)

    override fun onBind(intent: Intent?): IBinder {
        return mBinder
    }

    fun doSomeThing(message: String): String {

        //做什么事情呢 ...
        YYLogUtils.w("MyService1 - Recive Message: $message")

        return "Hello Activity"
    }


}