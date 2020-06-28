package com.guadou.kt_zoom

import com.guadou.cpt_main.di.mainModule
import com.guadou.lib_baselib.base.BaseApplication
import com.guadou.kt_zoom.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化Koin-只能在App模块初始化
        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(appModule + mainModule)
        }
    }
}