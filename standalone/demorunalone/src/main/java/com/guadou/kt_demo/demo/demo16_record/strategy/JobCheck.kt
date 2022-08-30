package com.guadou.kt_demo.demo.demo16_record.strategy

data class JobCheck(
    //人物要求
    val nric: String,
    val visa: String,
    val nationality: String,
    val age: Float,
    val workingYear: Float,
    val isFillProfile: Boolean,

    var hasCovidTest: Boolean,

    //工作要求
    val language: String,
    val gender: Int,
    val deposit: String,
    val isSelfProvide: Boolean,
    val isTrained: Boolean,
)
