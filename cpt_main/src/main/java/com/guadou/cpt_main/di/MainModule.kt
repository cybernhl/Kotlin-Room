package com.guadou.cpt_main.di

import com.guadou.cpt_main.mvvm.AuthRepository
import com.guadou.cpt_main.mvvm.AuthViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {


    viewModel { AuthViewModel(get()) }

}


val repositoryModule = module {

    single { AuthRepository() }
}


val mainModule = listOf(viewModelModule, repositoryModule)