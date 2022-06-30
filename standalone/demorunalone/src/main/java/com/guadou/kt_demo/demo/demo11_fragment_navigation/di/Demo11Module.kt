package com.guadou.kt_demo.demo.demo11_fragment_navigation.di

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Demo11Module {

    @Provides
    fun provideMsg(): String {
        return "di提供的消息"
    }

    @Singleton
    @Provides
    fun provideBook(): Book {
        return Book("1", "2")
    }

    @Singleton
    @Provides
    fun provideLD(): MutableLiveData<String> {
        return MutableLiveData()
    }

}