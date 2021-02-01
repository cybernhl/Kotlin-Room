package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.ActivityDemo4Binding
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.banner.DemoBannerActivity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup.DemoXPopupActivity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toastSuccess
import com.guadou.lib_baselib.utils.StatusBarUtils


/**
 * 吐司 弹窗 banner
 */
class Demo4Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo4Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo4Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo4)
            .addBindingParams(BR.click, ClickProxy())
    }


    override fun startObserve() {

    }

    override fun init() {

        //默认的状态栏是白背景-黑文字
        //这里改为随EasyTitle的背景-白色文字
        StatusBarUtils.setStatusBarWhiteText(this)
        StatusBarUtils.immersive(this)

    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun testToast(){
            toastSuccess("Test Tosast")
        }

        fun navPopupPage(){
            DemoXPopupActivity.startInstance()
        }

        fun navBannerPage(){
            DemoBannerActivity.startInstance()
        }

    }

}