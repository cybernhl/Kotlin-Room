package com.guadou.kt_demo.demo.demo13_motionlayout.view

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager.widget.ViewPager

/**
 * 监听ViewPager的滚动 设置MotionLayout的Progress
 */
class MyViewpagerMotionLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : MotionLayout(context, attrs, defStyleAttr), ViewPager.OnPageChangeListener {

    override fun onPageScrollStateChanged(state: Int) {
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val numPages = 3
        progress = (position + positionOffset) / (numPages - 1)
    }

    override fun onPageSelected(position: Int) {
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        val viewGroup = (parent as? ViewGroup)!!
        for (i in 0 until viewGroup.childCount) {
            val view = viewGroup.getChildAt(i)
            if (view is ViewPager) {
                view.addOnPageChangeListener(this)
                break
            }
        }

    }
}