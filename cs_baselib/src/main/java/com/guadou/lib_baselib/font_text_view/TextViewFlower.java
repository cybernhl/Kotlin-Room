package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by roselle.elefante on 02/08/2017.
 * 标题花体字
 */

public class TextViewFlower extends TextView {

    public TextViewFlower(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewFlower(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewFlower(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("IBMPlexSerif-Medium.ttf", context);
        setTypeface(customFont);
    }
}
