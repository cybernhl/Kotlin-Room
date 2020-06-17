package com.guadou.kt_zoom.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.kt_zoom.http.CachedRetrofit
import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.lib_baselib.ext.networkRequest
import com.guadou.testxiecheng.base.OkResult

class MainRepository : BaseRepository() {

    suspend inline fun getIndustry(): OkResult<List<Industry>> {

        return handleErrorApiCall(call = {
            handleApiErrorResponse(

                CachedRetrofit.apiService.getIndustry(
                    Constants.NETWORK_CONTENT_TYPE,
                    Constants.NETWORK_ACCEPT_V1
                )

            )
        })

    }


    suspend inline fun getSchool(): OkResult<List<SchoolBean>> {
        return handleErrorApiCall(call = {
            handleApiErrorResponse(

                CachedRetrofit.apiService.getSchool(
                    Constants.NETWORK_CONTENT_TYPE,
                    Constants.NETWORK_ACCEPT_V1
                )
            )

        })
    }


}