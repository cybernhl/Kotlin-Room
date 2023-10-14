package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi3

/**
 * 页面意图
 */
sealed class MVI3Intent {

    //行为- 想要获取行业数据
    object GetIndustry : MVI3Intent()

    //行为- 想要获取学校数据
    object GetSchool : MVI3Intent()

}
