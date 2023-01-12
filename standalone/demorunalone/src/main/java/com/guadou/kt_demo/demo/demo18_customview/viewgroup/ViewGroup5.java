package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;

import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public class ViewGroup5 extends ViewGroup {

    static final String SCROLL_MORE = "更多";
    private static final String RELEASE_MORE = "释放查看";

    private RecyclerView mHorizontalRecyclerView;
    private ShowMoreTextView mMoreTextView;

    private float mHintLeftMargin = 0;
    private int mOffsetWidth = 0;
    private float mLastX;
    private float mLastY;
    private boolean mConsumeMoveEvent = false;
    private int mMoveIndex = 0;

    // 释放的回弹动画
    private ValueAnimator mReleasedAnim;

    private static final float RATIO = 0.4f;  //阻尼

    private int rvContentWidth;
    private int rvContentHeight;
    private int showMoreViewWidth;
    private int showMoreViewHeight;

    public ViewGroup5(Context context) {
        this(context, null);
    }

    public ViewGroup5(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
    }

    //展示之后获取宽高信息
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        rvContentWidth = mHorizontalRecyclerView.getMeasuredWidth();
        rvContentHeight = mHorizontalRecyclerView.getMeasuredHeight();
        showMoreViewWidth = mMoreTextView.getMeasuredWidth();
        showMoreViewHeight = rvContentHeight;

        //右侧布局的偏移量
        mOffsetWidth = -showMoreViewWidth;
    }

    //完成初始化，获取控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mHorizontalRecyclerView = (RecyclerView) getChildAt(0);
        mMoreTextView = (ShowMoreTextView) getChildAt(1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //RV测量 - 默认的测量不改动
        measureChild(mHorizontalRecyclerView, widthMeasureSpec, heightMeasureSpec);
        int width = mHorizontalRecyclerView.getMeasuredWidth();
        int height = mHorizontalRecyclerView.getMeasuredHeight();

        //右侧ShowMore的测量 - 自行改动高度测量
        final LayoutParams lp = mMoreTextView.getLayoutParams();
        mMoreTextView.measure(
                getChildMeasureSpec(widthMeasureSpec, mMoreTextView.getPaddingLeft() + mMoreTextView.getPaddingRight(), lp.width),
                getChildMeasureSpec(MeasureSpec.EXACTLY, mMoreTextView.getPaddingTop() + mMoreTextView.getPaddingBottom(), height)
        );

        //指定ViewGroup的测量 - 父布局的测量就是RV的宽高
        this.setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        mHorizontalRecyclerView.layout(0, 0, rvContentWidth, rvContentHeight);
        mMoreTextView.layout(mHorizontalRecyclerView.getRight(), 0, mHorizontalRecyclerView.getRight() + showMoreViewWidth, showMoreViewHeight);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (mHorizontalRecyclerView == null) {
            return super.dispatchTouchEvent(ev);
        }
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mHintLeftMargin = 0;
                mMoveIndex = 0;
                mConsumeMoveEvent = false;
                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                // 释放动画
                if (mReleasedAnim != null && mReleasedAnim.isRunning()) {
                    break;
                }
                float mDeltaX = (ev.getRawX() - mLastX);
                float mDeltaY = ev.getRawY() - mLastY;

                if (!mConsumeMoveEvent) {
                    // 处理事件冲突
                    if (Math.abs(mDeltaX) > Math.abs(mDeltaY)) {
                        //拦截事件-让我滚
                        getParent().requestDisallowInterceptTouchEvent(true);
                    } else {
                        //拦截事件-父布局滚
                        getParent().requestDisallowInterceptTouchEvent(false);
                    }
                }

                mMoveIndex++;

                if (mMoveIndex > 2) {
                    mConsumeMoveEvent = true;
                }

                mLastX = ev.getRawX();
                mLastY = ev.getRawY();
                mDeltaX = mDeltaX * RATIO;

                //滑动的赋值
                if (mDeltaX > 0) {
                    // 右滑并判断是否滑动到边缘
                    if (!mHorizontalRecyclerView.canScrollHorizontally(-1) || mHorizontalRecyclerView.getTranslationX() < 0) {
                        //偏移值加上已偏移的值
                        float transX = mDeltaX + mHorizontalRecyclerView.getTranslationX();
                        if (mHorizontalRecyclerView.canScrollHorizontally(-1) && transX >= 0) {
                            transX = 0;
                        }

                        //RV和ShowMore一起设置-TranslationX
                        mHorizontalRecyclerView.setTranslationX(transX);
                        setHintTextTranslationX(mDeltaX);
                    }
                } else if (mDeltaX < 0) {
                    // 左滑并判断是否滑动到边缘
                    if (!mHorizontalRecyclerView.canScrollHorizontally(1) || mHorizontalRecyclerView.getTranslationX() > 0) {
                        //偏移值加上已偏移的值
                        float transX = mDeltaX + mHorizontalRecyclerView.getTranslationX();
                        if (transX <= 0 && mHorizontalRecyclerView.canScrollHorizontally(1)) {
                            transX = 0;
                        }
                        //RV和ShowMore一起设置-TranslationX
                        mHorizontalRecyclerView.setTranslationX(transX);
                        setHintTextTranslationX(mDeltaX);
                    }
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
                        mHorizontalRecyclerView.setTranslationX(value * mHorizontalRecyclerView.getTranslationX());
                        mMoreTextView.setTranslationX(value * mMoreTextView.getTranslationX());
                    }
                });
                mReleasedAnim.start();

                break;

        }
        return mHorizontalRecyclerView.getTranslationX() != 0 ? true : super.dispatchTouchEvent(ev);
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
