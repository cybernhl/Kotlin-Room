package com.guadou.kt_demo

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.guadou.kt_demo.demo.demo16_record.ForegroundCheck
import com.guadou.lib_baselib.base.BaseApplication
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.newki.glrecord.utils.GLCamera1Utils
import com.newki.glrecordx.camerax.utils.GLCameraxUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DemoApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //相机的初始化，提供上下文对象
        GLCameraxUtils.init(this)
        GLCamera1Utils.init(this)

        ForegroundCheck.init(this)

        //必须process版本为2.4.0版本以上 (集成process默认添加startup库)
        ProcessLifecycleOwner.get().lifecycle.addObserver(AutoForegroundObserver())
    }

    //必须common为2.4.0版本以上
    inner class AutoForegroundObserver : DefaultLifecycleObserver {
        override fun onStart(owner: LifecycleOwner) {
            //应用进入前台
            YYLogUtils.w("DemoApplication-应用进入前台")
        }

        override fun onStop(owner: LifecycleOwner) {
            //应用进入后台
            YYLogUtils.w("DemoApplication-应用进入后台")
        }
    }

}