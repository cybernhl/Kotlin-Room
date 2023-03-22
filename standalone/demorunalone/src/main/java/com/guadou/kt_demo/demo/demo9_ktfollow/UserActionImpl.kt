package com.guadou.kt_demo.demo.demo9_ktfollow

import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlin.reflect.KProperty

class UserActionImpl : IUserAction {

    override fun attack() {
        YYLogUtils.w("默认操作-开始执行攻击")
    }

    override fun defense() {
        YYLogUtils.w("默认操作-开始执行防御")
    }

}