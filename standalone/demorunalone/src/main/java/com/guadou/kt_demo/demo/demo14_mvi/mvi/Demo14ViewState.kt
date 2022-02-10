package com.guadou.kt_demo.demo.demo14_mvi.mvi

import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.lib_baselib.base.mvi.BaseViewState

data class Demo14ViewState(
    val industrys: List<Industry> = emptyList(),
    val schools: List<SchoolBean> = emptyList(),
    var isChanged: Boolean = false
) : BaseViewState()
