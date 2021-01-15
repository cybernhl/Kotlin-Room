package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.content.Intent
import androidx.activity.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext
import kotlinx.android.synthetic.main.activity_demo_2.*


/**
 * ViewPager+LazyFragment
 */
class Demo2Activity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }


    override fun inflateLayoutById(): Int = R.layout.activity_demo_2

    override fun startObserve() {
    }

    override fun init() {

        viewPager.bindFragment(
            supportFragmentManager,
            listOf(LazyLoad1Fragment.obtainFragment(), LazyLoad2Fragment.obtainFragment(), LazyLoad3Fragment.obtainFragment()),
            listOf("Demo1", "Demo2", "Demo3")
        )

        tabLayout.setupWithViewPager(viewPager)
    }
}