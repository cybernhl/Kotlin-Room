package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.guadou.lib_baselib.utils.log.YYLogUtils


class MyPager2Adapter(
    fm: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

    override fun getItemId(position: Int): Long {
       val name = fragments[position].javaClass.simpleName+ position
        val toLong = name.hashCode().toLong()
        YYLogUtils.w("getItemId:$toLong")
        return toLong
    }
}