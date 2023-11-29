package com.guadou.cpt_main.http

import com.guadou.cpt_main.bean.ServerTimeBean
import com.guadou.lib_baselib.bean.BaseBean
import retrofit2.http.GET
import retrofit2.http.Header

interface MainApiService {

    @GET("/index.php/api/employee/extra/time")
    suspend fun getServerTime(
        @Header("Content-Type") contentType: String,
        @Header("Accept") accept: String
    ): BaseBean<ServerTimeBean>

}