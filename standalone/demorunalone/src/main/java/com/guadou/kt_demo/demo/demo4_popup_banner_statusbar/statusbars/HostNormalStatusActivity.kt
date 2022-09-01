package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars

import android.graphics.Color
import android.view.View
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.color
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHost

/**
 * 使用宿主的方式管理状态栏
 */
class HostNormalStatusActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<HostNormalStatusActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_host_normal_status


    override fun startObserve() {

    }

    override fun init() {
        val hostLayout = StatusBarHost.inject(this)
            .setStatusBarBackground(color(R.color.white))
            .setStatusBarBlackText()

        findViewById<View>(R.id.btn_bg_color).click {

            hostLayout.setStatusBarBackground(Color.RED)
        }

        findViewById<View>(R.id.btn_bg_color2).click {

            hostLayout.setStatusBarBackground(Color.WHITE)
        }

        findViewById<View>(R.id.btn_bg_img).click {

            hostLayout.setStatusBarBackground(getDrawable(R.drawable.statusbar_image_1))
        }

        findViewById<View>(R.id.btn_text_color).click {

            hostLayout.setStatusBarWhiteText()
        }

    }

}