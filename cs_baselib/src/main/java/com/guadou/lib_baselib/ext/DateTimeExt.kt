package com.guadou.lib_baselib.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * 时间日期相关
 */


/**
 *  日期转换为时间戳
 *  字符串日期格式（比如：2018-4-6)转为毫秒
 *  @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun String.toDateMills(format: String = "yyyy-MM-dd HH:mm:ss"): Long {

    return try {
        SimpleDateFormat(format, Locale.getDefault()).parse(this).time
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }

}


/**
 * 时间戳转日期
 * Long类型时间戳转为字符串的日期格式
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun Long.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {

    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(Date(this))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}


/**
 * 时间戳转日期
 * @param format 时间的格式，默认是按照yyyy-MM-dd HH:mm:ss来转换，如果您的格式不一样，则需要传入对应的格式
 */
fun Int.toDateString(format: String = "yyyy-MM-dd HH:mm:ss"): String {

    return try {
        SimpleDateFormat(format, Locale.getDefault()).format(Date(this.toLong()))
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }

}
