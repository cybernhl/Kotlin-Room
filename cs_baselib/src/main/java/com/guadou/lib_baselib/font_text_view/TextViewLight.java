package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by roselle.elefante on 02/08/2017.
 * Custom TextView for Light
 */

public class TextViewLight extends TextView {

    public TextViewLight(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewLight(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewLight(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SF-UI-Text-Light.otf", context);
        setTypeface(customFont);
    }
}
