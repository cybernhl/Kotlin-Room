package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.lib_baselib.utils.CommUtils;

public class ViewGroup1 extends ViewGroup {

    private int mFixedHeight;

    public ViewGroup1(Context context) {
        this(context, null);
    }

    public ViewGroup1(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mFixedHeight = CommUtils.dip2px(200);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        measureChildren(widthMeasureSpec,heightMeasureSpec);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childCount = getChildCount();
        //设置子View的高度
        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
        params.height = mFixedHeight * childCount;
        setLayoutParams(params);

        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() != View.GONE) {
                child.layout(l, i * mFixedHeight, r, (i + 1) * mFixedHeight);
            }

        }

    }


}
