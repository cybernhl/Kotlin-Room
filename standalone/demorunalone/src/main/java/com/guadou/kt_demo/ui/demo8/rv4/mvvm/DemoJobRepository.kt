package com.guadou.kt_demo.ui.demo8.rv4.mvvm

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.ui.demo5.http.DemoRetrofit
import com.guadou.kt_demo.ui.demo8.rv4.bean.FullJobsPage
import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.lib_baselib.ext.engine.extRequestHttp
import com.guadou.testxiecheng.base.OkResult

class DemoJobRepository : BaseRepository() {

    /**
     * 使用扩展方法，自己的引擎类请求网络
     */
    suspend inline fun getAllJobs(curPage: Int): OkResult<FullJobsPage> {

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