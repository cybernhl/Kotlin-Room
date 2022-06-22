package com.guadou.lib_baselib.utils.log

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

object YYLogUtils {

    var LINE_SEPARATOR = System.getProperty("line.separator")

    private const val DEBUG = 3

    private const val INFO = 4

    private const val WARN = 5

    private const val ERROR = 6


    private val logInterceptors = mutableListOf<LogInterceptor>()
    private val interceptorChain = Chain(logInterceptors)

    @JvmStatic
    fun d(message: String, tag: String = "/", vararg args: Any) {
        log(DEBUG, message, tag, *args)
    }

    @JvmStatic
    fun d(message: String) {
        d(message, "/")
    }

    @JvmStatic
    fun e(message: String, tag: String = "/", vararg args: Any, throwable: Throwable? = null) {
        log(ERROR, message, tag, *args, throwable = throwable)
    }

    @JvmStatic
    fun e(message: String) {
        e(message, "/")
    }

    @JvmStatic
    fun w(message: String, tag: String = "/", vararg args: Any) {
        log(WARN, message, tag, *args)
    }

    @JvmStatic
    fun w(message: String) {
        w(message, "/")
    }

    @JvmStatic
    fun i(message: String, tag: String = "/", vararg args: Any) {
        log(INFO, message, tag, *args)
    }

    @JvmStatic
    fun i(message: String) {
        i(message, "/")
    }

    @JvmStatic
    fun json(json: String) {
        if (TextUtils.isEmpty(json)) {
            e("json 数据为空！")
            return
        }
        try {
            var message = ""
            if (json.startsWith("{")) {
                val jo = JSONObject(json)
                message = jo.toString(4)
            } else if (json.startsWith("[")) {
                val ja = JSONArray(json)
                message = ja.toString(4)
            }
            e(message)
        } catch (e: Exception) {
            e(e.cause!!.message + LINE_SEPARATOR + json)
        }
    }

    fun addInterceptor(interceptor: LogInterceptor) {
        logInterceptors.add(interceptor)
    }

    fun addFirstInterceptor(interceptor: LogInterceptor) {
        logInterceptors.add(0, interceptor)
    }

    fun removeInterceptor(interceptor: LogInterceptor) {
        logInterceptors.remove(interceptor)
    }

    @Synchronized
    private fun log(
        priority: Int,
        message: String,
        tag: String,
        vararg args: Any,
        throwable: Throwable? = null
    ) {
        var logMessage = message.format(*args)
        if (throwable != null) {
            logMessage += getStackTraceString(throwable)
        }

        interceptorChain.proceed(priority, tag, logMessage)

    }

    fun String.format(vararg args: Any) =
        if (args.isNullOrEmpty()) this else String.format(this, *args)

    private fun getStackTraceString(tr: Throwable?): String {
        if (tr == null) {
            return ""
        }

        var t = tr
        while (t != null) {
            if (t is UnknownHostException) {
                return ""
            }
            t = t.cause
        }
        val sw = StringWriter()
        val pw = PrintWriter(sw)
        tr.printStackTrace(pw)
        pw.flush()
        return sw.toString()
    }
}