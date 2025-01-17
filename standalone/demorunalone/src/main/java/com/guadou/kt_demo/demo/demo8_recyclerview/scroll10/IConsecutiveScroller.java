package com.guadou.kt_demo.demo.demo8_recyclerview.scroll10;

import android.view.View;

import java.util.List;

public interface IConsecutiveScroller {

    /**
     * 返回当前需要滑动的下级view。在一个时间点里只能有一个view可以滑动。
     */
    View getCurrentScrollerView();

    /**
     * 返回所有可以滑动的子view。由于ConsecutiveScrollerLayout允许它的子view包含多个可滑动的子view，所以返回一个view列表。
     */
    List<View> getScrolledViews();
}
