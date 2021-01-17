package com.guadou.kt_zoom

import com.guadou.cs_router.YYRouterService
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int = R.layout.activity_main

    override fun startObserve() {

    }

    override fun init() {

        btn_jump_main.click {
            YYRouterService.mainComponentServer?.startAuthActivity()
        }

    }
}