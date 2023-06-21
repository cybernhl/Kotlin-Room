package com.guadou.kt_demo.demo.demo16_record.scroll1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

import com.guadou.lib_baselib.utils.log.YYLogUtils;


/**
 * @auther Newki
 * @date 2023/6/1
 * @description XX
 */
public class MyViewPager extends ViewPager {

    int lastX = -1;
    int lastY = -1;
    private float startX;
    private float startY;

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        int x = (int) ev.getRawX();
//        int y = (int) ev.getRawY();
//        int dealtX = 0;
//        int dealtY = 0;
//
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                dealtX = 0;
//                dealtY = 0;
//                // 保证子View能够接收到Action_move事件
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                dealtX += Math.abs(x - lastX);
//                dealtY += Math.abs(y - lastY);
//
//                // 这里是否拦截的判断依据是左右滑动，
//                if (dealtX >= dealtY) {
//                    YYLogUtils.w("MyViewPager - 水平的手势-能滚动不拦截");
//                    getParent().requestDisallowInterceptTouchEvent(true); //父布局不拦截
//                } else {
//                    YYLogUtils.w("MyViewPager - 垂直的手势-拦截");
//                    getParent().requestDisallowInterceptTouchEvent(false); //父布局要拦截
//                }
//                lastX = x;
//                lastY = y;
//                break;
//
//
//        }
//        return super.dispatchTouchEvent(ev);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent ev) {
//        YYLogUtils.w("MyViewPager - onTouchEvent");
//        return super.onTouchEvent(ev);
//    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        // 检查是否应该拦截触摸事件
//        switch (ev.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//                startX = ev.getX();
//                startY = ev.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float deltaX = Math.abs(ev.getX() - startX);
//                float deltaY = Math.abs(ev.getY() - startY);
//                if (deltaX > deltaY) {
//                    YYLogUtils.w("MyViewPager - 水平的手势-能滚动不拦截");
//                    // 如果水平滑动距离大于垂直滑动距离，则不拦截事件
//                    return false;
//                } else {
//                    YYLogUtils.w("MyViewPager - 垂直的手势-拦截");
//                    // 如果垂直滑动距离大于等于水平滑动距离，则交给MotionLayout处理事件
//                    return true;
//                }
//            case MotionEvent.ACTION_UP:
//                // 如果手指松开，则调用MotionLayout的方法，以触发动画效果
//                YYLogUtils.w("MyViewPager - 调用MotionLayout");
//                ((MotionLayout)getParent()).transitionToEnd();
//                break;
//        }
//        YYLogUtils.w("MyViewPager - super.onInterceptTouchEvent");
//        return super.onInterceptTouchEvent(ev);
//    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
//        if (ev.getAction() == MotionEvent.ACTION_UP) {
//            // 如果手指松开，则调用MotionLayout的方法，以触发动画效果
//            ((MotionLayout)getParent()).transitionToEnd();
//        }
        YYLogUtils.w("MyViewPager - onTouchEvent");
        return super.onTouchEvent(ev);
    }

}

