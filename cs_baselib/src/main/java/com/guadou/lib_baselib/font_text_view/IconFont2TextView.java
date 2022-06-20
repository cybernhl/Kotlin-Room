package com.guadou.lib_baselib.font_text_view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 字体图片的TextView
 */
public class IconFont2TextView extends AppCompatTextView {

    public IconFont2TextView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public IconFont2TextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconFont2TextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontCache.getIconFontTypeFace("002", context));
    }

}
