package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop

import com.guadou.cs_cptservices.Constants
import com.guadou.lib_baselib.ext.SP
import com.guadou.lib_baselib.ext.checkEmpty


object LoginManager {

    @JvmStatic
    fun isLogin(): Boolean {

        return !SP().getString(Constants.KEY_TOKEN, "").checkEmpty()
    }

}