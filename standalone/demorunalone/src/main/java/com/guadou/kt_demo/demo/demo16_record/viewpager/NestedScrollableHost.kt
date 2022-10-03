package com.guadou.kt_demo.demo.demo16_record.viewpager

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlin.math.absoluteValue
import kotlin.math.sign


class NestedScrollableHost : FrameLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    private var touchSlop = 0
    private var initialX = 0f
    private var initialY = 0f
    private val parentViewPager: ViewPager2?
        get() {
            var v: View? = parent as? View
            while (v != null && v !is ViewPager2) {
                v = v.parent as? View
            }
            return v as? ViewPager2
        }

    private val child: View? get() = if (childCount > 0) getChildAt(0) else null

    init {
        touchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    private fun canChildScroll(orientation: Int, delta: Float): Boolean {
        val direction = -delta.sign.toInt()
        return when (orientation) {
            0 -> child?.canScrollHorizontally(direction) ?: false
            1 -> child?.canScrollVertically(direction) ?: false
            else -> throw IllegalArgumentException()
        }
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        //默认都是返回false/super不拦截， 只有当子View不能滚动的时候拦截事件
        return handleInterceptTouchEvent(e)
    }

    private fun handleInterceptTouchEvent(e: MotionEvent): Boolean {

        val orientation = parentViewPager?.orientation ?: return false

        if (!canChildScroll(orientation, -1f) && !canChildScroll(orientation, 1f)) {
            return false
        }

        if (e.action == MotionEvent.ACTION_DOWN) {
            initialX = e.x
            initialY = e.y
            parent.requestDisallowInterceptTouchEvent(true)

        } else if (e.action == MotionEvent.ACTION_MOVE) {

            val dx = e.x - initialX
            val dy = e.y - initialY
            val isVpHorizontal = orientation == ORIENTATION_HORIZONTAL

            val scaledDx = dx.absoluteValue * if (isVpHorizontal) .5f else 1f
            val scaledDy = dy.absoluteValue * if (isVpHorizontal) 1f else .5f

            if (scaledDx > touchSlop || scaledDy > touchSlop) {
                return if (isVpHorizontal == (scaledDy > scaledDx)) {
                    //垂直的手势拦截
                    parent.requestDisallowInterceptTouchEvent(false)
                    true
                } else {

                    if (canChildScroll(orientation, if (isVpHorizontal) dx else dy)) {
                        //子View能滚动，不拦截事件
                        parent.requestDisallowInterceptTouchEvent(true)
                        false
                    } else {
                        //子View不能滚动，直接就拦截事件
                        parent.requestDisallowInterceptTouchEvent(false)
                        true
                    }
                }
            }
        }

        return false
    }
}
