package com.guadou.kt_zoom

import com.guadou.cs_router.YYRouterService
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<EmptyViewModel>() {

    override fun initVM(): EmptyViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_main

    override fun startObserve() {

    }

    override fun init() {

        btn_jump_main.click {
            YYRouterService.mainComponentServer?.startAuthActivity()
        }

    }
}