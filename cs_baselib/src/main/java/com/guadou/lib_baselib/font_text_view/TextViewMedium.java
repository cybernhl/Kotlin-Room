package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by roselle.elefante on 02/08/2017.
 * Custom TextView for Medium
 */

public class TextViewMedium extends TextView {

    public TextViewMedium(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewMedium(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewMedium(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("San-Francisco-Display-Medium.ttf", context);
        setTypeface(customFont);
    }
}
