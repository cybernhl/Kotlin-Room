package com.guadou.kt_demo.demo.demo18_customview.temperature;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TemperatureView extends View {

    private Point centerPosition;   //中心点
    private RectF mRectF;   //边界矩形
    private float radius = 0f;   //半径
    private float mSmallRadius = 0f;

    private float mStartAngle = 120f;  // 圆弧的起始角度
    private float mSweepAngle = 300f; //绘制的起始角度和滑过角度(绘制300度)
    private float targetAngle = 0f;  //刻度的角度(根据此计算需要绘制有色的进度)
    private float totalAngle = 0f;  //刻度的角度(根据此计算需要绘制有色的进度)

    private Paint mDegreeCirPaint;  //刻度圆环
    private Paint mDegreelinePaint;  //刻度线

    private float mCurPercent = 0f;  //总进度
    private String mCurTemperature = "0.0";
    private DecimalFormat mDecimalFormat;

    //动画状态
    private boolean isAnimRunning;
    // 手动实现越来越慢的效果
    private int[] slow = {10, 10, 10, 8, 8, 8, 6, 6, 6, 6, 4, 4, 4, 4, 2};
    // 动画的下标
    private int goIndex = 0;
    private int mWaveUpValue = 0;
    private float mWaveMoveValue = 0f;

    private Paint mTextPaint;
    private Paint mSmallCirclePaint;
    private Paint mSmallCircleBorderPaint;
    private RectF mSmalloval;

    private float[] mFirstWaterLine;
    private float[] mSecondWaterLine;
    private Timer mWaveTimer;
    private Timer mAnimTimer;


    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        centerPosition = new Point();
        mRectF = new RectF();
        mDecimalFormat = new DecimalFormat("0.0");

        mDegreeCirPaint = new Paint();
        mDegreeCirPaint.setAntiAlias(true);
        mDegreeCirPaint.setStyle(Paint.Style.STROKE);
        mDegreeCirPaint.setStrokeWidth(2f);
        mDegreeCirPaint.setColor(Color.WHITE);
        mDegreeCirPaint.setStrokeCap(Paint.Cap.ROUND);

        mDegreelinePaint = new Paint();
        mDegreelinePaint.setStrokeWidth(2f);
        mDegreelinePaint.setColor(Color.WHITE);
        mDegreelinePaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(Color.WHITE);

        mSmallCirclePaint = new Paint();

        mSmallCircleBorderPaint = new Paint();
        mSmallCircleBorderPaint.setColor(Color.WHITE);
        mSmallCircleBorderPaint.setAlpha(65);
        mSmallCircleBorderPaint.setStrokeWidth(1f);
        mSmallCircleBorderPaint.setStyle(Paint.Style.STROKE);

        moveWaterLine();
    }

    public void moveWaterLine() {
        mWaveTimer = new Timer();
        mWaveTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                mWaveMoveValue += 1;
                if (mWaveMoveValue == 100) {
                    mWaveMoveValue = 1;
                }
                postInvalidate();
            }
        }, 500, 200);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mWaveTimer != null) {
            mWaveTimer.cancel();
        }
        if (mAnimTimer != null && isAnimRunning) {
            mAnimTimer.cancel();
        }
    }

    //设置温度，入口的开始
    public void setupTemperature(float temperature) {
        mCurPercent = 0f;
        totalAngle = (temperature / 100) * mSweepAngle;
        targetAngle = 0f;
        mCurPercent = 0f;
        mCurTemperature = "0.0";
        mWaveUpValue = 0;

        startTimerAnim();
    }

    //使用定时任务做动画
    private void startTimerAnim() {

        if (isAnimRunning) {
            return;
        }

        mAnimTimer = new Timer();
        mAnimTimer.schedule(new TimerTask() {

            @Override
            public void run() {

                isAnimRunning = true;
                targetAngle += slow[goIndex];
                goIndex++;
                if (goIndex == slow.length) {
                    goIndex--;
                }
                if (targetAngle >= totalAngle) {
                    targetAngle = totalAngle;
                    isAnimRunning = false;
                    mAnimTimer.cancel();
                }

                // 计算的温度
                mCurPercent = targetAngle / mSweepAngle;
                mCurTemperature = mDecimalFormat.format(mCurPercent * 100);

                // 水波纹的高度
                mWaveUpValue = (int) (mCurPercent * (mSmallRadius * 2));

                postInvalidate();
            }
        }, 250, 30);

    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //获取控件的宽度，高度
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int newWidthMeasureSpec = widthMeasureSpec;

        //如果没有指定宽度，默认给200宽度
        if (widthMode != MeasureSpec.EXACTLY) {
            newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY);
        }

        //获取到最新的宽度
        int width = MeasureSpec.getSize(newWidthMeasureSpec) - getPaddingLeft() - getPaddingRight();

        //我们要的是矩形，不管高度是多高，让它总是和宽度一致
        int height = width;

        centerPosition.x = width / 2;
        centerPosition.y = height / 2;
        radius = width / 2f;
        mSmallRadius = radius - 45f;
        mRectF.set(0f, 0f, width, height);
        mSmalloval = new RectF(mRectF.left + 45, mRectF.top + 45, mRectF.right - 45, mRectF.bottom - 45);

        mFirstWaterLine = new float[width];
        mSecondWaterLine = new float[width];

        //最后设置生效-下面两种方式都可以
        // setMeasuredDimension(width, height);

        super.onMeasure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        );

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画圆环
        canvas.drawArc(
                mRectF.left + 2f, mRectF.top + 2f, mRectF.right - 2f, mRectF.bottom - 2f,
                mStartAngle, mSweepAngle, false, mDegreeCirPaint
        );

        //画刻度线
        drawDegreeLine(canvas);

        int evaluateColor = evaluateColor(mCurPercent, Color.GREEN, Color.RED);
        //画小圆
        drawSmallCircle(canvas, evaluateColor);

        //画出水波纹
        drawWaterWave(canvas, evaluateColor);

        //画中心的圆与文本
        drawTemperatureText(canvas);

    }

    /**
     * 绘制水波
     */
    private void drawWaterWave(Canvas canvas, int color) {

        int len = (int) mRectF.right;

        // 将周期定为view总宽度
        float mCycleFactorW = (float) (2 * Math.PI / len);

        // 得到第一条波的峰值
        for (int i = 0; i < len; i++) {
            mFirstWaterLine[i] = (float) (10 * Math.sin(mCycleFactorW * i + mWaveMoveValue) - mWaveUpValue);
        }
        // 得到第一条波的峰值
        for (int i = 0; i < len; i++) {
            mSecondWaterLine[i] = (float) (15 * Math.sin(mCycleFactorW * i + mWaveMoveValue + 10) - mWaveUpValue);
        }

        canvas.save();

        // 裁剪成圆形区域
        Path path = new Path();
        path.addCircle(len / 2f, len / 2f, mSmallRadius, Path.Direction.CCW);
        canvas.clipPath(path);
        path.reset();

        // 将坐标系移到底部
        canvas.translate(0, centerPosition.y + mSmallRadius);

        mSmallCirclePaint.setColor(color);

        for (int i = 0; i < len; i++) {
            canvas.drawLine(i, mFirstWaterLine[i], i, len, mSmallCirclePaint);
        }
        for (int i = 0; i < len; i++) {
            canvas.drawLine(i, mSecondWaterLine[i], i, len, mSmallCirclePaint);
        }

        canvas.restore();

    }

    private void drawSmallCircle(Canvas canvas, int evaluateColor) {
        //中心的小圆
        mSmallCirclePaint.setColor(evaluateColor);
        mSmallCirclePaint.setAlpha(65);
        canvas.drawCircle(centerPosition.x, centerPosition.y, mSmallRadius, mSmallCirclePaint);

        //画个小圆边框
        canvas.drawArc(mSmalloval, 0, 360, false, mSmallCircleBorderPaint);
    }

    private void drawTemperatureText(Canvas canvas) {

        //提示文字
        mTextPaint.setTextSize(mSmallRadius / 6f);
        canvas.drawText("当前温度", centerPosition.x, centerPosition.y - mSmallRadius / 2f, mTextPaint);

        //温度文字
        mTextPaint.setTextSize(mSmallRadius / 2f);
        canvas.drawText(mCurTemperature, centerPosition.x, centerPosition.y + mSmallRadius / 4f, mTextPaint);

        //绘制单位
        mTextPaint.setTextSize(mSmallRadius / 6f);
        canvas.drawText("°C", centerPosition.x + (mSmallRadius / 1.5f), centerPosition.y, mTextPaint);

    }


    private void drawDegreeLine(Canvas canvas) {

        //先保存
        canvas.save();

        // 移动画布
        canvas.translate(radius, radius);
        // 旋转坐标系,需要确定旋转角度
        canvas.rotate(30);

        // 每次旋转的角度
        float rotateAngle = mSweepAngle / 100;
        // 累计叠加的角度
        float currentAngle = 0;

        for (int i = 0; i <= 100; i++) {

            if (currentAngle <= targetAngle && targetAngle != 0) {
                // 计算累计划过的刻度百分比
                float percent = currentAngle / mSweepAngle;

                //动态的设置颜色
                int evaluateColor = evaluateColor(percent, Color.GREEN, Color.RED);
                mDegreelinePaint.setColor(evaluateColor);

                canvas.drawLine(0, radius, 0, radius - 20, mDegreelinePaint);

                // 画过的角度进行叠加
                currentAngle += rotateAngle;

            } else {
                mDegreelinePaint.setColor(Color.WHITE);
                canvas.drawLine(0, radius, 0, radius - 20, mDegreelinePaint);
            }

            //画完一个刻度就要旋转移动位置
            canvas.rotate(rotateAngle);
        }

        //再恢复
        canvas.restore();

    }


    //百分比计算颜色
    private int evaluateColor(float fraction, int startValue, int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return ((startA + (int) (fraction * (endA - startA))) << 24) |
                ((startR + (int) (fraction * (endR - startR))) << 16) |
                ((startG + (int) (fraction * (endG - startG))) << 8) |
                ((startB + (int) (fraction * (endB - startB))));
    }
}
