package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Field;


public class MyImageBehavior extends CoordinatorLayout.Behavior<View> {

    private int topBarHeight = 0;  //负图片高度
    private int downEndY = 0;   //默认为0


    public MyImageBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout,
                                       @NonNull View child, @NonNull View directTargetChild,
                                       @NonNull View target, int axes, int type) {
        //监听垂直滚动
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {

        if (topBarHeight == 0) {
            topBarHeight = -child.getMeasuredHeight();
        }

        float transY = child.getTranslationY() - dy;

        //处理上滑
        if (dy > 0) {
            if (transY >= topBarHeight) {
                translationByConsume(child, transY, consumed, dy);
                translationByConsume(target, transY, consumed, dy);
            } else {
                translationByConsume(child, topBarHeight, consumed, (child.getTranslationY() - topBarHeight));
                translationByConsume(target, topBarHeight, consumed, (child.getTranslationY() - topBarHeight));
            }
        }

        if (dy < 0 && !target.canScrollVertically(-1)) {
            //处理下滑
            if (transY >= topBarHeight && transY <= downEndY) {
                translationByConsume(child, transY, consumed, dy);
                translationByConsume(target, transY, consumed, dy);
            } else {
                translationByConsume(child, downEndY, consumed, (downEndY - child.getTranslationY()));
                translationByConsume(target, downEndY, consumed, (downEndY - child.getTranslationY()));

                //到点了，ScrollView停止滚动
                stopViewScroll(target);
            }
        }
    }

    @Override
    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, float velocityX, float velocityY, boolean consumed) {
        return super.onNestedFling(coordinatorLayout, child, target, velocityX,
                velocityY, consumed);
    }


    private void stopViewScroll(View target) {
        if (target instanceof RecyclerView) {
            ((RecyclerView) target).stopScroll();
        }
        if (target instanceof NestedScrollView) {
            try {
                Class<? extends NestedScrollView> clazz = ((NestedScrollView) target).getClass();
                Field mScroller = clazz.getDeclaredField("mScroller");
                mScroller.setAccessible(true);
                OverScroller overScroller = (OverScroller) mScroller.get(target);
                overScroller.abortAnimation();
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void translationByConsume(View view, float translationY, int[] consumed, float consumedDy) {
        consumed[1] = (int) consumedDy;
        view.setTranslationY(translationY);
    }

}