package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean

data class Demo14State(val industryUiState: IndustryUiState, val schoolUiState: SchoolUiState, val loadUiState: LoadUiState) : IUiState {}


// 内部分子类，定义了常见的四种加载状态
sealed class LoadUiState {
    object Idle : LoadUiState()  //默认空闲
    data class Loading(var isShow: Boolean) : LoadUiState()  //展示Loading
    object ShowContent : LoadUiState()           //展示布局
    data class Error(val msg: String) : LoadUiState()  //失败
}

// 内部分子类，并实现初始化值，成功值，失败值
sealed class IndustryUiState {
    object INIT : IndustryUiState()
    data class SUCCESS(val industries: List<Industry>) : IndustryUiState()
}

// 内部分子类，并实现初始化值，成功值，失败值
sealed class SchoolUiState {
    object INIT : SchoolUiState()
    data class SUCCESS(val schooles: List<SchoolBean>) : SchoolUiState()
}
