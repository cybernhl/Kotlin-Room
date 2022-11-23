package com.guadou.kt_demo.demo.demo18_customview.range;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.guadou.kt_demo.R;


/**
 * 区间选择价格
 */
public class RangeView extends View {

    private int mRangLineHeight = getResources().getDimensionPixelSize(R.dimen.d_4dp);  //圆角矩形线的高度
    private int mRangLineDefaultColor = Color.parseColor("#CDCDCD");  //默认颜色
    private int mRangLineCheckedColor = Color.parseColor("#0689FD");  //选中颜色

    private int mCircleRadius = getResources().getDimensionPixelSize(R.dimen.d_14dp); //圆半径
    private int mCircleStrokeWidth = getResources().getDimensionPixelSize(R.dimen.d_1d5dp); //圆边框的大小

    private int mLeftCircleBGColor = Color.parseColor("#0689FD");  //左边实心圆颜色
    private int mLeftCircleStrokeColor = Color.parseColor("#FFFFFF");  //左边圆边框的颜色

    private int mRightCircleBGColor = Color.parseColor("#0689FD");  //右边实心圆颜色
    private int mRightCircleStrokeColor = Color.parseColor("#FFFFFF");  //右边圆边框的颜色

    private int mTopDialogTextSize = getResources().getDimensionPixelSize(R.dimen.d_12dp);  //顶部文字的大小
    private int mTopDialogTextColor = Color.parseColor("#000000");  //顶部文字的颜色
    private int mTopDialogWidth = getResources().getDimensionPixelSize(R.dimen.d_70dp);  //顶部描述信息弹窗的宽度
    private int mTopDialogCornerRadius = getResources().getDimensionPixelSize(R.dimen.d_15dp);  //顶部描述信息弹窗圆角半径
    private int mTopDialogBGColor = Color.parseColor("#0689FD");  //顶部框的颜色
    private int mTopDialogSpaceToProgress = getResources().getDimensionPixelSize(R.dimen.d_2dp); //顶部描述信息弹窗距离进度条的间距(配置)

    private int mRealDialogDistanceSpace;  //顶部弹窗与进度条的间距（顶部弹窗与进度的真正距离）计算得出
    private int mRangLineCornerRadius;     //圆角矩形线的圆角半径，计算得出

    private float mLeftCircleCenterX;    //左右两个圆心位置
    private float mLeftCircleCenterY;
    private float mRightCircleCenterX;
    private float mRightCircleCenterY;

    private RectF mDefaultCornerLineRect;      //默认颜色的圆角矩形
    private RectF mSelectedCornerLineRect;     //选中颜色的圆角矩形
    private RectF mTopDialogRect;      //顶部表示具体数值的矩形

    private Path mTrianglePath;     //画小三角形路径
    private int mTriangleLength = 15;  //等边三角形边长
    private int mTriangleHeight;     //等边三角形的高

    private Paint mLeftCirclePaint;        //各种画笔
    private Paint mLeftCircleStrokePaint;
    private Paint mRightCirclePaint;
    private Paint mRightCircleStrokePaint;
    private Paint mDefaultLinePaint;
    private Paint mSelectedLinePaint;
    private Paint textPaint;

    private int mStrokeRadius;  //半径+边框的总值

    private int slice = 5; //代表整体进度分为多少份
    private float perSlice;   //每一份所占的长度
    private int maxValue = 100;  //最大值，默认为100
    private int minValue = 0;    //最小值，默认为0
    private int leftValue;  //当前左边数值，
    private int rightValue;  //当前右边数值

    private float downX;
    private boolean touchLeftCircle;

    private String textDesc = "0";   //描述信息弹窗中的文字
    private boolean isShowRectDialog = true;  //是否展示顶部弹窗
    private boolean isRectDialogShowing = false;  //是否一直显示弹窗

    public RangeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public RangeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     */
    private void init(Context context, AttributeSet attrs) {

        initPaint();
    }

    /**
     * 设置数据与回调处理
     */
    public void setupData(int minValue, int maxValue, int sliceValue, OnRangeValueListener listener) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.mListener = listener;
        int num = (maxValue - minValue) / sliceValue;
        slice = (maxValue - minValue) % sliceValue == 0 ? num : num + 1;
        invalidate();
    }

    private void initPaint() {

        //初始化左边实心圆
        mLeftCirclePaint = new Paint();
        mLeftCirclePaint.setAntiAlias(true);
        mLeftCirclePaint.setDither(true);
        mLeftCirclePaint.setStyle(Paint.Style.FILL);
        mLeftCirclePaint.setColor(mLeftCircleBGColor);

        //初始化左边圆的边框
        mLeftCircleStrokePaint = new Paint();
        mLeftCircleStrokePaint.setAntiAlias(true);
        mLeftCircleStrokePaint.setDither(true);
        mLeftCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mLeftCircleStrokePaint.setColor(mLeftCircleStrokeColor);
        mLeftCircleStrokePaint.setStrokeWidth(mCircleStrokeWidth);

        //初始化右边实心圆
        mRightCirclePaint = new Paint();
        mRightCirclePaint.setAntiAlias(true);
        mRightCirclePaint.setDither(true);
        mRightCirclePaint.setStyle(Paint.Style.FILL);
        mRightCirclePaint.setColor(mRightCircleBGColor);

        //初始化右边圆的边框
        mRightCircleStrokePaint = new Paint();
        mRightCircleStrokePaint.setAntiAlias(true);
        mRightCircleStrokePaint.setDither(true);
        mRightCircleStrokePaint.setStyle(Paint.Style.STROKE);
        mRightCircleStrokePaint.setColor(mRightCircleStrokeColor);
        mRightCircleStrokePaint.setStrokeWidth(mCircleStrokeWidth);

        //默认颜色的圆角矩形线
        mDefaultCornerLineRect = new RectF();

        //中间选中颜色的圆角矩形
        mSelectedCornerLineRect = new RectF();

        //数值描述圆角矩形
        mTopDialogRect = new RectF();
        //画小三角形
        mTrianglePath = new Path();
        //小三角形的高
        mTriangleHeight = (int) Math.sqrt(mTriangleLength * mTriangleLength - mTriangleLength / 2 * (mTriangleLength / 2));

        mDefaultLinePaint = new Paint();
        mDefaultLinePaint.setAntiAlias(true);
        mDefaultLinePaint.setDither(true);

        mSelectedLinePaint = new Paint();
        mSelectedLinePaint.setAntiAlias(true);
        mSelectedLinePaint.setDither(true);

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setTextSize(mTopDialogTextSize);
        textPaint.setColor(mTopDialogTextColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int finalWidth, finalHeight;

        //计算的宽度与高度
        int calWidthSize = getPaddingLeft() + mCircleRadius * 2 + getPaddingRight() + mCircleStrokeWidth * 2;
        int calHeightSize = getPaddingTop() + mTopDialogCornerRadius * 2 + mTriangleHeight + mTopDialogSpaceToProgress
                + mCircleRadius * 2 + mCircleStrokeWidth * 2 + getPaddingBottom();

        if (widthMode == MeasureSpec.EXACTLY) {
            //如果是精确模式使用测量的宽度
            finalWidth = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //如果是WrapContent使用计算的宽度
            finalWidth = Math.min(widthSize, calWidthSize);
        } else {
            //其他模式使用计算的宽度
            finalWidth = calWidthSize;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            //如果是精确模式使用测量的高度
            finalHeight = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //如果是WrapContent使用计算的高度
            finalHeight = Math.min(heightSize, calHeightSize);
        } else {
            //其他模式使用计算的高度
            finalHeight = calHeightSize;
        }

        //确定测量宽高
        setMeasuredDimension(finalWidth, finalHeight);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int realWidth = w - getPaddingLeft() - getPaddingRight();
        mStrokeRadius = mCircleRadius + mCircleStrokeWidth;

        mRealDialogDistanceSpace = mTopDialogCornerRadius * 2 + mTopDialogSpaceToProgress;

        //计算每一份对应的距离
        perSlice = (realWidth - mStrokeRadius * 2) * 1f / slice;

        //左边圆的圆心坐标
        mLeftCircleCenterX = getPaddingLeft() + mStrokeRadius;
        mLeftCircleCenterY = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius;

        //右边圆的圆心坐标
        mRightCircleCenterX = w - getPaddingRight() - mStrokeRadius;
        mRightCircleCenterY = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius;

        //默认圆角矩形进度条
        mRangLineCornerRadius = mRangLineHeight / 2;//圆角半径
        mDefaultCornerLineRect.left = getPaddingLeft() + mStrokeRadius;
        mDefaultCornerLineRect.top = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius - mRangLineCornerRadius;
        mDefaultCornerLineRect.right = w - getPaddingRight() - mStrokeRadius;
        mDefaultCornerLineRect.bottom = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius + mRangLineCornerRadius;

        //选中状态圆角矩形进度条
        mSelectedCornerLineRect.left = mLeftCircleCenterX;
        mSelectedCornerLineRect.top = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius - mRangLineCornerRadius;
        mSelectedCornerLineRect.right = mRightCircleCenterX;
        mSelectedCornerLineRect.bottom = getPaddingTop() + mRealDialogDistanceSpace + mStrokeRadius + mRangLineCornerRadius;

        //数值描述圆角矩形
        mTopDialogRect.left = w / 2 - mTopDialogWidth / 2;
        mTopDialogRect.top = getPaddingTop();
        mTopDialogRect.right = w / 2 + mTopDialogWidth / 2;
        mTopDialogRect.bottom = getPaddingTop() + mTopDialogCornerRadius * 2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //绘制中间圆角矩形线
        drawDefaultCornerRectLine(canvas);

        //绘制两圆之间已经选中的的圆角矩形
        drawSelectedRectLine(canvas);

        //画左边圆以及圆的边框
        drawLeftCircle(canvas);

        //画右边圆以及圆的边框
        drawRightCircle(canvas);

        //绘制描述信息弹窗以及文字
        drawTopTextRectDialog(canvas);

        //绘制小三角形
        drawSmallTriangle(canvas);
    }

    //顶部文字框下面的三角箭头
    private void drawSmallTriangle(Canvas canvas) {
        if (isShowRectDialog) {
            mTrianglePath.reset();
            mTrianglePath.moveTo(mTopDialogRect.left + mTopDialogWidth / 2 - mTriangleLength / 2, getPaddingTop() + mTopDialogCornerRadius * 2);
            mTrianglePath.lineTo(mTopDialogRect.left + mTopDialogWidth / 2 + mTriangleLength / 2, getPaddingTop() + mTopDialogCornerRadius * 2);
            mTrianglePath.lineTo(mTopDialogRect.left + mTopDialogWidth / 2, getPaddingTop() + mTopDialogCornerRadius * 2 + mTriangleHeight);
            mTrianglePath.close();
            canvas.drawPath(mTrianglePath, mSelectedLinePaint);
        }
    }

    //顶部的文字框
    private void drawTopTextRectDialog(Canvas canvas) {
        if (leftValue == minValue && (rightValue == maxValue || rightValue < maxValue)) {
            textDesc = "低于 " + rightValue;
        } else if (leftValue > minValue && rightValue == maxValue) {
            textDesc = "高于 " + leftValue;
        } else if (leftValue > minValue && rightValue < maxValue) {
            if (leftValue == rightValue) {
                textDesc = "低于 " + rightValue;
            } else
                textDesc = leftValue + "-" + rightValue;
        }


        if (isShowRectDialog) {
            //绘制圆角矩形框
            mSelectedLinePaint.setShader(null);
            mSelectedLinePaint.setColor(mTopDialogBGColor);
            canvas.drawRoundRect(mTopDialogRect, mTopDialogCornerRadius, mTopDialogCornerRadius, mSelectedLinePaint);

            //绘制文本
            textPaint.setColor(mTopDialogTextColor);
            textPaint.setTextSize(mTopDialogTextSize);
            float textWidth = textPaint.measureText(textDesc);
            float textLeft = mTopDialogRect.left + mTopDialogWidth / 2 - textWidth / 2;
            canvas.drawText(textDesc, textLeft, getPaddingTop() + mTopDialogCornerRadius + mTopDialogTextSize / 4, textPaint);
        }
    }

    //左侧的控制圆与边框
    private void drawLeftCircle(Canvas canvas) {
        //实心圆
        canvas.drawCircle(mLeftCircleCenterX, mLeftCircleCenterY, mCircleRadius, mLeftCirclePaint);
        //空心圆
        canvas.drawCircle(mLeftCircleCenterX, mLeftCircleCenterY, mCircleRadius, mLeftCircleStrokePaint);
    }

    //右侧的控制圆与边框
    private void drawRightCircle(Canvas canvas) {
        //实心圆
        canvas.drawCircle(mRightCircleCenterX, mRightCircleCenterY, mCircleRadius, mRightCirclePaint);
        //空心圆
        canvas.drawCircle(mRightCircleCenterX, mRightCircleCenterY, mCircleRadius, mRightCircleStrokePaint);
    }

    //中心的圆角矩形进度条-默认的底色
    private void drawDefaultCornerRectLine(Canvas canvas) {
        mDefaultLinePaint.setColor(mRangLineDefaultColor);
        canvas.drawRoundRect(mDefaultCornerLineRect, mRangLineCornerRadius, mRangLineCornerRadius, mDefaultLinePaint);
    }

    //中心的圆角矩形进度条-已经选中的颜色
    private void drawSelectedRectLine(Canvas canvas) {
        mSelectedLinePaint.setColor(mRangLineCheckedColor);
        canvas.drawRoundRect(mSelectedCornerLineRect, mRangLineCornerRadius, mRangLineCornerRadius, mSelectedLinePaint);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            //按下的时候记录当前操作的是左侧限制圆还是右侧的限制圆
            downX = event.getX();
            touchLeftCircle = checkTouchCircleLeftOrRight(downX);

            if (touchLeftCircle) {
                //如果是左侧
                //如果超过右侧最大值则不处理
                if (downX + perSlice > mRightCircleCenterX) {
                    return false;
                }

                mLeftCircleCenterX = downX;
            } else {
                //如果是右侧
                //如果超过左侧最小值则不处理
                if (downX - perSlice < mLeftCircleCenterX) {
                    return false;
                }

                mRightCircleCenterX = downX;
            }

        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {

            float moveX = event.getX();
            isShowRectDialog = true;

            if (mLeftCircleCenterX + perSlice > mRightCircleCenterX) {
                //两圆重合的情况下的处理
                if (touchLeftCircle) {
                    // 左侧到最右边
                    if (mLeftCircleCenterX == getWidth() - getPaddingRight() - mStrokeRadius) {
                        touchLeftCircle = true;
                        mLeftCircleCenterX = getWidth() - getPaddingRight() - mStrokeRadius;

                    } else {
                        //交换右侧滑动
                        touchLeftCircle = false;
                        mRightCircleCenterX = moveX;
                    }
                } else {
                    //右侧到最左边
                    if (mRightCircleCenterX == getPaddingLeft() + mStrokeRadius) {
                        touchLeftCircle = false;
                        mRightCircleCenterX = getPaddingLeft() + mStrokeRadius;

                    } else {
                        //交换左侧滑动
                        touchLeftCircle = true;
                        mLeftCircleCenterX = moveX;
                    }
                }

            } else {
                //如果是正常的移动
                if (touchLeftCircle) {
                    //滑动左边限制圆，如果左边圆超过右边圆，那么把右边圆赋值给左边圆，如果没超过就赋值当前的moveX
                    mLeftCircleCenterX = mLeftCircleCenterX - mRightCircleCenterX >= 0 ? mRightCircleCenterX : moveX;
                } else {
                    //滑动右边限制圆，如果右边圆超过左边圆，那么把左边圆赋值给右边圆，如果没超过就赋值当前的moveX
                    mRightCircleCenterX = mRightCircleCenterX - mLeftCircleCenterX <= 0 ? mLeftCircleCenterX : moveX;
                }

            }

            //计算当前的左侧右侧真正的限制值
            int moveLeftData = getPercentMax(mLeftCircleCenterX - getPaddingLeft() - mStrokeRadius);
            int moveRightData = getPercentMax(mRightCircleCenterX - getPaddingLeft() - mStrokeRadius);
            //顺便赋值当前的真正值,可以让顶部文字显示
            leftValue = Math.min(moveLeftData, maxValue);
            rightValue = Math.min(moveRightData, maxValue);

        } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {

            //计算当前的左侧右侧真正的限制值
            int moveLeftData = getPercentMax(mLeftCircleCenterX - getPaddingLeft() - mStrokeRadius);
            int moveRightData = getPercentMax(mRightCircleCenterX - getPaddingLeft() - mStrokeRadius);
            //赋值方便回调
            leftValue = Math.min(moveLeftData, maxValue);
            rightValue = Math.min(moveRightData, maxValue);

            if (mListener != null) mListener.onMoveValue(leftValue, rightValue);

            //移除消息，并重新开始延时关闭顶部弹窗
            removeCallbacks(dismissRunnable);
            postDelayed(dismissRunnable, 1000);
        }

        //对所有的手势效果进行过滤操作，不能超过最大最小值
        limitMinAndMax();

        //中间的进度矩形是根据两边圆心点动态计算的
        mSelectedCornerLineRect.left = mLeftCircleCenterX;
        mSelectedCornerLineRect.right = mRightCircleCenterX;

        //顶部的文本框矩阵也要居中布局
        mTopDialogRect.left = (mRightCircleCenterX + mLeftCircleCenterX) / 2 - mTopDialogWidth / 2;
        mTopDialogRect.right = (mRightCircleCenterX + mLeftCircleCenterX) / 2 + mTopDialogWidth / 2;

        //全部的事件处理完毕，变量赋值完成之后，开始重绘
        invalidate();

        return true;
    }

    //顶部弹窗的显示
    Runnable dismissRunnable = new Runnable() {
        @Override
        public void run() {
            if (!isRectDialogShowing) {
                isShowRectDialog = false;
            }
            postInvalidate();
        }
    };

    private void limitMinAndMax() {
        //如果是操作的左侧限制圆，超过最小值了
        if (mLeftCircleCenterX < getPaddingLeft() + mStrokeRadius) {
            mLeftCircleCenterX = getPaddingLeft() + mStrokeRadius;
        }

        //如果是操作的左侧限制圆，超过最大值了
        if (mLeftCircleCenterX > getWidth() - getPaddingRight() - mStrokeRadius) {
            mLeftCircleCenterX = getWidth() - getPaddingRight() - mStrokeRadius;
        }

        //如果是操作的右侧限制圆，超过最大值了
        if (mRightCircleCenterX > getWidth() - getPaddingRight() - mStrokeRadius) {
            mRightCircleCenterX = getWidth() - getPaddingRight() - mStrokeRadius;
        }

        //如果是操作的右侧限制圆，超过最小值了
        if (mRightCircleCenterX < getPaddingLeft() + mStrokeRadius) {
            mRightCircleCenterX = getPaddingLeft() + mStrokeRadius;
        }

    }

    //根据移动的距离计算当前的值
    private int getPercentMax(float distance) {
        //计算此时的位置坐标对应的距离能分多少份
        int lineLength = getWidth() - getPaddingLeft() - getPaddingRight() - mStrokeRadius * 2;
        distance = distance <= 0 ? 0 : (distance >= lineLength ? lineLength : distance);

        //计算滑动的百分比
        float percentage = distance / lineLength;

        return (int) (percentage * maxValue);

    }

    /**
     * 判断当前移动的是左侧限制圆，还是右侧限制圆
     *
     * @param downX 按下的坐标点
     * @return true表示按下的左侧，false表示按下的右侧
     */
    private boolean checkTouchCircleLeftOrRight(float downX) {
        //用一个取巧的方法，如果当前按下的为X坐标，那么左侧圆心X的坐标减去按下的X坐标，如果大于右侧的圆心X减去X坐标，那么就说明在左侧，否则就在右侧
        return !(Math.abs(mLeftCircleCenterX - downX) - Math.abs(mRightCircleCenterX - downX) > 0);
    }


    //回调区间值的监听
    private OnRangeValueListener mListener;

    public interface OnRangeValueListener {
        void onMoveValue(int leftValue, int rightValue);
    }
}


