package com.guadou.kt_demo.demo.demo13_motionlayout

import android.content.Intent
import androidx.viewpager.widget.ViewPager
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad1Fragment
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad2Fragment
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad3Fragment
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext

class Demo13ViewPagerActivity : BaseVMActivity<EmptyViewModel>() {

    private var toggle = true

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo13ViewPagerActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo13_view_pager
    }

    override fun startObserve() {
    }

    override fun init() {

        val viewPager = findViewById<ViewPager>(R.id.viewpager)

        viewPager.bindFragment(
            supportFragmentManager,
            listOf(LazyLoad1Fragment.obtainFragment(), LazyLoad2Fragment.obtainFragment(), LazyLoad3Fragment.obtainFragment()),
            listOf("Demo1", "Demo2", "Demo3"),
            behavior = 1
        )

    }

}