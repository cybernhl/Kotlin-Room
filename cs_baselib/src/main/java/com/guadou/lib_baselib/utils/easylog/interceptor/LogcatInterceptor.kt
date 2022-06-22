package com.guadou.lib_baselib.utils.easylog.interceptor

import android.util.Log
import com.guadou.lib_baselib.utils.easylog.Chain
import com.guadou.lib_baselib.utils.easylog.LogInterceptor

open class LogcatInterceptor : LogInterceptor {
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