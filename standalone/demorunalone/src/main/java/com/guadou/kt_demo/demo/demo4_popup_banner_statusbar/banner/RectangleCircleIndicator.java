package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.banner;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.youth.banner.indicator.BaseIndicator;

/**
 * 选中的是矩形-未选中的是圆形
 */
class RectangleCircleIndicator extends BaseIndicator {
    RectF rectF;

    public RectangleCircleIndicator(Context context) {
        this(context, null);
    }

    public RectangleCircleIndicator(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleCircleIndicator(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        rectF = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        //间距*（总数-1）+默认宽度*（总数-1）+选中宽度
        int space = config.getIndicatorSpace() * (count - 1);
        int normal = config.getNormalWidth() * (count - 1);

        int width = space + normal + config.getSelectedWidth();
        setMeasuredDimension(width, Math.max(config.getNormalWidth(), config.getSelectedWidth()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int count = config.getIndicatorSize();
        if (count <= 1) {
            return;
        }
        float left = 0;
        for (int i = 0; i < count; i++) {

            if (config.getCurrentPosition() == i) {
                //选中
                mPaint.setColor(config.getSelectedColor());

                int circleWidth = config.getNormalWidth();
                int indicatorWidth = config.getSelectedWidth();
                int top = circleWidth / 2 - config.getHeight() / 2;
                rectF.set(left, top, left + indicatorWidth, top + config.getHeight());
                left += indicatorWidth + config.getIndicatorSpace();
                canvas.drawRoundRect(rectF, config.getRadius(), config.getRadius(), mPaint);

            } else {
                //未选中
                mPaint.setColor(config.getNormalColor());
                int indicatorWidth = config.getNormalWidth();

                int radius = indicatorWidth / 2;
                canvas.drawCircle(left + radius, radius, radius, mPaint);
                left += indicatorWidth + config.getIndicatorSpace();
            }

        }
    }

}