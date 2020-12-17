package com.guadou.kt_demo.demo.demo5_network_request.http

import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRetrofitClient

object DemoRetrofit : BaseRetrofitClient() {

    //默认的ApiService
    val apiService by lazy { getService(DemoApiService::class.java, Constants.BASE_URL) }

}