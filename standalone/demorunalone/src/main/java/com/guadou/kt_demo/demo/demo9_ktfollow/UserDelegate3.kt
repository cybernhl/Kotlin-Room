package com.guadou.kt_demo.demo.demo9_ktfollow

import com.guadou.lib_baselib.utils.log.YYLogUtils


class UserDelegate3(private val action: IUserAction) : IUserAction by action {

    override fun attack() {
        YYLogUtils.w("UserDelegate3 - 只重写了攻击")
    }
}

