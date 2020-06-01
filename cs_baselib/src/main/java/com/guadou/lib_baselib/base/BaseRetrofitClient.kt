package com.guadou.lib_baselib.base

import com.guadou.lib_baselib.utils.interceptor.LoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by luyao
 * on 2018/3/13 14:58
 */
abstract class BaseRetrofitClient {

    companion object {
        private const val TIME_OUT = 20
    }

    private val client: OkHttpClient
        get() {
            val builder = OkHttpClient.Builder()

            //可以添加日志拦截和参数拦截
            builder.addInterceptor(LoggingInterceptor())
                .connectTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)
                .readTimeout(TIME_OUT.toLong(), TimeUnit.SECONDS)

            handleBuilder(builder)

            return builder.build()
        }

    protected abstract fun handleBuilder(builder: OkHttpClient.Builder)

    fun <S> getService(serviceClass: Class<S>, baseUrl: String): S {
        return Retrofit.Builder()
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .build().create(serviceClass)
    }
}
