package com.guadou.kt_demo.demo.demo5_network_request.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.kt_demo.demo.demo5_network_request.http.DemoRetrofit
import com.guadou.lib_baselib.base.vm.BaseRepository
import com.guadou.lib_baselib.bean.OkResult
import com.guadou.lib_baselib.engine.extRequestHttp
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 两种方式的网络请求都可以，更推荐使用扩展方法，方便统一管理
 */
@Singleton
class Demo5Repository @Inject constructor() : BaseRepository() {

    /**
     * 使用扩展方法，自己的引擎类请求网络
     */
    suspend inline fun getIndustry(): OkResult<List<Industry>> {

        return extRequestHttp {
            DemoRetrofit.apiService.getIndustry(
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
                DemoRetrofit.apiService.getSchool(
                    Constants.NETWORK_CONTENT_TYPE,
                    Constants.NETWORK_ACCEPT_V1
                )
            )
        })
    }

    /**
     * 网络请求使用扩展函数的方式-推荐这种，便于引擎类集中管理
     */
    suspend fun getGiroAppointmentData(token: String?): OkResult<Long> {

        return extRequestHttp {
            DemoRetrofit.apiService.getGiroAppointmentData(
                Constants.NETWORK_CONTENT_TYPE,
                Constants.NETWORK_ACCEPT_V4,
                token
            )
        }
    }

}