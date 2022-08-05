package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class Demo10DIModule {

    @Singleton
    @Provides
    fun provideUser(): UserBean {
        return UserBean("newki", 18, 1, listOf("中文", "英文"))
    }

}