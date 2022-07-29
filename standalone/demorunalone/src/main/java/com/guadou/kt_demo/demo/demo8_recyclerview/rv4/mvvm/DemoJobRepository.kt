package com.guadou.kt_demo.demo.demo8_recyclerview.rv4.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.demo.demo5_network_request.http.DemoRetrofit
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.bean.NewsBean
import com.guadou.lib_baselib.base.vm.BaseRepository
import com.guadou.lib_baselib.engine.extRequestHttp
import com.guadou.lib_baselib.bean.OkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DemoJobRepository @Inject constructor() : BaseRepository() {

    /**
     * 使用扩展方法，自己的引擎类请求网络
     */
    suspend inline fun getAllJobs(curPage: Int): OkResult<NewsBean> {

        return extRequestHttp {
            DemoRetrofit.apiService.getAllJobs(
                curPage.toString(),
                "10",
                Constants.NETWORK_CONTENT_TYPE,
                Constants.NETWORK_ACCEPT_V9
            )
        }
    }

}