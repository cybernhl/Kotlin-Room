package com.guadou.kt_demo.demo.demo16_record.scroll1

import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.utils.log.YYLogUtils


class ViewPagerScroll1Activity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_viewpager_scroll1
    }

    override fun startObserve() {

    }

    override fun init() {

        val viewPager2 = findViewById<ViewPager2>(R.id.view_pager2)

        viewPager2.adapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                return object : RecyclerView.ViewHolder(ImageView(mActivity).apply {
                    layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                }) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                holder.itemView.setBackgroundColor(
                    if (position == 0) Color.GREEN else
                        if (position == 1) Color.YELLOW else Color.RED
                )
            }

            override fun getItemCount(): Int = 4

        }

    }

}