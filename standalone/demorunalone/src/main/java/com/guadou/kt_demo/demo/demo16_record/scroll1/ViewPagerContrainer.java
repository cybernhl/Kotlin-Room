package com.guadou.kt_demo.demo.demo16_record.scroll1;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewPagerContrainer extends FrameLayout {

    private static final int MIN_FLING_VELOCITY = 1000; // 最小滑动速度
    private static final float MIN_FLING_DISTANCE = 10f; // 最小滑动距离

    private float mLastX;
    private float mLastY;
    private float mStartX;
    private float mStartY;
    private long mStartTime;


    public ViewPagerContrainer(Context context) {
        super(context);
    }

    public ViewPagerContrainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isSelfHandle = false;

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
//                getParent().requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_MOVE:
//                dealtX += Math.abs(x - mLastX);
//                dealtY += Math.abs(y - mLastX);
//
//                // 这里是否拦截的判断依据是左右滑动，
//                if (dealtX >= dealtY) {
//                    YYLogUtils.w("ViewPagerContrainer - 水平的手势-能滚动不拦截");
//                    isSelfHandle = false;
//                } else {
//                    YYLogUtils.w("ViewPagerContrainer - 垂直的手势-拦截");
//                    isSelfHandle = true;
//                }
//
//                mLastX = x;
//                mLastX = y;
//                break;
//
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                isSelfHandle = false;
//                break;
//        }
//
//        return super.dispatchTouchEvent(ev);
//    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 记录按下的位置和时间
                mStartX = ev.getX();
                mStartY = ev.getY();
                mLastX = mStartX;
                mLastY = mStartY;
                mStartTime = System.currentTimeMillis();
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                // 计算滑动距离和时间差
                float dx = ev.getX() - mLastX;
                float dy = ev.getY() - mLastY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                long duration = System.currentTimeMillis() - mStartTime;

                // 计算滑动速度
                float velocity = 0f;
                if (duration > 0) {
                    velocity = distance / duration;
                }

                // 检查是否需要拦截事件
                if (Math.abs(velocity) > MIN_FLING_VELOCITY || distance > dp2px(getContext(), MIN_FLING_DISTANCE)) {
                    if (Math.abs(dy) > Math.abs(dx)) {
                        // 垂直方向滑动，拦截事件
                        YYLogUtils.w("ViewPager-Container - 垂直的手势-拦截");
                        getParent().requestDisallowInterceptTouchEvent(false);
                        isSelfHandle = true;
                        return true;
                    } else {
                        // 水平方向滑动，不拦截事件
                        YYLogUtils.w("ViewPager-Container - 水平的手势-能滚动不拦截");
                        getParent().requestDisallowInterceptTouchEvent(true);
                        isSelfHandle = false;
                    }
                }

                mLastX = ev.getX();
                mLastY = ev.getY();
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                isSelfHandle = false;
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    // dp 转 px
    private int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        YYLogUtils.w("ViewPager-Container- onInterceptTouchEvent - isSelfHandle：" + isSelfHandle);
        //dispatchTouchEvent先处理，拿到是否拦截的处理结果，判断是否需要拦截事件
        //当onInterceptTouchEvent返回true的时候，自己处理，此时子布局ViewPager无法横向滚动无法响应事件。
        //当onInterceptTouchEvent返回false的时候，会分发事件给子布局ViwePager，此时ViewPager可以响应横向滚动与垂直事件
        return isSelfHandle;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        YYLogUtils.w("ViewPager-Container - onTouchEvent");
        return super.onTouchEvent(ev);
    }

}
