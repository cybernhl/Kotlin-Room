package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.intercept

//定义拦截器接口
interface Interceptor {
    fun intercept(chain: InterceptChain)
}