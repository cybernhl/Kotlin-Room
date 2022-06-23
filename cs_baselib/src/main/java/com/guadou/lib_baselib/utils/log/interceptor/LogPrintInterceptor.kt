package com.guadou.lib_baselib.utils.log.interceptor

import android.util.Log
import com.guadou.lib_baselib.utils.log.Chain
import com.guadou.lib_baselib.utils.log.LogInterceptor

/**
 * 使用Android Log 打印日志
 */
open class LogPrintInterceptor : LogInterceptor {
    override fun log(priority: Int, tag: String, log: String, chain: Chain) {
        if (enable()) {
            Log.println(priority, tag, log)
        }

        chain.proceed(priority, tag, log)
    }

    override fun enable(): Boolean {
        return true
    }
}