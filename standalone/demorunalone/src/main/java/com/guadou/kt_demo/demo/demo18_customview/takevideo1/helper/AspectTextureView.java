package com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.TextureView;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

/**
 * 按比例展示的TextureView，可以像ImageView一样的选择裁剪方向
 */
public class AspectTextureView extends TextureView implements AspectInterface{

    public AspectTextureView(Context context) {
        super(context);
    }

    public AspectTextureView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @ScaleType
    private int scaleType = ScaleType.FIT_CENTER;
    private int previewW = 0;
    private int previewH = 0;

    @Override
    public void setScaleType(@ScaleType int scaleType) {
        this.scaleType = scaleType;
    }

    @Override
    public void setSize(int width, int height) {
        post(() -> {
            previewW = width;
            previewH = height;
            //请求重新布局
            requestLayout();
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        if (0 == previewW || 0 == previewH) {
            //未设定宽高比，使用预览窗口默认宽高
            setMeasuredDimension(width, height);
        } else {
            int tarW = 0, tarH = 0;
            if (scaleType == ScaleType.FIT_XY) {
                tarW = width;
                tarH = height;
            } else if (scaleType == ScaleType.FIT_CENTER) {
                //设定宽高比，调整预览窗口大小（调整后窗口大小不超过默认值）
                if (width * 1F / height < previewW * 1F / previewH) {
                    tarW = width;
                    tarH = width * previewH / previewW;
                } else {
                    tarW = height * previewW / previewH;
                    tarH = height;
                }
            } else if (scaleType == ScaleType.CENTER_CROP) {
                if (width * 1F / height < previewW * 1F / previewH) {
                    //屏幕比预览细长，不管缩小还是拉伸，都应该先满足height
                    tarH = height;
                    tarW = previewW * height / previewH;
                } else {
                    //预览比屏幕细长
                    tarW = width;
                    tarH = previewH * width / previewW;
                }
            }
            YYLogUtils.d( "原来的:" + width + "|" + height + "  raw:" + previewW + "|" + previewH + "  后：" + tarW + ":" + tarH);
            setMeasuredDimension(tarW, tarH);
            setTranslationX((width - tarW) * 1F / 2);
            setTranslationY((height - tarH) * 1F / 2);
        }

    }
}
