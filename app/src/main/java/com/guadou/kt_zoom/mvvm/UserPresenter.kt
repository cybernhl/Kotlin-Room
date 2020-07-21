package com.guadou.kt_zoom.mvvm

import com.guadou.lib_baselib.utils.Log.YYLogUtils

class UserPresenter(val userDao: UserDao) {

    fun testUser() {
        YYLogUtils.e(userDao.printUser())
    }
}