package com.guadou.kt_demo.demo.demo16_record.viewpager

import android.graphics.Color
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.bindFragment

class ViewPagerNestActivity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_viewpager_nest
    }

    override fun startObserve() {

    }

    override fun init() {

        findViewById<ViewPager>(R.id.viewpager).apply {

            bindFragment(
                supportFragmentManager,
//                lifecycle,
                listOf(VPItemFragment(Color.RED), VPItemFragment(Color.GREEN), VPItemFragment(Color.BLUE)),
                                behavior = 1
            )

//            orientation = ViewPager2.ORIENTATION_HORIZONTAL
        }

    }

}