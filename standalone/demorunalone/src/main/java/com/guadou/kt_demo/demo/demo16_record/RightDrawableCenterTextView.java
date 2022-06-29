package com.guadou.kt_demo.demo.demo16_record;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

public class RightDrawableCenterTextView extends AppCompatTextView {

    public RightDrawableCenterTextView(Context context) {
        super(context);
    }

    public RightDrawableCenterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RightDrawableCenterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Drawable[] drawables = getCompoundDrawables();
        Drawable drawable = drawables[2];
        if (drawable != null) {
            float textWidth = getPaint().measureText(getText().toString());
            int drawablePadding = getCompoundDrawablePadding();
            int drawableWidth = drawable.getMinimumWidth();
            float bodyWidth = textWidth + drawableWidth + drawablePadding + getPaddingLeft() + getPaddingRight();
            canvas.translate((bodyWidth - getWidth()) / 2, 0);
        }
        super.onDraw(canvas);
    }
}
