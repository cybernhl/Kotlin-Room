package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.ScreenUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewGroup7_3 extends LinearLayout {

    private int mScreenHeight;
    private GestureDetector mGestureDetector;
    private ValueAnimator mValueAnimator;
    private int mStart;
    private int mEnd;
    private int mCurrY;

    public ViewGroup7_3(Context context) {
        this(context, null);
    }

    public ViewGroup7_3(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
        mScreenHeight = ScreenUtils.getScreenHeith(context);
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
    public ViewGroup7_3(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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
                if (mValueAnimator != null && mValueAnimator.isRunning()) {
                    mValueAnimator.cancel();
                }

                scrollBy(0, (int) distanceY);
                return true;

            }

        });

        setFocusable(true);
        setClickable(true);
        setEnabled(true);
        setLongClickable(true);

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
        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            mValueAnimator.cancel();
        }

        mEnd = getScrollY();
        int dScrollY = mEnd - mStart;

        int startY = getScrollY();
        int endY = dScrollY > 0 ?
                (dScrollY < mScreenHeight / 3 ? startY - dScrollY : startY + mScreenHeight - dScrollY) :
                (Math.abs(dScrollY) < mScreenHeight / 3 ? startY - dScrollY : startY - mScreenHeight - dScrollY);

        startAnim(startY, endY);
    }

    private void startAnim(int startY, int endY) {
        mValueAnimator = ValueAnimator.ofInt(startY, endY);
        mValueAnimator.setDuration(250); // 动画执行时间可以根据需要进行调整
        mValueAnimator.setInterpolator(new DecelerateInterpolator()); // 设置动画插值器

        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrY = (Integer) animation.getAnimatedValue();
            }
        });

        mValueAnimator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                invalidate();  //启动重绘，否则无法实现滚动效果
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                // 在动画结束时执行需要的操作，例如校正位置确保对齐

                scrollTo(0, endY);
            }
        });

        mValueAnimator.start();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        if (mValueAnimator != null && mValueAnimator.isRunning()) {
            scrollTo(0, mCurrY);
            postInvalidate();  //继续不停的重绘，否则无法实现滚动的效果
        }
    }

}



