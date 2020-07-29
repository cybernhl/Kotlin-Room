package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TextViewSemiBold extends TextView {

    public TextViewSemiBold(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TextViewSemiBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);
    }

    public TextViewSemiBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("SanFranciscoText-Semibold.otf", context);
        setTypeface(customFont);
    }
}
