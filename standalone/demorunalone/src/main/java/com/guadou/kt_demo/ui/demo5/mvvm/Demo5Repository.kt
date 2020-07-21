package com.guadou.kt_demo.ui.demo5.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.ui.demo5.bean.Industry
import com.guadou.kt_demo.ui.demo5.bean.SchoolBean
import com.guadou.kt_demo.ui.demo5.http.DemoRetrofit

import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.lib_baselib.ext.engine.extRequestHttp
import com.guadou.testxiecheng.base.OkResult

/**
 * 两种方式的网络请求都可以，更推荐使用扩展方法，方便统一管理
 */
class Demo5Repository : BaseRepository() {

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


}