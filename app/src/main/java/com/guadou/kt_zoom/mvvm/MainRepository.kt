package com.guadou.kt_zoom.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.kt_zoom.http.AppRetrofit
import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.lib_baselib.ext.engine.extRequestHttp
import com.guadou.testxiecheng.base.OkResult

class MainRepository : BaseRepository() {

    /**
     * 使用扩展方法，自己的引擎类请求网络
     */
    suspend inline fun getIndustry(): OkResult<List<Industry>> {

        return extRequestHttp {
            AppRetrofit.apiService.getIndustry(
                Constants.NETWORK_CONTENT_TYPE,
                Constants.NETWORK_ACCEPT_V1
            )
        }

    }

    /**
     * 使用基类方法嵌套请求网络
     */
    suspend inline fun getSchool(): OkResult<List<SchoolBean>> {

        return handleErrorApiCall(call = {
            handleApiErrorResponse(

                AppRetrofit.apiService.getSchool(
                    Constants.NETWORK_CONTENT_TYPE,
                    Constants.NETWORK_ACCEPT_V1
                )
            )

        })
    }


}