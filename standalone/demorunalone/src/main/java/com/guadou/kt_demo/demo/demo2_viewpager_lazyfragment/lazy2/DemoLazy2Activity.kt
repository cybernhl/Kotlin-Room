package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2

import android.content.Intent
import com.google.android.material.tabs.TabLayoutMediator
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo2Page2Binding
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad1Fragment
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad2Fragment
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.LazyLoad3Fragment
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext

/**
 * ViewPager2+LazyFragment
 */
class DemoLazy2Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo2Page2Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoLazy2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_2_page2)

    }

    override fun startObserve() {
    }

    override fun init() {

        mBinding.viewPager2.bindFragment(
            supportFragmentManager,
            this.lifecycle,
            listOf(Lazy2Fragment1(), Lazy2Fragment2(), Lazy2Fragment3())
        )

        val title = listOf("Demo1", "Demo2", "Demo3")
        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager2) { tab, position ->
            //回调
            tab.text = title[position]
        }.attach()
    }

}