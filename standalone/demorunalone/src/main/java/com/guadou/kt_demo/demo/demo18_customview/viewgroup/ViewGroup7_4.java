package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.ScreenUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewGroup7_4 extends ViewGroup implements View.OnTouchListener {

    private int mScreenHeight;
    private int mLastY;
    private int mStart;
    private int mEnd;

    private Scroller mScroller;
    private VelocityTracker mVelocityTracker;

    public ViewGroup7_4(Context context) {
        this(context, null);
    }

    public ViewGroup7_4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup7_4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mScreenHeight = ScreenUtils.getScreenHeith(context);
        mScroller = new Scroller(context);

        setOnTouchListener(this);
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

    @Override
    public void computeScroll() {
        super.computeScroll();

        YYLogUtils.w("computeScroll  --->");

        if (mScroller.computeScrollOffset()) {

            // 真正fling滚动中
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();

        } else if (mScroller.isFinished()) {
            // 当fling操作完成后的逻辑
            adjustToEndPosition();
        }
    }

    private void adjustToEndPosition() {
        mEnd = getScrollY();
        int dScrollY = mEnd - mStart;

        if (dScrollY == 0 || Math.abs(dScrollY) == mScreenHeight) return;
        YYLogUtils.w("computeScroll  mScroller.isFinished dScrollY:" + dScrollY);

        // 接下来你可以判断滚动接近哪个屏幕并滚动到那里
        int finalY;
        if (dScrollY > 0) {
            if (dScrollY < mScreenHeight / 3) {
                finalY = mEnd - dScrollY;
            } else {
                finalY = mEnd + mScreenHeight - dScrollY;
            }
        } else {
            if (-dScrollY < mScreenHeight / 3) {
                finalY = mEnd - dScrollY;
            } else {
                finalY = mEnd - mScreenHeight - dScrollY;
            }
        }

        // 这里需要确保finalY不会超出你的视图界限，比如不要小于0或大于最大滚动高度
        finalY = Math.max(0, Math.min(finalY, mScreenHeight * (getChildCount() - 1)));

        mScroller.startScroll(0, mEnd, 0, finalY - mEnd);
        postInvalidate();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 初始化 VelocityTracker
                if (mVelocityTracker == null) {
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    mVelocityTracker.clear();
                }
                mVelocityTracker.addMovement(event);

                //当停止动画的时候，它会马上滚动到终点，然后向动画设置为结束。
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                mLastY = y;
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当停止动画的时候，它会马上滚动到终点，然后向动画设置为结束。
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }

                //将移动事件添加到 VelocityTracker 中
                mVelocityTracker.addMovement(event);

                int dy = mLastY - y;
                if (getScrollY() < 0) {
                    dy = 0;
                }
                //开始滚动
                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:

                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000); // 计算速度

                int initialYVelocity = (int) mVelocityTracker.getYVelocity();
                doFling(-initialYVelocity);

                mVelocityTracker.recycle(); // 回收 VelocityTracker
                mVelocityTracker = null;

                break;
            case MotionEvent.ACTION_CANCEL:
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    mVelocityTracker = null;
                }
                break;

        }

        return true;
    }

    private void doFling(int initialVelocity) {
        int scrollY = getScrollY();
        int maxY = Math.max(0, getChildCount() * mScreenHeight - mScreenHeight); // 计算最大滚动距离

        mScroller.fling(0, scrollY, 0, initialVelocity, 0, 0, 0, maxY);

        invalidate();
    }
}
