package com.guadou.lib_baselib.bean

data class BaseBean<out T>(val code: Int, val message: String, val data: T)