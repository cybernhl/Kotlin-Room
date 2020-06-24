package com.guadou.kt_zoom.mvvm

import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.countDown
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel


class ImageSelectViewModel : BaseViewModel() {

    private var mTimeDownScope: CoroutineScope? = null

    @ExperimentalCoroutinesApi
    fun startCountDown() {

        countDown(start = {
            mTimeDownScope = it
            YYLogUtils.e("开始")

        }, end = {
            YYLogUtils.e("结果倒计时")

        }, next = {

            if (it == 2) {
                mTimeDownScope?.cancel()
            }

            YYLogUtils.e("倒计时:$it")
        })

    }
}