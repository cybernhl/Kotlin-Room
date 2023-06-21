package com.guadou.kt_demo.demo.demo16_record.scroll1

import android.graphics.Color
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.bind
import com.guadou.lib_baselib.ext.dp2px
import com.guadou.lib_baselib.utils.log.YYLogUtils


class ViewPagerScroll2Activity : BaseVMActivity<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_viewpager_scroll2
    }

    override fun startObserve() {

    }

    override fun init() {

        val viewPager = findViewById<ViewPager>(R.id.view_pager)

        viewPager.bind(4) { container: ViewGroup, position: Int ->

            val imageView = ImageView(this)
            imageView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp2px(200f))

            imageView.setBackgroundColor(
                if (position == 0) Color.GREEN else
                    if (position == 1) Color.YELLOW else Color.RED
            )

            return@bind imageView
        }

    }

}