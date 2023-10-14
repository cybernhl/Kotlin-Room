package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2


sealed class Demo14Effect : IUIEffect {
    data class NavigationToSchoolDetail(val id: Int) : Demo14Effect()
}