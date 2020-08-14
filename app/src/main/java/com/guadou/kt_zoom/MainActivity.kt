package com.guadou.kt_zoom

import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel

class MainActivity : BaseActivity<BaseViewModel>() {

    override fun initVM(): BaseViewModel {
        return BaseViewModel()
    }

    override fun inflateLayoutById(): Int = R.layout.activity_main


    override fun startObserve() {

    }

    override fun init() {

    }
}