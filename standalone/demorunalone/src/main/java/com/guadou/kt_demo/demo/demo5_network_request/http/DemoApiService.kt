package com.guadou.kt_demo.demo.demo5_network_request.http


import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.bean.NewsBean
import com.guadou.lib_baselib.bean.BaseBean
import retrofit2.http.*

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
    ): BaseBean<NewsBean>

    /**
     * 银行卡提现已预约信息
     */
    @GET("/index.php/api/employee/giro/index")
    suspend fun getGiroAppointmentData(
        @Header("Content-Type") contentType: String?,
        @Header("Accept") accept: String?,
        @Header("Authorization") token: String?
    ): BaseBean<Long>


    @POST("/wanandroid")
    suspend fun fetchNews(
        @FieldMap map: Map<String, String>
    ): BaseBean<NewsBean>


    @POST("/wanandroid")
    suspend fun changeState(
        @FieldMap map: Map<String, String>
    ): BaseBean<String>

}