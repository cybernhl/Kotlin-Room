package com.google.android.material.appbar;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Transformation;

import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.guadou.kt_demo.R;


/**
 * Created by Administrator on 2017/8/14.
 */

public class EasyBehavior extends CoordinatorLayout.Behavior<View> {

    private static final int UNSPECIFIED_INT = Integer.MAX_VALUE;
    private static final float UNSPECIFIED_FLOAT = Float.MAX_VALUE;

    /**
     * Depend on the dependency view height
     */
    private static final int DEPEND_TYPE_HEIGHT = 0;

    /**
     * Depend on the dependency view width
     */
    private static final int DEPEND_TYPE_WIDTH = 1;

    /**
     * Depend on the dependency view x position
     */
    private static final int DEPEND_TYPE_X = 2;

    /**
     * Depend on the dependency view y position
     */
    private static final int DEPEND_TYPE_Y = 3;

    private int mDependType;
    private int mDependViewId;

    private int mDependTargetX;
    private int mDependTargetY;
    private int mDependTargetWidth;
    private int mDependTargetHeight;

    private int mDependStartX;
    private int mDependStartY;
    private int mDependStartWidth;
    private int mDependStartHeight;

    private int mStartX;
    private int mStartY;
    private int mStartWidth;
    private int mStartHeight;
    private int mStartBackgroundColor;
    private float mStartAlpha;
    private float mStartRotateX;
    private float mStartRotateY;

    private int mTargetX;
    private int mTargetY;
    private int mTargetWidth;
    private int mTargetHeight;
    private int mTargetBackgroundColor;
    private float mTargetAlpha;
    private float mTargetRotateX;
    private float mTargetRotateY;

    private int mAnimationId;
    private Animation mAnimation;

    /**
     * Is the values prepared to be use
     */
    private boolean isPrepared;

    /**
     * Creates a new behavior whose parameters come from the specified context and
     * attributes set.
     *
     * @param context the application environment
     * @param attrs   the set of attributes holding the target and animation parameters
     */
    public EasyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);

        // setting values
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EasyCoordinatorView);
        mDependViewId = a.getResourceId(R.styleable.EasyCoordinatorView_dependsOn, 0);
        mDependType = a.getInt(R.styleable.EasyCoordinatorView_dependType, DEPEND_TYPE_WIDTH);
        mDependTargetX = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_dependTargetX, UNSPECIFIED_INT);
        mDependTargetY = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_dependTargetY, UNSPECIFIED_INT);
        mDependTargetWidth = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_dependTargetWidth, UNSPECIFIED_INT);
        mDependTargetHeight = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_dependTargetHeight, UNSPECIFIED_INT);
        mTargetX = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_targetX, UNSPECIFIED_INT);
        mTargetY = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_targetY, UNSPECIFIED_INT);
        mTargetWidth = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_targetWidth, UNSPECIFIED_INT);
        mTargetHeight = a.getDimensionPixelOffset(R.styleable.EasyCoordinatorView_targetHeight, UNSPECIFIED_INT);
        mTargetBackgroundColor = a.getColor(R.styleable.EasyCoordinatorView_targetBackgroundColor, UNSPECIFIED_INT);
        mTargetAlpha = a.getFloat(R.styleable.EasyCoordinatorView_targetAlpha, UNSPECIFIED_FLOAT);
        mTargetRotateX = a.getFloat(R.styleable.EasyCoordinatorView_targetRotateX, UNSPECIFIED_FLOAT);
        mTargetRotateY = a.getFloat(R.styleable.EasyCoordinatorView_targetRotateY, UNSPECIFIED_FLOAT);
        mAnimationId = a.getResourceId(R.styleable.EasyCoordinatorView_animation, 0);
        a.recycle();
    }

    /**
     * 获取当前依赖控件的值与自己的值，
     * 在更改之前调用此方法
     */
    private void prepare(CoordinatorLayout parent, View child, View dependency) {
        mDependStartX = (int) dependency.getX();
        mDependStartY = (int) dependency.getY();
        mDependStartWidth = dependency.getWidth();
        mDependStartHeight = dependency.getHeight();
        mStartX = (int) child.getX();
        mStartY = (int) child.getY();
        mStartWidth = child.getWidth();
        mStartHeight = child.getHeight();
        mStartAlpha = child.getAlpha();
        mStartRotateX = child.getRotationX();
        mStartRotateY = child.getRotationY();

        // background color
        if (child.getBackground() instanceof ColorDrawable) {
            mStartBackgroundColor = ((ColorDrawable) child.getBackground()).getColor();
        }

        // if there is animation id, load it and initialize
        if (mAnimationId != 0) {
            mAnimation = AnimationUtils.loadAnimation(child.getContext(), mAnimationId);
            mAnimation.initialize(child.getWidth(), child.getHeight(), parent.getWidth(), parent.getHeight());
        }

        // if parent fitsSystemWindows is true, add status bar height to target y if specified
        if (Build.VERSION.SDK_INT > 16 && parent.getFitsSystemWindows() && mTargetY != UNSPECIFIED_INT) {
            int result = 0;
            int resourceId = parent.getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = parent.getContext().getResources().getDimensionPixelSize(resourceId);
            }
            mTargetY += result;
        }

        isPrepared = true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        //关键代码-指定依赖于哪一个控件
        return dependency.getId() == mDependViewId;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //当依赖控件改变是，当前View需要的变化
        if (!isPrepared) {
            prepare(parent, child, dependency);
        }
        updateView(child, dependency);
        return false;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        if (isPrepared) {
            updateView(child, parent.getDependencies(child).get(0));
        }
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    /**
     * 当依赖控件改变时候，当前View的变化
     */
    public void updateView(View child, View dependency) {
        float percent = 0;
        float start = 0;
        float current = 0;
        float end = UNSPECIFIED_INT;
        switch (mDependType) {
            case DEPEND_TYPE_WIDTH:
                //如果指定依赖类型为宽度变化
                start = mDependStartWidth;
                current = dependency.getWidth();
                end = mDependTargetWidth;
                break;
            case DEPEND_TYPE_HEIGHT:
                //如果指定依赖类型为高度变化
                start = mDependStartHeight;
                current = dependency.getHeight();
                end = mDependTargetHeight;
                break;
            case DEPEND_TYPE_X:
                //如果指定依赖类型为X变化
                start = mDependStartX;
                current = dependency.getX();
                end = mDependTargetX;
                break;
            case DEPEND_TYPE_Y:
                //如果指定依赖类型为Y变化
                start = mDependStartY;
                current = dependency.getY();
                end = mDependTargetY;
                break;
        }

        // need to define target value according to the depend type, if not then skip
        if (end != UNSPECIFIED_INT) {
            percent = Math.abs(current - start) / Math.abs(end - start);
        }
        updateViewWithPercent(child, percent > 1 ? 1 : percent);
    }

    /**
     * 根据百分比进度更新当前View的变化
     */
    public void updateViewWithPercent(View child, float percent) {
        // if there is no animation set, use the attr options
        if (mAnimation == null) {
            float newX = mTargetX == UNSPECIFIED_INT ? 0 : (mTargetX - mStartX) * percent;
            float newY = mTargetY == UNSPECIFIED_INT ? 0 : (mTargetY - mStartY) * percent;

            // set scale
            if (mTargetWidth != UNSPECIFIED_INT || mTargetHeight != UNSPECIFIED_INT) {
                float newWidth = mStartWidth + ((mTargetWidth - mStartWidth) * percent);
                float newHeight = mStartHeight + ((mTargetHeight - mStartHeight) * percent);

                child.setScaleX(newWidth / mStartWidth);
                child.setScaleY(newHeight / mStartHeight);
                // make up position for scale change
                newX -= (mStartWidth - newWidth) / 2;
                newY -= (mStartHeight - newHeight) / 2;
            }

            // set new position
            child.setTranslationX(newX);
            child.setTranslationY(newY);

            // set alpha
            if (mTargetAlpha != UNSPECIFIED_FLOAT) {
                child.setAlpha(mStartAlpha + (mTargetAlpha - mStartAlpha) * percent);
            }

            // set background color
            if (mTargetBackgroundColor != UNSPECIFIED_INT && mStartBackgroundColor != 0) {
                ArgbEvaluator evaluator = new ArgbEvaluator();
                int color = (int) evaluator.evaluate(percent, mStartBackgroundColor, mTargetBackgroundColor);
                child.setBackgroundColor(color);
            }

            // set rotation
            if (mTargetRotateX != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateX + (mTargetRotateX - mStartRotateX) * percent);
            }
            if (mTargetRotateY != UNSPECIFIED_FLOAT) {
                child.setRotationX(mStartRotateY + (mTargetRotateY - mStartRotateY) * percent);
            }
        } else {
            // get the transform at the specify time in progress
            mAnimation.setStartTime(0);
            mAnimation.restrictDuration(100);
            Transformation transformation = new Transformation();
            mAnimation.getTransformation((long) (percent * 100), transformation);
            // start new animation from transformation we got
            BehaviorAnimation animation = new BehaviorAnimation(transformation);
            child.startAnimation(animation);
        }

        child.requestLayout();
    }

    /**
     * Custom animation class to apply Transformation to the view
     */
    private static class BehaviorAnimation extends Animation {

        private Transformation mTransformation;

        public BehaviorAnimation(Transformation transformation) {
            mTransformation = transformation;
            // always set duration to zero and fill after
            setDuration(0);
            setFillAfter(true);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            t.compose(mTransformation);
            super.applyTransformation(interpolatedTime, t);
        }
    }
}
