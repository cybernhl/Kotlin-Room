package com.guadou.lib_baselib.utils.log

interface LogInterceptor {

    fun log(priority: Int, tag: String, log: String, chain: Chain)

    fun enable(): Boolean
}