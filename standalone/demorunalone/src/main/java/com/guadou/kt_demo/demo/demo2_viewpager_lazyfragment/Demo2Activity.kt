package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo2Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext


/**
 * ViewPager+LazyFragment
 */
class Demo2Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo2Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_2)

    }

    override fun startObserve() {
    }

    override fun init() {

        mBinding.viewPager.bindFragment(
            supportFragmentManager,
            listOf(LazyLoad1Fragment.obtainFragment(), LazyLoad2Fragment.obtainFragment(), LazyLoad3Fragment.obtainFragment()),
            listOf("Demo1", "Demo2", "Demo3"),
            behavior = 1
        )

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }

}