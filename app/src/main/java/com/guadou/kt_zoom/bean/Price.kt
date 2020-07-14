package com.guadou.kt_zoom.bean

//测试Gson容错的测试Bean
data class Price(
    val value: Float,
    val type: Float,
    val price: Float,
    val id: Int,
    val msg: String? = "abc"
)
