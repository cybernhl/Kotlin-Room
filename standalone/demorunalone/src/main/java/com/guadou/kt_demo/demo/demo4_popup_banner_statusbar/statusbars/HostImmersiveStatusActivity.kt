package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars

import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.drawable
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHost

/**
 * 使用宿主的方式管理状态栏-沉浸式
 */
class HostImmersiveStatusActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<HostImmersiveStatusActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_host_immersive_status


    override fun startObserve() {

    }

    override fun init() {
        val hostLayout = StatusBarHost.inject(this)
            .setStatusBarImmersive(true)
            .setStatusBarWhiteText()
            .setNavigationBarBackground(drawable(R.drawable.statusbar_image_1))


        findViewById<View>(R.id.btn_001).click {

            hostLayout.setStatusBarImmersive(true)
        }

        findViewById<View>(R.id.btn_002).click {

            hostLayout.setStatusBarImmersive(false)
        }

        findViewById<View>(R.id.btn_003).click {
            hostLayout.setViewFitsStatusBarView(findViewById(R.id.title_view))
        }

    }

}