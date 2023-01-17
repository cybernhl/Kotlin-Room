package com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.event;

import android.content.Context;
import android.graphics.PointF;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;


/**
 * 处理移动与缩放的通用事件处理类
 */
public class TouchEventHandler {

    private static float MAX_SCALE = 1.5f;  //最大能缩放值
    private static float MIN_SCALE = 0.8f;  //最小能缩放值
    //当前的触摸事件类型
    private static final int TOUCH_MODE_UNSET = -1;
    private static final int TOUCH_MODE_RELEASE = 0;
    private static final int TOUCH_MODE_SINGLE = 1;
    private static final int TOUCH_MODE_DOUBLE = 2;

    private View mView;
    private int mode = 0;
    private float scaleFactor = 1.0f;
    private float scaleBaseR;
    private GestureDetector mGestureDetector;
    private float mTouchSlop;
    private MotionEvent preMovingTouchEvent = null;
    private MotionEvent preInterceptTouchEvent = null;
    private boolean mIsMoving;
    private float minScale = MIN_SCALE;
    private FlingAnimation flingY = null;
    private FlingAnimation flingX = null;

    private ViewBox layoutLocationInParent = new ViewBox();  //移动中不断变化的盒模型
    private final ViewBox viewportBox = new ViewBox();   //初始化的盒模型
    private PointF preFocusCenter = new PointF();
    private PointF postFocusCenter = new PointF();
    private PointF preTranslate = new PointF();
    private float preScaleFactor = 1f;
    private final DynamicAnimation.OnAnimationUpdateListener flingAnimateListener;
    private boolean isKeepInViewport = false;  //是否只能框架内移动
    private boolean enableScale;  //是否可以支持缩放
    private TouchEventListener controlListener = null;
    private int scalePercentOnlyForControlListener = 0;

    public TouchEventHandler(Context context, View view) {
        this.mView = view;
        flingAnimateListener = (animation, value, velocity) -> keepWithinBoundaries();

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        flingX = new FlingAnimation(mView, DynamicAnimation.TRANSLATION_X);
                        flingX.setStartVelocity(velocityX)
                                .addUpdateListener(flingAnimateListener)
                                .start();

                        flingY = new FlingAnimation(mView, DynamicAnimation.TRANSLATION_Y);
                        flingY.setStartVelocity(velocityY)
                                .addUpdateListener(flingAnimateListener)
                                .start();
                        return false;
                    }
                });
        ViewConfiguration vc = ViewConfiguration.get(view.getContext());
        mTouchSlop = vc.getScaledTouchSlop() * 0.8f;
    }

    /**
     * 设置内部布局视图窗口高度和宽度
     */
    public void setViewport(int winWidth, int winHeight) {
        viewportBox.setValues(0, 0, winWidth, winHeight);
    }

    /**
     * 暴露的方法，内部处理事件并判断是否拦截事件
     */
    public boolean detectInterceptTouchEvent(MotionEvent event) {
        final int action = event.getAction() & MotionEvent.ACTION_MASK;
        onTouchEvent(event);
        if (action == MotionEvent.ACTION_DOWN) {
            preInterceptTouchEvent = MotionEvent.obtain(event);
            mIsMoving = false;
        }
        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mIsMoving = false;
        }
        if (action == MotionEvent.ACTION_MOVE && mTouchSlop < calculateMoveDistance(event, preInterceptTouchEvent)) {
            mIsMoving = true;
        }
        return mIsMoving;
    }

    /**
     * 当前事件的真正处理逻辑
     */
    public boolean onTouchEvent(MotionEvent event) {
        mGestureDetector.onTouchEvent(event);

        int action = event.getAction() & MotionEvent.ACTION_MASK;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mode = TOUCH_MODE_SINGLE;
                preMovingTouchEvent = MotionEvent.obtain(event);

                if (flingX != null) {
                    flingX.cancel();
                }
                if (flingY != null) {
                    flingY.cancel();
                }
                break;
            case MotionEvent.ACTION_UP:
                mode = TOUCH_MODE_RELEASE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                mode = TOUCH_MODE_UNSET;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode++;
                if (mode >= TOUCH_MODE_DOUBLE) {
                    scaleFactor = preScaleFactor = mView.getScaleX();
                    preTranslate.set(mView.getTranslationX(), mView.getTranslationY());
                    scaleBaseR = (float) distanceBetweenFingers(event);
                    centerPointBetweenFingers(event, preFocusCenter);
                    centerPointBetweenFingers(event, postFocusCenter);
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (enableScale) {

                    if (mode >= TOUCH_MODE_DOUBLE) {
                        //双指缩放
                        float scaleNewR = (float) distanceBetweenFingers(event);
                        centerPointBetweenFingers(event, postFocusCenter);
                        if (scaleBaseR <= 0) {
                            break;
                        }
                        scaleFactor = (scaleNewR / scaleBaseR) * preScaleFactor * 0.15f + scaleFactor * 0.85f;
                        int scaleState = TouchEventListener.FREE_SCALE;
                        float finalMinScale = isKeepInViewport ? minScale : minScale * 0.8f;
                        if (scaleFactor >= MAX_SCALE) {
                            scaleFactor = MAX_SCALE;
                            scaleState = TouchEventListener.MAX_SCALE;
                        } else if (scaleFactor <= finalMinScale) {
                            scaleFactor = finalMinScale;
                            scaleState = TouchEventListener.MIN_SCALE;
                        }
                        if (controlListener != null) {
                            int current = (int) (scaleFactor * 100);
                            //回调
                            if (scalePercentOnlyForControlListener != current) {
                                scalePercentOnlyForControlListener = current;
                                controlListener.onScaling(scaleState, scalePercentOnlyForControlListener);
                            }
                        }
                        mView.setPivotX(0);
                        mView.setPivotY(0);
                        mView.setScaleX(scaleFactor);
                        mView.setScaleY(scaleFactor);
                        float tx = postFocusCenter.x - (preFocusCenter.x - preTranslate.x) * scaleFactor / preScaleFactor;
                        float ty = postFocusCenter.y - (preFocusCenter.y - preTranslate.y) * scaleFactor / preScaleFactor;
                        mView.setTranslationX(tx);
                        mView.setTranslationY(ty);
                        keepWithinBoundaries();
                    } else if (mode == TOUCH_MODE_SINGLE) {
                        //单指移动
                        float deltaX = event.getRawX() - preMovingTouchEvent.getRawX();
                        float deltaY = event.getRawY() - preMovingTouchEvent.getRawY();
                        onSinglePointMoving(deltaX, deltaY);
                    }

                } else {
                    //单指移动
                    float deltaX = event.getRawX() - preMovingTouchEvent.getRawX();
                    float deltaY = event.getRawY() - preMovingTouchEvent.getRawY();
                    onSinglePointMoving(deltaX, deltaY);
                }

                break;
            case MotionEvent.ACTION_OUTSIDE:
                //外界的事件
                break;
        }
        preMovingTouchEvent = MotionEvent.obtain(event);
        return true;
    }

    /**
     * 计算两个事件的移动距离
     */
    private float calculateMoveDistance(MotionEvent event1, MotionEvent event2) {
        if (event1 == null || event2 == null) {
            return 0f;
        }
        float disX = Math.abs(event1.getRawX() - event2.getRawX());
        float disY = Math.abs(event1.getRawX() - event2.getRawX());
        return (float) Math.sqrt(disX * disX + disY * disY);
    }

    /**
     * 单指移动
     */
    private void onSinglePointMoving(float deltaX, float deltaY) {
        float translationX = mView.getTranslationX() + deltaX;
        mView.setTranslationX(translationX);
        float translationY = mView.getTranslationY() + deltaY;
        mView.setTranslationY(translationY);
        keepWithinBoundaries();
    }

    /**
     * 需要保持在界限之内
     */
    private void keepWithinBoundaries() {
        //默认不在界限内，不做限制，直接返回
        if (!isKeepInViewport) {
            return;
        }
        calculateBound();
        int dBottom = layoutLocationInParent.bottom - viewportBox.bottom;
        int dTop = layoutLocationInParent.top - viewportBox.top;
        int dLeft = layoutLocationInParent.left - viewportBox.left;
        int dRight = layoutLocationInParent.right - viewportBox.right;
        float translationX = mView.getTranslationX();
        float translationY = mView.getTranslationY();
        //边界限制
        if (dLeft > 0) {
            mView.setTranslationX(translationX - dLeft);
        }
        if (dRight < 0) {
            mView.setTranslationX(translationX - dRight);
        }
        if (dBottom < 0) {
            mView.setTranslationY(translationY - dBottom);
        }
        if (dTop > 0) {
            mView.setTranslationY(translationY - dTop);
        }
    }

    /**
     * 移动时计算边界，赋值给本地的视图
     */
    private void calculateBound() {
        View v = mView;
        float left = v.getLeft() * v.getScaleX() + v.getTranslationX();
        float top = v.getTop() * v.getScaleY() + v.getTranslationY();
        float right = v.getRight() * v.getScaleX() + v.getTranslationX();
        float bottom = v.getBottom() * v.getScaleY() + v.getTranslationY();
        layoutLocationInParent.setValues((int) top, (int) left, (int) right, (int) bottom);
    }

    /**
     * 计算两个手指之间的距离
     */
    private double distanceBetweenFingers(MotionEvent event) {
        if (event.getPointerCount() > 1) {
            float disX = Math.abs(event.getX(0) - event.getX(1));
            float disY = Math.abs(event.getY(0) - event.getY(1));
            return Math.sqrt(disX * disX + disY * disY);
        }
        return 1;
    }

    /**
     * 计算两个手指之间的中心点
     */
    private void centerPointBetweenFingers(MotionEvent event, PointF point) {
        float xPoint0 = event.getX(0);
        float yPoint0 = event.getY(0);
        float xPoint1 = event.getX(1);
        float yPoint1 = event.getY(1);
        point.set((xPoint0 + xPoint1) / 2f, (yPoint0 + yPoint1) / 2f);
    }

    /**
     * 设置视图是否要保持在窗口中
     */
    public void setKeepInViewport(boolean keepInViewport) {
        isKeepInViewport = keepInViewport;
    }

    /**
     * 设置控制的监听回调
     */
    public void setControlListener(TouchEventListener controlListener) {
        this.controlListener = controlListener;
    }

    public void setEnableScale(boolean enableScale) {
        this.enableScale = enableScale;
    }

    public void setMinScale(float minScale) {
        this.minScale = minScale;
        MIN_SCALE = minScale;
    }

    public void setMaxScale(float maxScale) {
        MAX_SCALE = maxScale;
    }
}
