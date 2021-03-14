package com.guadou.cpt_main.mvvm

import com.guadou.cpt_main.bean.ServerTimeBean
import com.guadou.cpt_main.http.MainRetrofit
import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.base.vm.BaseRepository
import com.guadou.lib_baselib.bean.OkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor() : BaseRepository() {

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