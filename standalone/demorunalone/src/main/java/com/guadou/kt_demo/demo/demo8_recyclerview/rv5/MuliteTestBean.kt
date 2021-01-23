package com.guadou.kt_demo.demo.demo8_recyclerview.rv5

import com.chad.library.adapter.base.entity.MultiItemEntity

data class MuliteTestBean(
    var content: String,
    val iamge: String,
    val type: Int
) : MultiItemEntity {

    companion object {
        const val TYPE_TEXT = 0
        const val TYPE_IMAGE = 1
    }

    //具体是哪一个分类
    override val itemType: Int
        get() = type

}
