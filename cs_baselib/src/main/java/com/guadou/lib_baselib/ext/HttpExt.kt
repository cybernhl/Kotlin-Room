package com.guadou.lib_baselib.ext

import android.text.TextUtils
import com.guadou.lib_baselib.base.BaseRepository
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.testxiecheng.base.BaseBean
import com.guadou.testxiecheng.base.OkResult
import kotlinx.coroutines.coroutineScope
import java.io.IOException

/**
 * 网络请求的封装
 */

suspend fun <T : Any> BaseRepository.networkRequest(bean: BaseBean<T>): OkResult<T> {
    return handleErrorApiCall(call = {
        handleApiErrorResponse(
                bean
        )

    })

}
