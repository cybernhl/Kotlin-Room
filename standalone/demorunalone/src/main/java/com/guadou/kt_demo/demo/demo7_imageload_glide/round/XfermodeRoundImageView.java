package com.guadou.kt_demo.demo.demo7_imageload_glide.round;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.guadou.lib_baselib.utils.CommUtils;

public class XfermodeRoundImageView extends AppCompatImageView {

    private Paint mPaint;
    private Xfermode mXfermode;
    private int mRoundRadius = CommUtils.dip2px(15);

    public XfermodeRoundImageView(Context context) {
        this(context, null);
    }

    public XfermodeRoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XfermodeRoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //在api11到api18之间设置禁用硬件加速
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR2
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        if (getDrawable() == null) {
            return;
        }

        int sc = canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        //画源图像，为一个圆角矩形
        canvas.drawRoundRect(new RectF(0, 0, getWidth(), getHeight()), mRoundRadius, mRoundRadius, mPaint);
        //设置混合模式
        mPaint.setXfermode(mXfermode);
        //画目标图像
        canvas.drawBitmap(drawableToBitamp(exChangeSize(getDrawable())), 0, 0, mPaint);
        // 还原混合模式
        mPaint.setXfermode(null);
        canvas.restoreToCount(sc);

    }

    /**
     * 图片拉升
     */
    private Drawable exChangeSize(Drawable drawable) {
        float scale = 1.0f;
        scale = Math.max(getWidth() * 1.0f / drawable.getIntrinsicWidth(), getHeight()
                * 1.0f / drawable.getIntrinsicHeight());
        drawable.setBounds(0, 0, (int) (scale * drawable.getIntrinsicWidth()),
                (int) (scale * drawable.getIntrinsicHeight()));
        return drawable;
    }


    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth() <= 0 ? getWidth() : drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight() <= 0 ? getHeight() : drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }
}