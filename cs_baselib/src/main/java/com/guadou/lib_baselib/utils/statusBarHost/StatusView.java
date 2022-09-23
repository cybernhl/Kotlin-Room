
package com.guadou.lib_baselib.utils.statusBarHost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 自定义状态栏的View，用于StatusBarHostLayout中使用
 */
class StatusView extends View {

    private int mBarSize;

    public StatusView(Context context) {
        this(context, null, 0);
    }

    public StatusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);


        StatusBarHostUtils.getStatusBarHeight(this, new HeightValueCallback() {
            @Override
            public void onHeight(int height) {

                mBarSize = height;
                YYLogUtils.w("获取到的高度：statusBarHeight1：" + height);
            }
        });


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mBarSize);
    }

    //获取到当前的状态栏高度
    public int getStatusBarHeight() {
        return mBarSize;
    }
}