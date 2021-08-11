package com.guadou.kt_demo.demo.demo5_network_request.http


import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.bean.FullJobsPage
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

    /**
     * 银行卡提现已预约信息
     */
    @GET("/index.php/api/employee/giro/index")
    suspend fun getGiroAppointmentData(
        @Header("Content-Type") contentType: String?,
        @Header("Accept") accept: String?,
        @Header("Authorization") token: String?
    ): BaseBean<Long>

}