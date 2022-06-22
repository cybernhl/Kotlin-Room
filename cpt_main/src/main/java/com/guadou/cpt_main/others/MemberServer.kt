package com.guadou.cpt_main.others

import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class MemberServer @Inject constructor(val userDao: MemberDao) {

    fun testUser() {
        YYLogUtils.w(userDao.printUser() + userDao.toString())
        toast(userDao.printUser())
    }
}