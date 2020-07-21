package com.guadou.kt_demo.ui.demo2

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import kotlinx.android.synthetic.main.activity_demo_1.*
import kotlinx.android.synthetic.main.activity_demo_2.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * ViewPager+LazyFragment
 */
class Demo2Activity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

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