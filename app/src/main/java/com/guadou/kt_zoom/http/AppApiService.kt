package com.guadou.kt_zoom.http

import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.testxiecheng.base.BaseBean
import retrofit2.http.GET
import retrofit2.http.Header

interface AppApiService {

    @GET("/index.php/api/employee/extra/industry")
    suspend fun getIndustry(
        @Header("Content-Type") contentType: String,
        @Header("Accept") accept: String
    ): BaseBean<List<Industry>>


    @GET("/index.php/api/employee/extra/school")
    suspend fun getSchool(
        @Header("Content-Type") contentType: String,
        @Header("Accept") accept: String
    ): BaseBean<List<SchoolBean>>

}