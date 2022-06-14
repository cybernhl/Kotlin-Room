package com.google.android.material.appbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


public class MyTextBehavior extends CoordinatorLayout.Behavior<View> {


    private int imgHeight;

    public MyTextBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        return dependency instanceof ImageView;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull View child, @NonNull View dependency) {
        //跟随ImageView滚动，ImageView滚动多少我滚动多少
        float translationY = dependency.getTranslationY();

        if (imgHeight == 0) {
            imgHeight = dependency.getHeight();
        }

        float offsetTranslationY = imgHeight + translationY;

        child.setTranslationY(offsetTranslationY);

        return true;
    }
}