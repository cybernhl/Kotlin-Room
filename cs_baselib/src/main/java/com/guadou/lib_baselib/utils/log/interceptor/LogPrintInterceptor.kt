package com.guadou.lib_baselib.utils.log.interceptor

import android.util.Log
import com.guadou.lib_baselib.utils.log.ILogInterceptor
import com.guadou.lib_baselib.utils.log.LogInterceptorChain

/**
 * 使用Android Log 打印日志
 */
open class LogPrintInterceptor(private val isEnable: Boolean) : ILogInterceptor {

    override fun log(priority: Int, tag: String, logMsg: String, chain: LogInterceptorChain) {

        if (isEnable) {
            Log.println(priority, tag, logMsg)
        }

        chain.process(priority, tag, logMsg)
    }

}