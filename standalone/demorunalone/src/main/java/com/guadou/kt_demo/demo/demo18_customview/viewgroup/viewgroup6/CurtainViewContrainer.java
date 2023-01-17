package com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.adapter.CurtainAdapter;
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.layoutmanager.HorizontalLayoutManager;
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.layoutmanager.ILayoutManager;
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.layoutmanager.VerticalLayoutManager;
import com.guadou.lib_baselib.utils.CommUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @auther Newki
 * @date 2023/1/14
 * @description 包裹每一个Item容器的总容器
 */
class CurtainViewContrainer extends ViewGroup {

    private ILayoutManager mLayoutManager;
    private int horizontalSpacing;  //每一个Item的左右间距
    private int verticalSpacing;  //每一个Item的上下间距
    private int mRowCount = 6;   // 一行多少个Item
    private int fixedWidth;  //如果是垂直瀑布流，需要设置宽度固定
    private int fixedHeight;

    private CurtainAdapter mAdapter;

    public CurtainViewContrainer(Context context) {
        this(context, null);
    }

    public CurtainViewContrainer(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CurtainViewContrainer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setClipChildren(false);
        setClipToPadding(false);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();

        if (mAdapter == null || mAdapter.getItemCount() == 0 || childCount == 0) {
            setMeasuredDimension(0, 0);
            return;
        }

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        if (mLayoutManager != null && (fixedWidth > 0 || fixedHeight > 0)) {

            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);

                if (mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL) {
                    measureChild(childView,
                            MeasureSpec.makeMeasureSpec(fixedWidth, MeasureSpec.EXACTLY),
                            heightMeasureSpec);
                } else {
                    measureChild(childView,
                            widthMeasureSpec,
                            MeasureSpec.makeMeasureSpec(fixedHeight, MeasureSpec.EXACTLY));
                }
            }

            int[] dimensions = mLayoutManager.performMeasure(this, mRowCount, horizontalSpacing, verticalSpacing,
                    mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL ? fixedWidth : fixedHeight);
            setMeasuredDimension(dimensions[0], dimensions[1]);

        } else {
            throw new RuntimeException("You need to set the layoutManager first");
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        if (mLayoutManager != null && (fixedWidth > 0 || fixedHeight > 0)) {
            mLayoutManager.performLayout(this, mRowCount, horizontalSpacing, verticalSpacing,
                    mLayoutManager.getLayoutDirection() == ILayoutManager.DIRECTION_VERITICAL ? fixedWidth : fixedHeight);
            performBindData();
        } else {
            throw new RuntimeException("You need to set the layoutManager first");
        }

    }


    // =======================  数据适配器 begin ↓ =========================

    public void setAdapter(CurtainAdapter adapter) {
        mAdapter = adapter;
        inflateAllViews();
    }

    public CurtainAdapter getAdapter() {
        return mAdapter;
    }

    //填充Adapter布局
    private void inflateAllViews() {
        removeAllViewsInLayout();

        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        //添加布局
        for (int i = 0; i < mAdapter.getItemCount(); i++) {

            int itemType = mAdapter.getItemViewType(i);

            View view = mAdapter.onCreateItemView(getContext(), this, itemType);

            addView(view);
        }

        requestLayout();
    }

    //绑定布局中的数据
    private void performBindData() {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            return;
        }

        post(() -> {

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                int itemType = mAdapter.getItemViewType(i);
                View view = getChildAt(i);

                mAdapter.onBindItemView(view, itemType, i);
            }

        });

    }

    // =======================  暴露的LayoutManager方法 ↓ =========================

    /**
     * 设置Layout的方向，如果是垂直瀑布流，可以改变固定的宽度
     */
    public void setLayoutDirectionVertical() {
        mLayoutManager = new VerticalLayoutManager();
    }

    public void setLayoutDirectionVertical(int fixedWidth) {
        this.fixedWidth = fixedWidth;
        mLayoutManager = new VerticalLayoutManager();
    }

    /**
     * 设置Layout的方向，如果是水平瀑布流，可以改变固定的高度
     */
    public void setLayoutDirectionHorizontal() {
        mLayoutManager = new HorizontalLayoutManager();
    }

    public void setLayoutDirectionHorizontal(int fixedHeight) {
        this.fixedHeight = fixedHeight;
        mLayoutManager = new HorizontalLayoutManager();
    }

    public void setHorizontalSpacing(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing;
    }

    public void setVerticalSpacing(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    public void setRowCount(int rowCount) {
        this.mRowCount = rowCount;
    }

    public void setFixedWidth(int fixedWidth) {
        this.fixedWidth = fixedWidth;
    }

    public void setFixedHeight(int fixedHeight) {
        this.fixedHeight = fixedHeight;
    }

}
