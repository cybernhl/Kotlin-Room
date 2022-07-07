package com.guadou.kt_demo.demo.demo11_fragment_navigation.di

import androidx.lifecycle.MutableLiveData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@Module
@InstallIn(ActivityComponent::class)
class Demo11Module {

    @Provides
    fun provideMsg(): String {
        return "di提供的消息"
    }

    @ActivityScoped
    @Provides
    fun provideBook(): Book {
        return Book("1", "2")
    }

    @ActivityScoped
    @Provides
    fun provideLD(): MutableLiveData<String> {
        return MutableLiveData()
    }

}