package com.guadou.kt_demo.demo.demo16_record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class LeftDrawableCenterTextView extends AppCompatTextView {

    public LeftDrawableCenterTextView(Context context) {
        super(context);
    }

    public LeftDrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LeftDrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawableLeft = drawables[0];
        if (drawableLeft != null) {
            int drawableWith = drawableLeft.getIntrinsicWidth();
            int drawablePadding = getCompoundDrawablePadding();
            int textWith = (int) getPaint().measureText(getText().toString());
            int bodyWith = drawableWith + drawablePadding + textWith;
            canvas.translate((getWidth() - bodyWith) / 2, 0);
        }

        super.onDraw(canvas);
    }
}
