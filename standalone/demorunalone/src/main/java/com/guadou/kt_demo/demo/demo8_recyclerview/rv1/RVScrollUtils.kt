package com.guadou.kt_demo.demo.demo8_recyclerview.rv1

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


object RVScrollUtils {

    /**
     * 缓慢滚动
     */
    fun rvSmoothScrollToPosition(recyclerView: RecyclerView, layoutManager: LinearLayoutManager, position: Int) {

        var smoothScrolling = true

        val firstPos: Int = layoutManager.findFirstVisibleItemPosition()
        val lastPos: Int = layoutManager.findLastVisibleItemPosition()

        if (position in (firstPos + 1) until lastPos) {
            val childAt: View? = layoutManager.findViewByPosition(position)
            var top = childAt?.top ?: 0
            recyclerView.smoothScrollBy(0, top)

        } else {

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                    if (smoothScrolling || newState == RecyclerView.SCROLL_STATE_IDLE) {

                        if (position in layoutManager.findFirstVisibleItemPosition() + 1..layoutManager.findLastVisibleItemPosition()) {

                            val childAt: View? = layoutManager.findViewByPosition(position)
                            val top = childAt?.top ?: 0
                            recyclerView.scrollBy(0, top)

                            recyclerView.removeOnScrollListener(this)
                        }
                        smoothScrolling = false
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                }
            })

            recyclerView.smoothScrollToPosition(position)
        }

    }


    /**
     * 直接跳转刷新Layout
     */
    fun rvScrollToPosition(rv: RecyclerView, layoutManager: LinearLayoutManager, position: Int) {

        val firstPos = layoutManager.findFirstVisibleItemPosition()
        val lastPos: Int = layoutManager.findLastVisibleItemPosition()

        if (position <= firstPos) {
            //当要置顶的项在当前显示的第一个项的前面时
            rv.scrollToPosition(position)

        } else if (position <= lastPos) {
            //当要置顶的项已经在屏幕上显示时,通过LayoutManager
            val childAt: View? = layoutManager.findViewByPosition(position)
            var top = childAt?.top ?: 0
            rv.scrollBy(0, top)

        } else {
            //当要置顶的项在当前显示的最后一项之后
            layoutManager.scrollToPositionWithOffset(position, 0)
        }
    }
}