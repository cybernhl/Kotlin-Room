package com.guadou.lib_baselib.utils.log


class LogInterceptorChain(
    private var interceptors: MutableList<ILogInterceptor>,
    private var index: Int = 0
) {

    // 执行拦截器。
    fun process(priority: Int, tag: String, logMsg: String) {

        val nextChain = LogInterceptorChain(interceptors, index + 1)

        //拿到下一个拦截器
        val interceptor = interceptors.getOrNull(index)

        //通过下一个拦截器处理当前处理好的log
        interceptor?.log(priority, tag, logMsg, nextChain)

    }

}