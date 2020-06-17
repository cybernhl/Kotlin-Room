package com.guadou.cpt_main.http

import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRetrofitClient
import okhttp3.OkHttpClient

object MainRetrofit : BaseRetrofitClient() {

    //默认的ApiService
    val apiService by lazy { getService(MainApiService::class.java, Constants.BASE_URL) }

    override fun handleBuilder(builder: OkHttpClient.Builder) {
    }

}