package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter

import androidx.annotation.CallSuper

abstract class BaseLoginInterceptImpl : Interceptor {

    protected var mChain: LoginInterceptChain? = null

    @CallSuper
    override fun intercept(chain: LoginInterceptChain) {
        mChain = chain
    }

}