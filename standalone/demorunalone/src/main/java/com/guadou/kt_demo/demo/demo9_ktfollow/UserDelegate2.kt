package com.guadou.kt_demo.demo.demo9_ktfollow

import com.guadou.lib_baselib.utils.log.YYLogUtils


class UserDelegate2(private val action: IUserAction) : IUserAction by action

