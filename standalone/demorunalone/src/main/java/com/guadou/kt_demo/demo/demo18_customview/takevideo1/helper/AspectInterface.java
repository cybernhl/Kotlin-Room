package com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface AspectInterface {

    @IntDef({
            ScaleType.FIT_XY,  //铺满宽高
            ScaleType.FIT_CENTER,  //居中展示
            ScaleType.CENTER_CROP  //居中裁剪
    })
    
    @Retention(RetentionPolicy.SOURCE)
    public @interface ScaleType {
        int FIT_XY = 0;
        int FIT_CENTER = 1;
        int CENTER_CROP = 2;
    }

    void setScaleType(@ScaleType int scaleType);

    void setSize(int width, int height);
}
