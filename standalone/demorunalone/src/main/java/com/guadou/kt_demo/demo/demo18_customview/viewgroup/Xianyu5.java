package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class Xianyu5 extends ViewGroup {

    static final String SCROLL_MORE = "释放查看";
    private static final String RELEASE_MORE = "查看详情";

    private float mLastX;
    private float mLastY;
    private static final float RATIO = 0.4f;  //阻尼
    private float mHintLeftMargin = 0;

    private ShowMoreTextView mMoreTextView;
    private int showMoreViewWidth;
    private int mOffsetWidth;
    private int showMoreViewHeight;

    // 释放的回弹动画
    private ValueAnimator mReleasedAnim;
    private View mContentView;
    private int contentWidth;
    private int contentHeight;
    private float mDeltaX;
    private float mDeltaY;

    public Xianyu5(Context context) {
        this(context, null);
    }

    public Xianyu5(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Xianyu5(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //完成初始化，获取控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mContentView = getChildAt(0);
        mMoreTextView = (ShowMoreTextView) getChildAt(1);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = mContentView.getMeasuredWidth();
        contentHeight = mContentView.getMeasuredHeight();
        showMoreViewWidth = mMoreTextView.getMeasuredWidth();
        showMoreViewHeight = mMoreTextView.getMeasuredHeight();

        //右侧布局的偏移量
        mOffsetWidth = -showMoreViewWidth;

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //测量真正的容器的布局
        measureChild(mContentView, widthMeasureSpec, heightMeasureSpec);

        //测量ShowMore布局
        measureChild(mMoreTextView, widthMeasureSpec, heightMeasureSpec);

        this.setMeasuredDimension(mContentView.getMeasuredWidth(), mContentView.getMeasuredHeight());
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {

        mContentView.layout(0, 0, contentWidth, contentHeight);

        mMoreTextView.layout(contentWidth, contentHeight / 2 - showMoreViewHeight / 2,
                contentWidth + showMoreViewWidth, contentHeight / 2 - showMoreViewHeight / 2 + showMoreViewHeight);
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHintLeftMargin = 0;
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:

                // 释放动画
                if (mReleasedAnim != null && mReleasedAnim.isRunning()) {
                    break;
                }

                mDeltaX = (ev.getRawX() - mLastX);
                mDeltaY = ev.getRawY() - mLastY;

                mLastX = ev.getRawX();
                mLastY = ev.getRawY();

                mDeltaX = mDeltaX * RATIO;

                YYLogUtils.w("mDeltaX:" + mDeltaX);

                //滑动的赋值
                if (mDeltaX > 0) {
                    // 右滑
                    setHintTextTranslationX(mDeltaX);

                } else if (mDeltaX < 0) {
                    // 左滑
                    setHintTextTranslationX(mDeltaX);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:

                //拦截事件-父布局滚
                getParent().requestDisallowInterceptTouchEvent(false);

                // 释放动画
                if (mReleasedAnim != null && mReleasedAnim.isRunning()) {
                    break;
                }

                //如果达到指定位置了才算释放
                if (mOffsetWidth != 0 && mHintLeftMargin <= mOffsetWidth && mListener != null) {
                    mListener.onRelease();
                }

                //默认的回去动画
                mReleasedAnim = ValueAnimator.ofFloat(1.0f, 0);
                mReleasedAnim.setDuration(300);
                mReleasedAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        float value = (float) animation.getAnimatedValue();

                        mMoreTextView.setTranslationX(value * mMoreTextView.getTranslationX());
                    }
                });
                mReleasedAnim.start();

                break;

        }

        return true;
    }

    /**
     * 设置ShowMore布局的偏移量，并且设置内部重绘贝塞尔曲线的控制点变量
     */
    private void setHintTextTranslationX(float deltaX) {

        float offsetX = 0;
        if (mMoreTextView != null) {
            mHintLeftMargin += deltaX;
            if (mHintLeftMargin <= mOffsetWidth) {
                offsetX = mOffsetWidth;
                mMoreTextView.setVerticalText(RELEASE_MORE);
            } else {
                offsetX = mHintLeftMargin;
                mMoreTextView.setVerticalText(SCROLL_MORE);
            }
            mMoreTextView.setShadowOffset(offsetX, mOffsetWidth);
            mMoreTextView.setTranslationX(offsetX);

            YYLogUtils.w("setTranslationX:" + offsetX);
        }

    }

    public interface OnShowMoreListener {
        void onRelease();
    }

    private OnShowMoreListener mListener;

    public void setOnShowMoreListener(OnShowMoreListener listener) {
        this.mListener = listener;
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mReleasedAnim != null && mReleasedAnim.isRunning()) {
            mReleasedAnim.cancel();
            mReleasedAnim = null;
        }
    }

}
