package com.guadou.kt_zoom.http

import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRetrofitClient
import okhttp3.OkHttpClient

object AppRetrofit : BaseRetrofitClient() {

    //默认的ApiService
    val apiService by lazy { getService(AppApiService::class.java, Constants.BASE_URL) }


    override fun needHttpCache(): Boolean {
        return true
    }

}