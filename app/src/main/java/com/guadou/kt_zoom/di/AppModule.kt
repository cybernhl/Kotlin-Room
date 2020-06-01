package com.guadou.kt_zoom.di


import com.guadou.kt_zoom.mvvm.MainRepository
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.lib_baselib.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel { MainViewModel(get()) }
    viewModel { BaseViewModel() }
}


val repositoryModule = module {

    single { MainRepository() }
}


val appModule = listOf(viewModelModule, repositoryModule)