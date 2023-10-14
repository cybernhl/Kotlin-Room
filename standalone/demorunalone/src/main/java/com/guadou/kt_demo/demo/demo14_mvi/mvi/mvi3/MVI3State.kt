package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi3

import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean

/**
 * 页面状态
 */
sealed class MVI3State {

    //默认空闲
    object Idle : MVI3State()

    //加载
    object Loading : MVI3State()

    //错误信息
    data class Error(val error: String) : MVI3State()

    //成功的行业数据
    data class Industries(val indusory: List<Industry>) : MVI3State()

    //成功的学校数据
    data class Schools(val schools: List<SchoolBean>) : MVI3State()

}
