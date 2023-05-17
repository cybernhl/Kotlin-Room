package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.content.Intent
import android.view.View
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo2Page2Binding
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.adapter.ViewPagerFragmentAdapter
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext

/**
 * ViewPager2+LazyFragment
 */
class Demo2PagerCustomActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo2Page2Binding>() {

    val fragments = mutableListOf(LazyLoad1Fragment.obtainFragment(), LazyLoad2Fragment.obtainFragment(), LazyLoad3Fragment.obtainFragment());
    val titles = mutableListOf("Demo1", "Demo2", "Demo3")
    val adapter = ViewPagerFragmentAdapter(supportFragmentManager, fragments, null)

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo2PagerCustomActivity::class.java).apply {
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
        mBinding.easyTitle.addRightText("Refresh") {
            //添加并刷新
            fragments.add(LazyLoad1Fragment.obtainFragment())
            titles.add("Demo4")
            addTab("Demo4")

            //更新指定位置并刷新
//            fragments[2] = LazyLoad2Fragment.obtainFragment()
//            titles[2] = "Refresh1"

            //反转换位置呢
//            fragments.reverse()
//            titles.reverse()

            //删除并刷新
//            fragments.removeAt(2)
//            titles.removeAt(2)
//            mBinding.tabLayout.removeTabAt(2)

            mBinding.viewPager.adapter?.notifyDataSetChanged()
            mBinding.viewPager.offscreenPageLimit = fragments.size - 1
        }

        titles.forEach {
            addTab(it)
        }

        mBinding.viewPager.adapter = adapter
        mBinding.viewPager.offscreenPageLimit = fragments.size - 1

        //自定义Tab不能这么设置了？
//        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)

       // 需要手动的写监听绑定？
        mBinding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {}
            override fun onPageSelected(i: Int) {
                mBinding.tabLayout.setScrollPosition(i, 0f, true)
            }

            override fun onPageScrollStateChanged(i: Int) {}
        })

        mBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                mBinding.viewPager.setCurrentItem(position, true)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

    }

    private fun addTab(content: String) {
        val tab: TabLayout.Tab = mBinding.tabLayout.newTab()
        val view: View = layoutInflater.inflate(R.layout.tab_custom_layout, null)
        tab.customView = view

        val textView = view.findViewById<TextView>(R.id.tab_text)
        textView.text = content

        mBinding.tabLayout.addTab(tab)
    }

}