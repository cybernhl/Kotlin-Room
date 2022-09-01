package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.statusbars

import android.widget.ImageView
import androidx.core.widget.NestedScrollView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.color
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.CalculateUtils
import com.guadou.lib_baselib.utils.SizeUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHost
import com.guadou.lib_baselib.utils.statusBarHost.StatusBarHostLayout
import com.guadou.lib_baselib.view.titlebar.EasyTitleBar

/**
 * 使用宿主的方式管理状态栏
 */
class HostScrollStatusActivity : BaseVMActivity<EmptyViewModel>() {

    private var imgHeight = 0
    lateinit var hostLayout: StatusBarHostLayout
    lateinit var easyTitleBar: EasyTitleBar

    companion object {
        fun startInstance() {
            commContext().gotoActivity<HostScrollStatusActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_host_scroll_status


    override fun startObserve() {

    }


    override fun init() {

        val startColor: Int = color(R.color.white)
        val endColor: Int = color(R.color.colorPrimary)

        //默认的状态处理
        hostLayout = StatusBarHost.inject(this)
            .setStatusBarBackground(startColor)
            .setStatusBarWhiteText()


        //监听滚动
        val myScrollView = findViewById<NestedScrollView>(R.id.my_scroll)
        easyTitleBar = findViewById(R.id.easy_title)
        easyTitleBar.setBackgroundColor(startColor)
        val topImg = findViewById<ImageView>(R.id.iv_top_img)

        SizeUtils.forceGetViewSize(topImg) {
            imgHeight = it.height
        }

        myScrollView.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

            if (imgHeight > 0) {

                val scrollDistance = Math.min(scrollY, imgHeight)

                val fraction: Float = scrollDistance.toFloat() / imgHeight.toFloat()

                YYLogUtils.w("滚动：scrollY：$scrollY imgHeight:$imgHeight  scrollDistance:$scrollDistance fraction:$fraction")

                setTitleStatusBarAlpha(CalculateUtils.evaluate(fraction, startColor, endColor))
            }

        })

    }

    private fun setTitleStatusBarAlpha(color: Int) {
        easyTitleBar.setBackgroundColor(color)
        hostLayout.setStatusBarBackground(color)
    }


}