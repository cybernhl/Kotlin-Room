package com.guadou.kt_demo.ui.demo10

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDao @Inject constructor() {

    fun printUser(): String {
        return "User Name"
    }
}