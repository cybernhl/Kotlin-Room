package com.guadou.kt_zoom

import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel

class MainActivity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int = R.layout.activity_main

    override fun startObserve() {

    }

    override fun init() {

//        btn_jump_main.click {
//            YYRouterService.mainComponentServer?.startAuthActivity()
//        }

    }
}