package com.guadou.kt_demo.demo.demo18_customview.star;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.guadou.kt_demo.R;


/**
 * 星星评分控件
 */
public class StarScoreView extends View {

    private int mStarDistance = 5;
    private int mStarCount = 5;
    private int mStarSize = 20;    //每一个星星的宽度和高度是一致的
    private float mScoreNum = 0.0F;  //当前的评分值
    private Drawable mStarScoredDrawable;  //已经评分的星星图片
    private Drawable mStarUnscoredDrawable;  //还未评分的星星图片
    private boolean isOnlyIntegerScore = false;  //默认显示小数类型
    private boolean isCanTouch = true; //默认支持控件的点击
    private OnStarChangeListener onStarChangeListener;
    private Paint paint;
    private int mMeasuredWidth;

    public StarScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StarScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化UI组件
     */
    @SuppressLint("UseCompatLoadingForDrawables")
    private void init(Context context, AttributeSet attrs) {
        setClickable(true);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.StarScoreView);
        this.mStarDistance = mTypedArray.getDimensionPixelSize(R.styleable.StarScoreView_starDistance, 0);
        this.mStarSize = mTypedArray.getDimensionPixelSize(R.styleable.StarScoreView_starSize, 20);
        this.mStarCount = mTypedArray.getInteger(R.styleable.StarScoreView_starCount, 5);
        this.mStarUnscoredDrawable = mTypedArray.getDrawable(R.styleable.StarScoreView_starUnscoredDrawable);
        this.mStarScoredDrawable = mTypedArray.getDrawable(R.styleable.StarScoreView_starScoredDrawable);
        this.isOnlyIntegerScore = mTypedArray.getBoolean(R.styleable.StarScoreView_starIsTouchEnable, true);
        this.isOnlyIntegerScore = mTypedArray.getBoolean(R.styleable.StarScoreView_starIsOnlyIntegerScore, false);
        mTypedArray.recycle();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(new BitmapShader(drawableToBitmap(mStarScoredDrawable), BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
    }

    /**
     * 设置显示的星星的分数
     */
    public void setStarMark(float mark) {
        if (isOnlyIntegerScore) {
            mScoreNum = (int) Math.ceil(mark);
        } else {
            mScoreNum = Math.round(mark * 10) * 1.0f / 10;
        }
        if (this.onStarChangeListener != null) {
            this.onStarChangeListener.onStarChange(mScoreNum);  //调用监听接口
        }
        invalidate();
    }

    /**
     * 设置监听
     */
    public void setOnStarChangeListener(OnStarChangeListener onStarChangeListener) {
        this.onStarChangeListener = onStarChangeListener;
    }

    /**
     * 定义星星点击的监听接口
     */
    public interface OnStarChangeListener {
        void onStarChange(float mark);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mMeasuredWidth = mStarSize * mStarCount + mStarDistance * (mStarCount - 1);
        setMeasuredDimension(mMeasuredWidth, mStarSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mStarUnscoredDrawable == null || mStarScoredDrawable == null) {
            return;
        }

        for (int i = 0; i < mStarCount; i++) {
            mStarUnscoredDrawable.setBounds((mStarDistance + mStarSize) * i, 0, (mStarDistance + mStarSize) * i + mStarSize, mStarSize);
            mStarUnscoredDrawable.draw(canvas);
        }

        if (mScoreNum > 1) {
            canvas.drawRect(0, 0, mStarSize, mStarSize, paint);

            if (mScoreNum - (int) (mScoreNum) == 0) {
                //如果评分是3.0之类的整数，那么直接按正常的rect绘制
                for (int i = 1; i < mScoreNum; i++) {
                    canvas.translate(mStarDistance + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, paint);
                }
            } else {
                //如果是小数例如3.5，先绘制之前的3个，再绘制后面的0.5
                for (int i = 1; i < mScoreNum - 1; i++) {
                    canvas.translate(mStarDistance + mStarSize, 0);
                    canvas.drawRect(0, 0, mStarSize, mStarSize, paint);
                }
                canvas.translate(mStarDistance + mStarSize, 0);
                canvas.drawRect(0, 0, mStarSize * (Math.round((mScoreNum - (int) (mScoreNum)) * 10) * 1.0f / 10), mStarSize, paint);
            }

        } else {
            canvas.drawRect(0, 0, mStarSize * mScoreNum, mStarSize, paint);
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isCanTouch) {

            //x轴的宽度做一下最大最小的限制
            int x = (int) event.getX();
            if (x < 0) {
                x = 0;
            }
            if (x > mMeasuredWidth) {
                x = mMeasuredWidth;
            }

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE: {
                    setStarMark(x * 1.0f / (getMeasuredWidth() * 1.0f / mStarCount));
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    break;
                }
            }

            return super.onTouchEvent(event);

        } else {
            //如果设置不能点击，直接不触发事件
            return false;
        }

    }

    /**
     * drawable转bitmap
     */
    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) return null;
        Bitmap bitmap = Bitmap.createBitmap(mStarSize, mStarSize, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, mStarSize, mStarSize);
        drawable.draw(canvas);
        return bitmap;
    }
}

