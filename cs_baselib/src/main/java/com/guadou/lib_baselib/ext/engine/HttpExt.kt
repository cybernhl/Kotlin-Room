package com.guadou.lib_baselib.ext.engine

import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.testxiecheng.base.BaseBean
import com.guadou.testxiecheng.base.OkResult

import java.io.IOException
import java.lang.Exception

/**
 * 引擎类
 * 网络请求的封装
 */

//可用引擎切换 - 默认用于 Retrofit + suspend方式
suspend fun <T : Any> BaseRepository.httpRequest(call: suspend () -> BaseBean<T>): OkResult<T> {

    return try {

        val response = call()

        if (response.code == 200) {
            OkResult.Success(response.data)
        } else {
            OkResult.Error(IOException(response.message))
        }

    } catch (e: Exception) {
        e.printStackTrace()
        OkResult.Error(handleExceptionMessage(e))
    }

}
