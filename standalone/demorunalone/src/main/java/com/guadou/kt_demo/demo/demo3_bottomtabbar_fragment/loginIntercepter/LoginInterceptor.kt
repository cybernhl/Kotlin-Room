package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter

import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.LoginDemoActivity
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager

/**
 * 判断是否登录的拦截器
 */
class LoginInterceptor : BaseLoginInterceptImpl() {

    override fun intercept(chain: LoginInterceptChain) {
        super.intercept(chain)

        if (LoginManager.isLogin()) {
            //如果已经登录 -> 放行, 转交给下一个拦截器
            chain.process()
        } else {
            //如果未登录 -> 去登录页面
            LoginDemoActivity.startInstance()
        }
    }


    fun loginfinished() {
        //如果登录完成，调用方法放行到下一个拦截器
        mChain?.process()
    }
}