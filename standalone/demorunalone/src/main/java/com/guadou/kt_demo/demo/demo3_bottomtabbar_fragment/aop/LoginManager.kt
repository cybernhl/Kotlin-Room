package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop

import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.LoginDemoActivity
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils


object LoginManager {

    @JvmStatic
    fun isLogin(): Boolean {
        val token = SP().getString(Constants.KEY_TOKEN, "")
        YYLogUtils.w("LoginManager-token:$token")
        val checkEmpty = token.checkEmpty()
        return !checkEmpty
    }

    @JvmStatic
    fun gotoLoginPage() {
        commContext().gotoActivity<LoginDemoActivity>()
    }
}