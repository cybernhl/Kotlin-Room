package com.guadou.kt_demo.demo.demo5_network_request

/**
 *
 */
class SuccessCallbackImpl : TestNet.SuccessCallback {

    private var onSuccess: ((String) -> String)? = null

    private var doSth: (() -> Unit)? = null

    fun onSuccess(method: (String) -> String) {
        onSuccess = method
    }

    fun doSth(method: () -> Unit) {
        doSth = method
    }

    override fun onSuccess(str: String): String {
        return onSuccess?.invoke(str).toString()
    }

    override fun doSth() {
        doSth?.invoke()
    }
}