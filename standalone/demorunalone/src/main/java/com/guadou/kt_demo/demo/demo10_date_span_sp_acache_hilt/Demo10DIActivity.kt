package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import android.widget.Button
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Demo10DIActivity : BaseVMActivity<EmptyViewModel>() {

    //    @Inject
    lateinit var userServer: UserServer

    override fun getLayoutIdRes(): Int = R.layout.activity_demo10_di

    override fun init() {

        userServer = UserServer(UserDao(UserBean("newki", 18, 1, listOf("中文", "英文"))))

        findViewById<Button>(R.id.btn_01).click {
            YYLogUtils.w(userServer.toString())
            userServer.testUser()
        }
    }

    override fun startObserve() {

    }

}