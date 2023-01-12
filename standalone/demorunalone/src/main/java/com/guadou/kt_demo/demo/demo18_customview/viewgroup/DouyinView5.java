package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

public class DouyinView5 extends FrameLayout {

    private View contentView;
    private View detailView;
    private int contentWidth;
    private int contentHeight;
    private int detailWidth;
    private int detailHeight;
    private ViewDragHelper viewDragHelper;
    private float downX;
    private float downY;

    public DouyinView5(Context context) {
        super(context);
        init();
    }

    public DouyinView5(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DouyinView5(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return viewDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = event.getX();
                downY = event.getY();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = event.getX();
                float moveY = event.getY();
                float dx = moveX - downX;
                float dy = moveY - downY;

                if (Math.abs(dx) > Math.abs(dy)) {
                    requestDisallowInterceptTouchEvent(true);
                }

                downX = moveX;
                downY = moveY;
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        viewDragHelper.processTouchEvent(event);

        return true;
    }

    //完成初始化，获取控件
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        contentView = getChildAt(0);
        detailView = getChildAt(1);
    }

    /**
     * 完成测量时调用，获取高度，宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        detailWidth = detailView.getMeasuredWidth();
        detailHeight = detailView.getMeasuredHeight();
    }

    /**
     * 调用方法完成位置的布局
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        contentView.layout(0, 0, contentWidth, contentHeight);
        detailView.layout(contentWidth, 0, contentWidth + detailWidth, detailHeight);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == detailView;
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return detailWidth;
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //边界的限制
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -detailWidth) left = -detailWidth;
            } else if (child == detailView) {
                if (left > contentWidth) left = contentWidth;
                if (left < contentWidth - detailWidth) left = contentWidth - detailWidth;
            }
            return left;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //做内容布局移动的时候，详情布局跟着同样的移动
            if (changedView == contentView) {
                detailView.layout(detailView.getLeft() + dx, detailView.getTop() + dy,
                        detailView.getRight() + dx, detailView.getBottom() + dy);
            } else if (changedView == detailView) {
                //当详情布局移动的时候，内容布局做同样的移动
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy,
                        contentView.getRight() + dx, contentView.getBottom() + dy);
            }

        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //松开之后，只要移动超过一半就可以打开或者关闭
            if (contentView.getLeft() < -detailWidth / 2) {
                open();
            } else {
                close();
            }
        }
    };

    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -detailWidth, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }


    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

}

