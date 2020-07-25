package com.guadou.kt_demo.ui.demo5.http


import com.guadou.kt_demo.ui.demo5.bean.Industry
import com.guadou.kt_demo.ui.demo5.bean.SchoolBean
import com.guadou.kt_demo.ui.demo8.rv4.bean.FullJobsPage
import com.guadou.testxiecheng.base.BaseBean
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DemoApiService {

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


    //全职的工作
    @GET("/index.php/api/employee/full-time/jobs")
    suspend fun getAllJobs(
        @Query("cur_page") cur_page: String,
        @Query("page_size") page_size: String,
        @Header("Content-Type") contentType: String,
        @Header("Accept") accept: String
    ): BaseBean<FullJobsPage>

}