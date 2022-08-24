package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter

import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager

/**
 * 登录完成下一步的拦截器
 */
class LoginNextInterceptor(private val action: () -> Unit) : BaseLoginInterceptImpl() {

    override fun intercept(chain: LoginInterceptChain) {
        super.intercept(chain)

        if (LoginManager.isLogin()) {
            //如果已经登录执行当前的任务
            action()
        }

        mChain?.process()
    }


}