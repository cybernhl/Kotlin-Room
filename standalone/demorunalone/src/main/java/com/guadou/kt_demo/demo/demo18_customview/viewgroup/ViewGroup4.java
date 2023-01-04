package com.guadou.kt_demo.demo.demo18_customview.viewgroup;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;


public class ViewGroup4 extends LinearLayout {

    private GestureDetector mGestureDetector;


    public ViewGroup4(Context context) {
        this(context, null);
    }

    public ViewGroup4(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("ClickableViewAccessibility")
    public ViewGroup4(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mGestureDetector = new GestureDetector(context, new MTouchDetector());

        this.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return mGestureDetector.onTouchEvent(event);
            }
        });

        setFocusable(true);
        setClickable(true);
        setEnabled(true);
        setLongClickable(true);

    }


    private static class MTouchDetector extends GestureDetector.SimpleOnGestureListener {

        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return true;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);
        return false;
    }

}



