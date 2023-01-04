package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;


public class SwipeLayout extends FrameLayout {

    private View contentView;
    private View deleteView;
    private int contentWidth;
    private int contentHeight;
    private int deleteWidth;
    private int deleteHeight;
    private ViewDragHelper viewDragHelper;
    private float downX;
    private float downY;

    public SwipeLayout(Context context) {
        super(context);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    enum SwipeState {
        Open, Close;
    }

    private SwipeState currentState = SwipeState.Close; //默认为关闭

    private void init() {
        //是否处理触摸，是否处理拦截
        viewDragHelper = ViewDragHelper.create(this, callback);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = viewDragHelper.shouldInterceptTouchEvent(ev);
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {
            //在此关闭已经打开的item。
            SwipeLayoutManager.getInstance().closeCurrentLayout();
            result = true;
        }
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //如果当前的是打开的，下面的逻辑不能执行了
        if (!SwipeLayoutManager.getInstance().isShouldSwipe(this)) {
            requestDisallowInterceptTouchEvent(true);
            return true;
        }
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
                    //在水平移动。请求父类不要拦截
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
        deleteView = getChildAt(1);
    }

    /**
     * 完成测量时调用，获取高度，宽度
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        contentWidth = contentView.getMeasuredWidth();
        contentHeight = contentView.getMeasuredHeight();
        deleteWidth = deleteView.getMeasuredWidth();
        deleteHeight = deleteView.getMeasuredHeight();
    }

    /**
     * 调用方法完成位置的布局
     */
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        contentView.layout(0, 0, contentWidth, contentHeight);
        deleteView.layout(contentView.getRight(), 0, contentView.getRight() + deleteWidth, deleteHeight);
    }

    private ViewDragHelper.Callback callback = new ViewDragHelper.Callback() {

        //点击ContentView和右侧的DeleteView都可以触发事件
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return child == contentView || child == deleteView;
        }

        //控件水平可拖拽的范围，最多也就拖出一个右侧DeleteView的宽度
        @Override
        public int getViewHorizontalDragRange(View child) {
            return deleteWidth;
        }

        //控制水平移动的方向距离
        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            //做边界的限制
            if (child == contentView) {
                if (left > 0) left = 0;
                if (left < -deleteWidth) left = -deleteWidth;
            } else if (child == deleteView) {
                if (left > contentWidth) left = contentWidth;
                if (left < contentWidth - deleteWidth) left = contentWidth - deleteWidth;
            }
            return left;
        }

        //当前child移动后，别的view跟着做对应的移动。用于做伴随移动
        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            //做内容布局移动的时候，删除布局跟着同样的移动
            if (changedView == contentView) {
                deleteView.layout(deleteView.getLeft() + dx, deleteView.getTop() + dy,
                        deleteView.getRight() + dx, deleteView.getBottom() + dy);
            } else if (changedView == deleteView) {
                //当删除布局移动的时候，内容布局做同样的移动
                contentView.layout(contentView.getLeft() + dx, contentView.getTop() + dy,
                        contentView.getRight() + dx, contentView.getBottom() + dy);
            }

            //判断开，关的逻辑
            if (contentView.getLeft() == 0 && currentState != SwipeState.Close) {
                //关闭删除栏.删除实例
                currentState = SwipeState.Close;
                if (listener != null) {
                    listener.Close(getTag());    //在此回调关闭方法
                }
//                SwipeLayoutManager.getInstance().clearCurrentLayout();
            } else if (contentView.getLeft() == -deleteWidth && currentState != SwipeState.Open) {
                //开启删除栏。获取实例
                currentState = SwipeState.Open;
                if (listener != null) {
                    listener.Open(getTag());     //在此回调打开方法
                }
                SwipeLayoutManager.getInstance().setSwipeLayout(SwipeLayout.this);
            }
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            //松开之后，缓慢滑动，看是到打开状态还是到关闭状态
            if (contentView.getLeft() < -deleteWidth / 2) {
                //打开
                open();
            } else {
                //关闭
                close();
            }
        }
    };

    /**
     * 打开开关的的方法
     */
    public void open() {
        viewDragHelper.smoothSlideViewTo(contentView, -deleteWidth, 0);
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    /**
     * 关闭开关的方法
     */
    public void close() {
        viewDragHelper.smoothSlideViewTo(contentView, 0, 0);
        ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
    }

    /**
     * 刷新的方法
     */
    @Override
    public void computeScroll() {
        if (viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(SwipeLayout.this);
        }
    }

    private OnSwipeStateChangeListener listener;

    public void seOnSwipeStateChangeListener(OnSwipeStateChangeListener listener) {
        this.listener = listener;
    }

    public interface OnSwipeStateChangeListener {
        void Open(Object tag);

        void Close(Object tag);
    }
}
