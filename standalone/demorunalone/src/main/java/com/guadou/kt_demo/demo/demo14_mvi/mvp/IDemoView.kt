package com.guadou.kt_demo.demo.demo14_mvi.mvp

import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry


interface IDemoView {

    fun showLoading()

    fun hideLoading()

    fun getIndustrySuccess(list: List<Industry>?)

    fun getIndustryFailed(msg: String?)

}