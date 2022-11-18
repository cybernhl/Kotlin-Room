package com.guadou.kt_demo.demo.demo18_customview.star


import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.temperature.TemperatureView
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.utils.log.YYLogUtils


class StarScoreViewActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<StarScoreViewActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_star_score

    override fun startObserve() {

    }

    override fun init() {

        val starView = findViewById<StarScoreView>(R.id.star_view)

        starView.setOnStarChangeListener {
            YYLogUtils.w("当前选中的Star:$it")
        }

        findViewById<View>(R.id.set_progress).click {
            starView.setStarMark(3.5f)
        }
    }


}