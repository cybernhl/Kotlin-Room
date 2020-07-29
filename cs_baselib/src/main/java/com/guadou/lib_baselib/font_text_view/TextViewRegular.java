package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

/**
 * Created by roselle.elefante on 02/08/2017.
 * Custom TextView for Regular
 */

public class TextViewRegular extends TextView {

    public TextViewRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewRegular(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("San-Francisco-Display-Regular.ttf", context);
        setTypeface(customFont);
    }
}
