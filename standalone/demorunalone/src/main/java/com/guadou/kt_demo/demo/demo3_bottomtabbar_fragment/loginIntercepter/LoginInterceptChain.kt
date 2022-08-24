package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter

object LoginInterceptChain {

    private var index: Int = 0

    private val interceptors by lazy(LazyThreadSafetyMode.NONE) {
        ArrayList<Interceptor>(2)
    }

    //默认初始化Login的拦截器
    private val loginIntercept = LoginInterceptor()


    // 执行拦截器。
    fun process() {

        if (interceptors.isEmpty()) return

        when (index) {
            in interceptors.indices -> {
                val interceptor = interceptors[index]
                index++
                interceptor.intercept(this)
            }

            interceptors.size -> {
                clearAllInterceptors()
            }
        }
    }

    // 添加一个拦截器。
    fun addInterceptor(interceptor: Interceptor): LoginInterceptChain {
        //默认添加Login判断的拦截器
        if (!interceptors.contains(loginIntercept)) {
            interceptors.add(loginIntercept)
        }

        if (!interceptors.contains(interceptor)) {
            interceptors.add(interceptor)
        }

        return this
    }


    //放行登录判断拦截器
    fun loginFinished() {
        if (interceptors.contains(loginIntercept) && interceptors.size > 1) {
            loginIntercept.loginfinished()
        }
    }

    //清除全部的拦截器
    private fun clearAllInterceptors() {
        index = 0
        interceptors.clear()
    }

}