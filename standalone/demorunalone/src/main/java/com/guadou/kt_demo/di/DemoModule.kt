package com.guadou.kt_demo.di


import com.guadou.kt_demo.ui.demo5.mvvm.Demo5Repository
import com.guadou.kt_demo.ui.demo5.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { BaseViewModel() }
    viewModel { Demo5ViewModel(get()) }
}


val repositoryModule = module {
    single { Demo5Repository() }
}


val demoModule = listOf(viewModelModule, repositoryModule)