package com.guadou.kt_demo.demo.demo15_aidl.service1

import android.os.Binder

/**
 * Binder持有Service的对象
 */
class MyBinder1(private val service: MyService1) : Binder() {
    var callback: ((String) -> Unit)? = null

    fun setOnCallbackListener(action: ((String) -> Unit)?) {
        callback = action
    }

    //调用Service的方法
    fun doServiceMethod(message: String) {
        val msg = service.doSomeThing(message)

        callback?.invoke(msg)
    }

}