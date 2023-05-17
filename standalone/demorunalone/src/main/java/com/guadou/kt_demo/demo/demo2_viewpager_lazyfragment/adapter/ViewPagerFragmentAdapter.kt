package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.google.android.material.tabs.TabLayout
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.utils.log.YYLogUtils
import java.util.*


/**
 * 首页Fragment-ViewPager的适配器
 */
class ViewPagerFragmentAdapter @JvmOverloads constructor(
    private val fragmentManager: FragmentManager,
    private val fragments: List<Fragment>,
    private val pageTitles: List<String>? = null,
    behavor: Int = 0
) : FragmentStatePagerAdapter(fragmentManager, behavor) {

    private val fragmentMap = mutableMapOf<Int, Fragment>()
    private val fragmentPositions = hashMapOf<Int, Int>()

    init {
        for ((index, fragment) in fragments.withIndex()) {
            fragmentMap[index] = fragment
        }
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return if (fragments.isEmpty()) 0 else fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (pageTitles == null) "" else pageTitles[position]
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        YYLogUtils.w("ViewPagerFragmentAdapter-instantiateItem")

        val fragment = super.instantiateItem(container, position) as Fragment

        val id = generateUniqueId()
        var args = fragment.arguments
        if (args == null) {
            args = Bundle()
        }

        args.putInt("_uuid", id)
        fragment.arguments = args

        // 存储 Fragment 的位置信息
        fragmentPositions[id] = position

        return fragment
    }

    private fun generateUniqueId(): Int {
        // 生成唯一的 ID
        return UUID.randomUUID().hashCode()
    }

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        super.destroyItem(container, position, obj)
    }

    override fun getItemPosition(obj: Any): Int {
        YYLogUtils.w("ViewPagerFragmentAdapter-getItemPosition")

        val fragment = obj as Fragment

        // 从 Fragment 中获取唯一的 ID
        val args = fragment.arguments
        if (args != null && args.containsKey("_uuid")) {
            val id = args.getInt("_uuid")

            // 根据 ID 获取 Fragment 在 Adapter 中的位置
            val position = fragmentPositions[id]
            return if (position != null && position == fragments.indexOf(fragment)) {
                // Fragment 未发生变化，返回 POSITION_UNCHANGED
                POSITION_UNCHANGED
            } else {
                // Fragment 发生变化，返回 POSITION_NONE
                POSITION_NONE
            }
        }

        // 如果不是 Fragment，则返回默认值
        return super.getItemPosition(obj)
    }


}