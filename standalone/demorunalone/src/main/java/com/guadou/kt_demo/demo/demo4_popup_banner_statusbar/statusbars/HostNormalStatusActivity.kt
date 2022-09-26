package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars

import android.graphics.Color
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityHostNormalStatusBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.color
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHost
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHostLayout

/**
 * 使用宿主的方式管理状态栏
 */
class HostNormalStatusActivity : BaseVDBActivity<EmptyViewModel, ActivityHostNormalStatusBinding>() {

    lateinit var hostLayout: StatusBarHostLayout

    companion object {
        fun startInstance() {
            commContext().gotoActivity<HostNormalStatusActivity>()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_host_normal_status)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {
        hostLayout = StatusBarHost.inject(this)
            .setStatusBarBackground(color(R.color.white))
            .setStatusBarBlackText()
            .setNavigationBarBackground(color(R.color.colorAccent))
            .setNavigatiopnBarIconWhite()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {


        fun changeBGRed() {
            hostLayout.setStatusBarBackground(Color.RED)
        }

        fun changeBGWhite() {
            hostLayout.setStatusBarBackground(Color.WHITE)
            hostLayout.setStatusBarBlackText()
        }

        fun changeBGImage() {
            hostLayout.setStatusBarBackground(getDrawable(R.drawable.statusbar_image_1))
            hostLayout.setStatusBarWhiteText()
        }

        fun changeTextColor() {
            hostLayout.setStatusBarWhiteText()
        }

    }


}