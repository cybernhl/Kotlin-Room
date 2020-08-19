package com.guadou.kt_zoom

import android.app.ActivityManager
import android.content.Context
import android.os.Build
import android.os.Process
import android.webkit.WebView
import com.guadou.lib_baselib.base.BaseApplication
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //WebView的兼容
        setupWebView()
    }

    //兼容Android-P
    private fun setupWebView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val processName = getProcessName(this)
            if ("com.monstarlab.yyjobs" != processName) {
                WebView.setDataDirectorySuffix(processName)
            }
        }
    }

    //获取当前的进程名称
    private fun getProcessName(context: Context?): String? {
        if (context == null) return ""
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (processInfo in manager.runningAppProcesses) {
            if (processInfo.pid == Process.myPid()) {
                return processInfo.processName
            }
        }
        return ""
    }

}