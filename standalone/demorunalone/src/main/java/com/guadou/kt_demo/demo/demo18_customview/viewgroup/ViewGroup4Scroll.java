package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;

import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.ScreenUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewGroup4Scroll extends ViewGroup4 implements View.OnTouchListener {

    private int mScreenHeight;
    private int mLastY;
    private int mStart;
    private int mEnd;
    private Scroller mScroller;

    public ViewGroup4Scroll(Context context) {
        this(context, null);
    }

    public ViewGroup4Scroll(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup4Scroll(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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

        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());

            if (mScroller.getCurrX() == getScrollX() && mScroller.getCurrY() == getScrollY() ) {
                postInvalidate();
            }

        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int y = (int) event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = y;
                mStart = getScrollY();
                break;
            case MotionEvent.ACTION_MOVE:
                //当停止动画的时候，它会马上滚动到终点，然后向动画设置为结束。
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                int dy = mLastY - y;
                if (getScrollY() < 0) {
                    dy = 0;
                }
                //开始滚动
                scrollBy(0, dy);
                mLastY = y;
                break;
            case MotionEvent.ACTION_UP:
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
                invalidate();
                break;

        }

        return true;
    }
}
