package com.guadou.kt_demo.demo.demo9_ktfollow

import com.guadou.lib_baselib.utils.log.YYLogUtils


class UserDelegate1(private val action: IUserAction) : IUserAction {
    override fun attack() {
        YYLogUtils.w("UserDelegate1-需要自己实现攻击")
    }

    override fun defense() {
        YYLogUtils.w("UserDelegate1-需要自己实现防御")
    }
}