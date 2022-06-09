package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept

import androidx.annotation.CallSuper

abstract class BaseInterceptImpl : Interceptor {

    protected var mChain: InterceptChain? = null

    @CallSuper
    override fun intercept(chain: InterceptChain) {
        mChain = chain
    }

}