package com.guadou.kt_zoom

import com.guadou.cpt_main.di.mainModule
import com.guadou.cs_cptservices.BaseApplication
import com.guadou.kt_zoom.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApp : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        //初始化Koin-只能在App模块初始化
        startKoin {
            androidLogger()
            androidContext(this@MyApp)
            modules(appModule + mainModule)
        }
    }
}