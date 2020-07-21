package com.guadou.kt_demo.ui.demo4

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.ext.toastSuccess
import com.guadou.lib_baselib.utils.StatusBarUtils
import kotlinx.android.synthetic.main.activity_demo4.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 吐司 弹窗 banner
 */
class Demo4Activity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo4Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo4

    override fun startObserve() {

    }

    override fun init() {

        //默认的状态栏是白背景-黑文字
        //这里改为随EasyTitle的背景-白色文字
        StatusBarUtils.setStatusBarWhiteText(this)
        StatusBarUtils.immersive(this)

        initLitener()
    }

    private fun initLitener() {

        btn_shot_toast.click {
            toastSuccess("Test Tosast")
        }

        btn_shot_alert.click {
            toast("xPopup不用写了吧")
        }

        btn_shot_banner.click {
            //暂时没做-准备用banner库或者用ViewPager的扩展试试
            toast("暂时没做-准备用banner库或者用ViewPager的扩展试试")
        }
    }

}