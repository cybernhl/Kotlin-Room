package com.guadou.kt_demo.di


import com.guadou.kt_demo.ui.demo10.UserDao
import com.guadou.kt_demo.ui.demo10.UserServer
import com.guadou.kt_demo.ui.demo5.mvvm.Demo5Repository
import com.guadou.kt_demo.ui.demo5.mvvm.Demo5ViewModel
import com.guadou.kt_demo.ui.demo8.rv4.mvvm.DemoJobRepository
import com.guadou.kt_demo.ui.demo8.rv4.mvvm.DemoJobViewModel
import com.guadou.lib_baselib.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { BaseViewModel() }
    viewModel { Demo5ViewModel(get()) }
    viewModel { DemoJobViewModel(get()) }
}

val factoryModule = module {
    factory { UserServer(get()) }
}


val repositoryModule = module {
    single { Demo5Repository() }
    single { DemoJobRepository() }
    single { UserDao() }
}


val demoModule = listOf(viewModelModule, factoryModule, repositoryModule)