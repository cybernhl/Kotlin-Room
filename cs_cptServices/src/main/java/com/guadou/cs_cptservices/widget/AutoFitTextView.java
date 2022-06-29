package com.guadou.cs_cptservices.widget;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * 支持多行文本缩放的TextView
 */
public class AutoFitTextView extends AppCompatTextView {

    private static float DEFAULT_MIN_TEXT_SIZE = 15;
    private static float DEFAULT_MAX_TEXT_SIZE = 50;

    private TextPaint testPaint;
    private float minTextSize;
    private float maxTextSize;

    public AutoFitTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialise();
    }

    private void initialise() {
        testPaint = new TextPaint();
        testPaint.set(this.getPaint());
        maxTextSize = this.getTextSize();
        if (maxTextSize <= DEFAULT_MIN_TEXT_SIZE) {
            maxTextSize = DEFAULT_MAX_TEXT_SIZE;
        }
        minTextSize = DEFAULT_MIN_TEXT_SIZE;
    }


    private void refitText(String text, int textWidth, int textHeight) {
        if (textWidth > 0 && textHeight > 0) {
            int availableWidth = textWidth - this.getPaddingLeft() - this.getPaddingRight();
            int availableHeight = textHeight - this.getPaddingBottom() - this.getPaddingTop();
            int autoWidth = availableWidth;
            float mult = getLineSpacingMultiplier();
            float add = getLineSpacingExtra();
            float trySize = maxTextSize;
            testPaint.setTextSize(trySize);
            int lineCount = 1;
            while ((trySize > minTextSize)) {
                StaticLayout layout = new StaticLayout(text, testPaint, autoWidth, Layout.Alignment.ALIGN_NORMAL, mult, add, true);
                int displayH = layout.getHeight();
                if (displayH < availableHeight) {
                    lineCount = layout.getLineCount();
                    break;
                }
                trySize--;
                if (trySize <= minTextSize) {
                    trySize = minTextSize;
                    break;
                }
                testPaint.setTextSize(trySize);
            }
            //setMultiLine
            if (lineCount > 1) {
                this.setSingleLine(false);
                this.setMaxLines(lineCount);
                this.setEllipsize(TextUtils.TruncateAt.END);
            }
            this.setTextSize(TypedValue.COMPLEX_UNIT_PX, trySize);
        }
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int before, int after) {
        super.onTextChanged(text, start, before, after);
        refitText(text.toString(), this.getWidth(), this.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            refitText(this.getText().toString(), w, h);
        }
    }
}
