package com.guadou.kt_demo.demo.demo14_mvi.mvc

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.http.DemoRetrofit
import com.guadou.lib_baselib.base.vm.BaseRepository
import com.guadou.lib_baselib.bean.OkResult
import com.guadou.lib_baselib.engine.extRequestHttp


class OtherModel : BaseRepository() {

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
}