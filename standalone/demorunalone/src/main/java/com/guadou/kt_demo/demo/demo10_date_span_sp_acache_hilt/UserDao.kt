package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserDao @Inject constructor(private val user: UserBean) {

    fun printUser(): String {
        return user.toString()
    }

}