package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserServer @Inject constructor(private val userDao: UserDao) {

    fun testUser() {
        YYLogUtils.w(userDao.printUser())
        toast(userDao.printUser())
    }

    fun getDaoContent():String{
        return userDao.printUser()
    }

}