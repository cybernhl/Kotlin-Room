package com.guadou.kt_demo.demo.demo11_fragment_navigation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class Demo11Module {

    @Provides
    fun provideMsg(): String {
        return "di提供的消息"
    }

}