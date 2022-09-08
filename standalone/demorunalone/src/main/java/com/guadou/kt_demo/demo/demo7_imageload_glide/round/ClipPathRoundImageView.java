package com.guadou.kt_demo.demo.demo7_imageload_glide.round;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.guadou.lib_baselib.utils.CommUtils;


public class ClipPathRoundImageView extends AppCompatImageView {

    float width, height;
    private int mRoundRadius = CommUtils.dip2px(15);

    public ClipPathRoundImageView(Context context) {
        this(context, null);
    }

    public ClipPathRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClipPathRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //在api11到api18之间设置禁用硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth();
        height = getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (width > mRoundRadius && height > mRoundRadius) {
            Path path = new Path();
            path.moveTo(mRoundRadius, 0);

            path.lineTo(width - mRoundRadius, 0);  //上侧的横线

            path.quadTo(width, 0, width, mRoundRadius);  //右上的圆弧曲线

            path.lineTo(width, height - mRoundRadius);  //右侧的直线

            path.quadTo(width, height, width - mRoundRadius, height);  //右下的圆弧曲线

            path.lineTo(mRoundRadius, height);  //下侧的横线

            path.quadTo(0, height, 0, height - mRoundRadius);  //左下的圆弧曲线

            path.lineTo(0, mRoundRadius);       //左侧的直线

            path.quadTo(0, 0, mRoundRadius, 0);   //左上的圆弧曲线

            canvas.clipPath(path);
        }

        super.onDraw(canvas);
    }
}
