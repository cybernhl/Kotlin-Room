package com.guadou.kt_demo

import com.guadou.lib_baselib.base.BaseApplication


class DemoApplication : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化Koin-只能在App模块初始化
//        startKoin {
//            androidLogger()
//            androidContext(this@DemoApplication)
//            modules(mainModule)
//        }
    }
}