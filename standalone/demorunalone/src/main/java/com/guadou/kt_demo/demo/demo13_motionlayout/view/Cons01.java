package com.guadou.kt_demo.demo.demo13_motionlayout.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.guadou.kt_demo.R;
import com.guadou.lib_baselib.utils.Log.YYLogUtils;


public class Cons01 extends ConstraintLayout {

    public Cons01(Context context) {
        this(context, null);
    }

    public Cons01(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public Cons01(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initViews(context);
    }

    private void initViews(Context context) {
        inflate(context, R.layout.cl_03_layout, this);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long start = System.nanoTime();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        long end = System.nanoTime();
        long offset = end - start;
        YYLogUtils.w("Cons01 onMeasure - " + offset);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        long start = System.nanoTime();
        super.onLayout(changed, l, t, r, b);
        long end = System.nanoTime();
        long offset = end - start;
        YYLogUtils.w("Cons01 onLayout - " + offset);
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        long start = System.nanoTime();
        super.dispatchDraw(canvas);
        long end = System.nanoTime();
        long offset = end - start;
        YYLogUtils.w("Cons01 onDraw - " + offset);
    }


}
