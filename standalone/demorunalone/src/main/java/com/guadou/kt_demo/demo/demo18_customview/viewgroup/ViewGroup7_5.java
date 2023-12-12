package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.ScreenUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewGroup7_5 extends LinearLayout {

    private int mScreenHeight;
        private GestureDetector mGestureDetector;
    private Scroller mScroller;
    private int mStart;
    private int mEnd;

    public ViewGroup7_5(Context context) {
        this(context, null);
    }

    public ViewGroup7_5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mScreenHeight = ScreenUtils.getScreenHeith(context);
        mScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        //设置ViewGroup的高度
        MarginLayoutParams mlp = (MarginLayoutParams) getLayoutParams();
        mlp.height = mScreenHeight * childCount;
        setLayoutParams(mlp);

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != View.GONE) {
                child.layout(l, i * mScreenHeight, r, (i + 1) * mScreenHeight);
            }
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    public ViewGroup7_5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onDown(MotionEvent e) {
                mStart = getScrollY();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                //如果是按下抬起，可以触发 onSingleTapUp ，但是如果是按下滑动，则不会触发
                doEventUp();
                return true;
            }

            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                // 处理滑动事件
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                scrollBy(0, (int) distanceY);
                return true;

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                doFling((int) -velocityY);
                return true;
            }
        });

        setFocusable(true);
        setClickable(true);
        setEnabled(true);
        setLongClickable(true);

    }


    private void doFling(int velocityY) {
        YYLogUtils.w("惯性滚动  == ");
        int scrollY = getScrollY();
        int maxY = Math.max(0, getChildCount() * mScreenHeight - mScreenHeight);

        mScroller.fling(0, scrollY, 0, velocityY, 0, 0, 0, maxY);

        invalidate();
    }


    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mScroller.computeScrollOffset()) { // 检查是否滚动操作完成
            scrollTo(0, mScroller.getCurrY());

            if (mScroller.getCurrX() == getScrollX() && mScroller.getCurrY() == getScrollY()) {
                postInvalidate();
            }

        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        boolean result = mGestureDetector.onTouchEvent(event); //全部事件都交给 GestureDetector 处理

        if (!result) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // 手指抬起时并且 GestureDetector 没有触发onSingleTapUp, 那么你可以在这里处理
                doEventUp();
            }
            return true;
        }
        return result;

    }

    private void doEventUp() {
        YYLogUtils.w("doEventUp doEventUp");
        mEnd = getScrollY();

        int dScrollY = mEnd - mStart;
        if (dScrollY > 0) {
            if (dScrollY < mScreenHeight / 3) {
                mScroller.startScroll(0, getScrollY(), 0, -dScrollY);

            } else {
                mScroller.startScroll(0, getScrollY(), 0, mScreenHeight - dScrollY);
            }
        } else {
            if (-dScrollY < mScreenHeight / 3) {
                mScroller.startScroll(0, getScrollY(), 0, -dScrollY);
            } else {
                mScroller.startScroll(0, getScrollY(), 0, -mScreenHeight - dScrollY);
            }
        }
        invalidate(); // 必须调用此方法来触发重绘
    }

}



