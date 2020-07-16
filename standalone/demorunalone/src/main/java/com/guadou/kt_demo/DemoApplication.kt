package com.guadou.kt_demo

import com.guadou.cpt_main.di.mainModule
import com.guadou.lib_baselib.base.BaseApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class DemoApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化Koin-只能在App模块初始化
        startKoin {
            androidLogger()
            androidContext(this@DemoApplication)
            modules(mainModule)
        }
    }
}