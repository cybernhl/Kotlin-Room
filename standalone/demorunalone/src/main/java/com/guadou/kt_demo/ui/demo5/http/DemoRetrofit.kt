package com.guadou.kt_demo.ui.demo5.http

import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRetrofitClient
import okhttp3.OkHttpClient

object DemoRetrofit : BaseRetrofitClient() {

    //默认的ApiService
    val apiService by lazy { getService(DemoApiService::class.java, Constants.BASE_URL) }

}