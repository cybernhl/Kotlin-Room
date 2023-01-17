package com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.event;

/**
 * 事件的监听，缩放与移动
 */
public interface TouchEventListener {
    int MIN_SCALE = -1;
    int FREE_SCALE = 0;
    int MAX_SCALE = 1;

    void onScaling(int state, int percent);
}
