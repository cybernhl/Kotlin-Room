package com.guadou.kt_demo.di


import com.guadou.lib_baselib.base.BaseViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { BaseViewModel() }

}


val repositoryModule = module {

}


val demoModule = listOf(viewModelModule, repositoryModule)