package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class UserServer @Inject constructor(val userDao: UserDao) {

    fun testUser() {
        YYLogUtils.w(userDao.printUser() + userDao.toString())
        toast(userDao.printUser())
    }
}