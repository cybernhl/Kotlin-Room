package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

sealed class Demo14Intent : IUiIntent {
    object GetIndustry : Demo14Intent()
    object GetSchool : Demo14Intent()
}