package com.guadou.kt_demo.demo.demo18_customview.viewgroup;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewGroup2 extends ViewGroup {


    public ViewGroup2(Context context) {
        this(context, null);
    }

    public ViewGroup2(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        final int sizeWidth = MeasureSpec.getSize(widthMeasureSpec) - this.getPaddingRight() - this.getPaddingLeft();
        final int modeWidth = MeasureSpec.getMode(widthMeasureSpec);

        final int sizeHeight = MeasureSpec.getSize(heightMeasureSpec) - this.getPaddingTop() - this.getPaddingBottom();
        final int modeHeight = MeasureSpec.getMode(heightMeasureSpec);

        if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.EXACTLY) {

            measureChildren(widthMeasureSpec, heightMeasureSpec);

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        } else if (modeWidth == MeasureSpec.EXACTLY && modeHeight == MeasureSpec.AT_MOST) {

            int layoutChildViewCurX = this.getPaddingLeft();

            int totalControlHeight = 0;

            for (int i = 0; i < getChildCount(); i++) {
                final View childView = this.getChildAt(i);
                if (childView.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams lp = (LayoutParams) childView.getLayoutParams();
                childView.measure(
                        getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
                        getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height)
                );

                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                if (totalControlHeight == 0) {
                    totalControlHeight = height + lp.topMargin + lp.bottomMargin;
                }

                //如果剩余控件不够，则移到下一行开始位置
                if (layoutChildViewCurX + width + lp.leftMargin + lp.rightMargin > sizeWidth) {
                    layoutChildViewCurX = this.getPaddingLeft();
                    totalControlHeight += height + lp.topMargin + lp.bottomMargin;
                }
                layoutChildViewCurX += width + lp.leftMargin + lp.rightMargin;

            }

            //最后确定整个布局的高度和宽度
            int cachedTotalWith = resolveSize(sizeWidth, widthMeasureSpec);
            int cachedTotalHeight = resolveSize(totalControlHeight, heightMeasureSpec);

            this.setMeasuredDimension(cachedTotalWith, cachedTotalHeight);

        } else if (modeWidth == MeasureSpec.AT_MOST && modeHeight == MeasureSpec.AT_MOST) {

            //如果宽高都是Wrap-Content
            int layoutChildViewCurX = this.getPaddingLeft();
            //总宽度和总高度
            int totalControlWidth = 0;
            int totalControlHeight = 0;
            //由于宽度是非固定的，所以用一个List接收每一行的最大宽度
            List<Integer> lineLenghts = new ArrayList<>();

            for (int i = 0; i < getChildCount(); i++) {
                final View childView = this.getChildAt(i);
                if (childView.getVisibility() == GONE) {
                    continue;
                }

                final LayoutParams lp = (LayoutParams) childView.getLayoutParams();
                childView.measure(
                        getChildMeasureSpec(widthMeasureSpec, this.getPaddingLeft() + this.getPaddingRight(), lp.width),
                        getChildMeasureSpec(heightMeasureSpec, this.getPaddingTop() + this.getPaddingBottom(), lp.height)
                );

                int width = childView.getMeasuredWidth();
                int height = childView.getMeasuredHeight();

                if (totalControlHeight == 0) {
                    totalControlHeight = height + lp.topMargin + lp.bottomMargin;
                }

                //如果剩余控件不够，则移到下一行开始位置
                if (layoutChildViewCurX + width + lp.leftMargin + lp.rightMargin > sizeWidth) {
                    lineLenghts.add(layoutChildViewCurX);
                    layoutChildViewCurX = this.getPaddingLeft();
                    totalControlHeight += height + lp.topMargin + lp.bottomMargin;
                }
                layoutChildViewCurX += width + lp.leftMargin + lp.rightMargin;

            }

            //计算每一行的宽度，选出最大值
            YYLogUtils.w("每一行的宽度 ：" + lineLenghts.toString());
            totalControlWidth = Collections.max(lineLenghts);
            YYLogUtils.w("选出最大宽度 ：" + totalControlWidth);

            //最后确定整个布局的高度和宽度
            int cachedTotalWith = resolveSize(totalControlWidth, widthMeasureSpec);
            int cachedTotalHeight = resolveSize(totalControlHeight, heightMeasureSpec);

            this.setMeasuredDimension(cachedTotalWith, cachedTotalHeight);

        }

    }

    /**
     * @param changed 当前ViewGroup的尺寸或者位置是否发生了改变
     * @param l,t,r,b 当前ViewGroup相对于父控件的坐标位置，
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int mViewGroupWidth = getMeasuredWidth(); //当前ViewGroup的总宽度

        int layoutChildViewCurX = l; //当前绘制View的X坐标
        int layoutChildViewCurY = t; //当前绘制View的Y坐标

        int childCount = getChildCount(); //子控件的数量

        //遍历所有子控件，并在其位置上绘制子控件
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            //子控件的宽和高
            int width = childView.getMeasuredWidth();
            int height = childView.getMeasuredHeight();
            final LayoutParams lp = (LayoutParams) childView.getLayoutParams();

            //如果剩余控件不够，则移到下一行开始位置
            if (layoutChildViewCurX + width + lp.leftMargin + lp.rightMargin > mViewGroupWidth) {
                layoutChildViewCurX = l;
                //如果换行，则需要修改当前绘制的高度位置
                layoutChildViewCurY += height + lp.topMargin + lp.bottomMargin;
            }

            //执行childView的布局与绘制(右和下的位置加上自身的宽高即可)
            childView.layout(
                    layoutChildViewCurX + lp.leftMargin,
                    layoutChildViewCurY + lp.topMargin,
                    layoutChildViewCurX + width + lp.leftMargin + lp.rightMargin,
                    layoutChildViewCurY + height + lp.topMargin + lp.bottomMargin);

            //布局完成之后，下一次绘制的X坐标需要加上宽度
            layoutChildViewCurX += width + lp.leftMargin + lp.rightMargin;
        }

    }


    //要使子控件的margin属性有效必须继承此LayoutParams，内部还可以定制一些别的属性
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new ViewGroup2.LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }
}
