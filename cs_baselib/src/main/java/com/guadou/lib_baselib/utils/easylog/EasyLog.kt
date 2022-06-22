package com.guadou.lib_baselib.utils.easylog

import android.text.TextUtils
import org.json.JSONArray
import org.json.JSONObject
import java.io.PrintWriter
import java.io.StringWriter
import java.net.UnknownHostException

object EasyLog {

    var LINE_SEPARATOR = System.getProperty("line.separator")

    /**
     * Priority constant for the println method; use Log.v.
     */
    private const val VERBOSE = 2

    /**
     * Priority constant for the println method; use Log.d.
     */
    private const val DEBUG = 3

    /**
     * Priority constant for the println method; use Log.i.
     */
    private const val INFO = 4

    /**
     * Priority constant for the println method; use Log.w.
     */
    private const val WARN = 5

    /**
     * Priority constant for the println method; use Log.e.
     */
    private const val ERROR = 6

    /**
     * Priority constant for the println method.
     */
    private const val ASSERT = 7

    private val logInterceptors = mutableListOf<LogInterceptor>()
    private val interceptorChain = Chain(logInterceptors)

    fun d(message: String, tag: String = "/", vararg args: Any) {
        log(DEBUG, message, tag, *args)
    }

    fun e(message: String, tag: String = "/", vararg args: Any, throwable: Throwable? = null) {
        log(ERROR, message, tag, *args, throwable = throwable)
    }

    fun w(message: String, tag: String = "/", vararg args: Any) {
        log(WARN, message, tag, *args)
    }

    fun i(message: String, tag: String = "/", vararg args: Any) {
        log(INFO, message, tag, *args)
    }

    fun v(message: String, tag: String = "/", vararg args: Any) {
        log(VERBOSE, message, tag, *args)
    }

    fun wtf(message: String, tag: String = "/", vararg args: Any) {
        log(ASSERT, message, tag, *args)
    }

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