package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.lazy2

import android.content.Intent
import com.google.android.material.tabs.TabLayoutMediator
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo2Page2Binding
import com.guadou.kt_demo.databinding.ActivityDemo2PagelazyBinding
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
class DemoLazy2Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo2PagelazyBinding>() {

    val fragments = mutableListOf(Lazy2Fragment1.obtainFragment(), Lazy2Fragment2.obtainFragment(), Lazy2Fragment3.obtainFragment())
    val titles = mutableListOf("Demo1", "Demo2", "Demo3")

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
        return DataBindingConfig(R.layout.activity_demo_2_pagelazy)

    }

    override fun startObserve() {
    }

    override fun init() {
        mBinding.easyTitle.addRightText("Refresh2") {
            //添加并刷新
//            titles.add("Demo4")
//            fragments.add(Lazy2Fragment1.obtainFragment())
//            mBinding.viewPager2.adapter?.notifyItemInserted(fragments.size-1)

            //更新指定位置并刷新
//            fragments[1] = Lazy2Fragment1.obtainFragment()
//            titles[1] = "Refresh2"
//            mBinding.viewPager2.adapter?.notifyItemChanged(1)

            //删除并刷新
            fragments.removeAt(2)
            mBinding.viewPager2.adapter?.notifyItemRemoved(2)
            mBinding.viewPager2.adapter?.notifyItemRangeChanged(2, 1)
        }


        mBinding.viewPager2.bindFragment(
            supportFragmentManager,
            this.lifecycle,
            fragments,
        )

        TabLayoutMediator(mBinding.tabLayout, mBinding.viewPager2) { tab, position ->
            //回调
            tab.text = titles[position]
        }.attach()
    }

}