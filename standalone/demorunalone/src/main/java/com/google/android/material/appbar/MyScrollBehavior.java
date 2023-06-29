package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;

import java.util.List;

/**
 * 让底部的滚动布局在图片下面
 * 这里泛型是哪个，这个Behavior就给谁设置
 */
public class MyScrollBehavior extends ViewOffsetBehavior<NestedScrollView> {
    private int topImgHeight;
    private int topTextHeight;

    public MyScrollBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull NestedScrollView child,
                                   @NonNull View dependency) {
        return dependency instanceof ImageView || dependency instanceof TextView;
    }

    @Override
    protected void layoutChild(CoordinatorLayout parent, NestedScrollView child, int layoutDirection) {
        super.layoutChild(parent, child, layoutDirection);
        if (topImgHeight == 0) {
            final List<View> dependencies = parent.getDependencies(child);

            for (int i = 0, z = dependencies.size(); i < z; i++) {
                View view = dependencies.get(i);
                if (view instanceof ImageView) {
                    topImgHeight = view.getMeasuredHeight();
                } else if (view instanceof TextView) {
                    topTextHeight = view.getMeasuredHeight();
                    view.setTop(topImgHeight);
                    view.setBottom(view.getBottom() + topImgHeight);
                }
            }
        }

        child.setTop(topImgHeight + topTextHeight);
        child.setBottom(child.getBottom() + topImgHeight + topTextHeight);
    }

}
