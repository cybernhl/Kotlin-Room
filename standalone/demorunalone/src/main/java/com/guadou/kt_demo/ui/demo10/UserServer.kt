package com.guadou.kt_demo.ui.demo10

import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.Log.YYLogUtils

class UserServer(val userDao: UserDao) {

    fun testUser() {
        YYLogUtils.e(userDao.printUser())
        toast(userDao.printUser())
    }
}