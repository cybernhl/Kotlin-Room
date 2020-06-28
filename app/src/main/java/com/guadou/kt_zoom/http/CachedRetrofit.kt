package com.guadou.kt_zoom.http

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRetrofitClient
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.NetWorkUtil

import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import java.io.File

object CachedRetrofit : BaseRetrofitClient() {

    //默认的ApiService
    val apiService by lazy { getService(AppApiService::class.java, Constants.BASE_URL) }


    private val cookieJar by lazy {
        //第三方Cookie管理工具
        PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(CommUtils.getContext()))
    }

    override fun handleBuilder(builder: OkHttpClient.Builder) {

        val httpCacheDirectory = File(CommUtils.getContext().cacheDir, "responses") //Http缓存目录
        val cacheSize = 10 * 1024 * 1024L // 10 MiB
        val cache = Cache(httpCacheDirectory, cacheSize)     //Http缓存对象
        builder.cache(cache)
            .cookieJar(cookieJar)
            .addInterceptor { chain ->
                var request = chain.request()

                if (!NetWorkUtil.isAvailable(CommUtils.getContext())) {
                    request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build()
                }

                val response = chain.proceed(request)
                if (!NetWorkUtil.isAvailable(CommUtils.getContext())) {
                    val maxAge = 60 * 60
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, max-age=$maxAge")
                        .build()
                } else {
                    val maxStale = 60 * 60 * 24 * 28 // tolerate 4-weeks stale
                    response.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                        .build()
                }
                response
            }
    }
}