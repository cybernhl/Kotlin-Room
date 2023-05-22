package com.guadou.kt_demo.demo.demo18_customview.takevideo1;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.guadou.kt_demo.R;


/**
 * 录按钮的绘制和动画效果
 */
public class RecordedButton extends View {

    private int measuredWidth = -1;
    private Paint paint;
    private int colorGray;
    private float radius1;
    private float radius2;
    private float zoom = 0.8f; //初始化缩放比例
    private int dp5;
    private Paint paintProgress;
    private int colorBlue;

    /**
     * 当前进度 以角度为单位
     */
    private float girthPro;
    private RectF oval;
    private int max;
    private int animTime = 400;   //动画执行的时间
    private Paint paintSplit;
    private boolean isDeleteMode;
    private Paint paintDelete;
    private ValueAnimator buttonAnim;
    private float progress;


    public RecordedButton(Context context) {
        super(context);
        init();
    }

    public RecordedButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecordedButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        dp5 = (int) getResources().getDimension(R.dimen.d_5dp);
        colorGray = getResources().getColor(R.color.gray);
        colorBlue = getResources().getColor(R.color.ps_color_blue);

        paint = new Paint();
        paint.setAntiAlias(true);


        paintProgress = new Paint();
        paintProgress.setAntiAlias(true);
        paintProgress.setColor(colorBlue);
        paintProgress.setStrokeWidth(dp5);
        paintProgress.setStyle(Paint.Style.STROKE);


        paintSplit = new Paint();
        paintSplit.setAntiAlias(true);
        paintSplit.setColor(Color.WHITE);
        paintSplit.setStrokeWidth(dp5);
        paintSplit.setStyle(Paint.Style.STROKE);


        paintDelete = new Paint();
        paintDelete.setAntiAlias(true);
        paintDelete.setColor(Color.RED);
        paintDelete.setStrokeWidth(dp5);
        paintDelete.setStyle(Paint.Style.STROKE);

        //设置绘制大小
        oval = new RectF();
    }


    /**
     * 开始动画，按钮的展开和缩回
     */
    public void startAnim(float start, float end) {


        if (buttonAnim == null || !buttonAnim.isRunning()) {
            buttonAnim = ValueAnimator.ofFloat(start, end).setDuration(animTime);
            buttonAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float) animation.getAnimatedValue();
                    radius1 = measuredWidth * (zoom + value) / 2;
                    radius2 = measuredWidth * (zoom - value) / 2 - dp5;


                    value = 1 - zoom - value;
                    oval.left = measuredWidth * value / 2 + dp5 / 2;
                    oval.top = measuredWidth * value / 2 + dp5 / 2;
                    oval.right = measuredWidth * (1 - value / 2) - dp5 / 2;
                    oval.bottom = measuredWidth * (1 - value / 2) - dp5 / 2;


                    invalidate();
                }
            });
            buttonAnim.start();
        }
    }


    /**
     * 设置最大进度
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 设置进度
     */
    public void setProgress(float progress) {
        this.progress = progress;
        float ratio = progress / max;
        girthPro = 365 * ratio;

        postInvalidate();
    }

    /**
     * 清除残留的进度
     */
    public void clearProgress() {
        setProgress(0);
    }

    /**
     * 获取到当前按钮的动画
     */
    public ValueAnimator getButtonAnim() {
        return buttonAnim;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (measuredWidth == -1) {
            measuredWidth = getMeasuredWidth();

            radius1 = measuredWidth * zoom / 2;
            radius2 = measuredWidth * zoom / 2 - dp5;

            oval.left = dp5 / 2;
            oval.top = dp5 / 2;
            oval.right = measuredWidth - dp5 / 2;
            oval.bottom = measuredWidth - dp5 / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {

        //绘制外圈
        paint.setColor(colorGray);
        canvas.drawCircle(measuredWidth / 2, measuredWidth / 2, radius1, paint);
        //绘制内圈
        paint.setColor(Color.WHITE);
        canvas.drawCircle(measuredWidth / 2, measuredWidth / 2, radius2, paint);
        //绘制进度
        canvas.drawArc(oval, 270, girthPro, false, paintProgress);

    }
}
