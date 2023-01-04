package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.guadou.lib_baselib.utils.log.YYLogUtils;


public class ViewGroup4Child1 extends View {

    private GestureDetector mGestureDetector;

    public ViewGroup4Child1(Context context) {
        this(context, null);
    }

    public ViewGroup4Child1(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup4Child1(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);


    }

    private void init(Context context) {
        mGestureDetector = new GestureDetector(context, new MTouchDetector());
        setClickable(true);
        setFocusable(true);
        setEnabled(true);
        setLongClickable(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        setMeasuredDimension(600, 200);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor((Color.parseColor("#FFA07A")));

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        int action = event.getAction();

        if (action == MotionEvent.ACTION_DOWN) {

            YYLogUtils.w("dispatchTouchEvent-按下");
        } else if (action == MotionEvent.ACTION_MOVE) {
            YYLogUtils.w("dispatchTouchEvent-移动");
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            YYLogUtils.w("dispatchTouchEvent-完成");
        }

        // 先告诉父Viewgroup，不要拦截我我，然后再内部判断是否拦截
        getParent().requestDisallowInterceptTouchEvent(true);
        //将Event事件交给监听器 OnGestureListener
        mGestureDetector.onTouchEvent(event);

        return super.dispatchTouchEvent(event);
    }


    private class MTouchDetector extends GestureDetector.SimpleOnGestureListener {

        public boolean onDown(MotionEvent e) {
            YYLogUtils.w("MTouchDetector-onDown");
            return super.onDown(e);
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            YYLogUtils.w("MTouchDetector-onScroll");

            //手势与垂直方向大于30°,相应子View的onScroll,父Viewgroup的onScroll则不会响应
            if (1.732 * Math.abs(distanceX) >= Math.abs(distanceY)) {

                YYLogUtils.w("请求不要拦截我");
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;

            } else {
                YYLogUtils.w("拦截我");
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }

        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            YYLogUtils.w("MTouchDetector-onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        public boolean onDoubleTap(MotionEvent e) {
            YYLogUtils.w("MTouchDetector-onDoubleTap");
            return super.onDoubleTap(e);
        }

        public boolean onDoubleTapEvent(MotionEvent e) {
            YYLogUtils.w("MTouchDetector-onDoubleTapEvent");
            return super.onDoubleTapEvent(e);
        }

    }

}
