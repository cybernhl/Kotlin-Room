package com.guadou.cpt_main.mvvm

import com.guadou.cpt_main.bean.ServerTimeBean
import com.guadou.cpt_main.http.MainRetrofit
import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.testxiecheng.base.OkResult

class AuthRepository : BaseRepository() {

    suspend fun getServerTime(): OkResult<ServerTimeBean> {
        return handleErrorApiCall({
            handleApiErrorResponse(
                MainRetrofit.apiService.getServerTime(
                    Constants.NETWORK_CONTENT_TYPE,
                    Constants.NETWORK_ACCEPT_V1
                )
            )

        })
    }

}