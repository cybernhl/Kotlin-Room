package com.guadou.lib_baselib.utils.log

interface ILogInterceptor {

    fun log(priority: Int, tag: String, logMsg: String, chain: LogInterceptorChain)

}