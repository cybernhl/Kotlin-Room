package com.guadou.kt_zoom.di


import com.guadou.kt_zoom.mvvm.*
import com.guadou.lib_baselib.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { BaseViewModel() }
    viewModel { MainViewModel(get()) }
    viewModel { ImageSelectViewModel() }
}

val factoryModule = module {
     factory { UserPresenter(get()) }
}

val repositoryModule = module {

    single { MainRepository() }
    single { UserDao() }
}


val appModule = listOf(viewModelModule, repositoryModule, factoryModule)