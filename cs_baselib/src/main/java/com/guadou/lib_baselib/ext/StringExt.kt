package com.guadou.lib_baselib.ext

import android.text.TextUtils
import java.util.Objects.isNull


/**
 * 字符串处理相关
 *
 */

/**
 * 是否是手机号
 */
fun String.isPhone() =
    "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$".toRegex().matches(
        this
    )

/**
 * 是否是邮箱地址
 */
fun String.isEmail() = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?".toRegex().matches(this)

/**
 * 是否是身份证号码
 */
fun String.isIDCard() = "[1-9]\\d{16}[a-zA-Z0-9]".toRegex().matches(this)

/**
 * 是否是中文字符
 */
fun String.isChinese() = "^[\u4E00-\u9FA5]+$".toRegex().matches(this)

fun String.checkEmpty(): Boolean {

    return !(this != null && !"".equals(
        this.trim({ it <= ' ' }),
        ignoreCase = true
    ) && !"null".equals(this.trim({ it <= ' ' }), ignoreCase = true))
}

fun Collection<Any>.checkEmpty(): Boolean {
    return isNull(this) || this.isEmpty()
}

fun ArrayList<Any>.checkEmpty(): Boolean {
    return this == null || this.size > 0
}

fun Map<Any, Any>.checkEmpty(): Boolean {
    return this == null || this.size > 0
}

fun HashMap<Any, Any>.checkEmpty(): Boolean {
    return this == null || this.size > 0
}

fun LinkedHashMap<Any, Any>.checkEmpty(): Boolean {
    return this == null || this.size > 0
}


//截取，逗号转集合，集合转逗号

