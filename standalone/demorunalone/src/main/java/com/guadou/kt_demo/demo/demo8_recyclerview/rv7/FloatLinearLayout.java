package com.guadou.kt_demo.demo.demo8_recyclerview.rv7;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.math.MathUtils;
import androidx.core.view.ViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.guadou.kt_demo.R;

/**
 * 可实现Appbar模式的 二次悬停
 * 使用demo
 * <com.google.android.material.appbar.AppBarLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content">
 * <p>
 * <ImageView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@drawable/pic1"
 * android:contentDescription="吸顶区域一"
 * android:gravity="center"
 * android:scaleType="fitXY"
 * app:layout_scrollFlags="scroll" />
 * <p>
 * <ImageView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@drawable/pic2"
 * android:contentDescription="吸顶区域二"
 * android:gravity="center"
 * app:layout_scrollFlags="enterAlways|scroll"></ImageView>
 * <p>
 * <com.peter.viewgrouptutorial.coordinatorlayout.FloatLinearLayout
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:orientation="vertical"
 * app:layout_scrollFlags="enterAlways|scroll|exitUntilCollapsed">
 * <p>
 * <p>
 * <ImageView
 * android:id="@+id/text.view3"
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@drawable/pic3"
 * android:contentDescription="吸顶区域三"
 * android:gravity="center"
 * android:text="HEAD3"
 * app:layout_pin="true" />
 * <p>
 * <ImageView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@drawable/pic4"
 * android:contentDescription="吸顶区域四"
 * android:gravity="center"
 * app:layout_pin="false" />
 * <p>
 * <ImageView
 * android:layout_width="match_parent"
 * android:layout_height="wrap_content"
 * android:background="@drawable/pic5"
 * android:contentDescription="吸顶区域四"
 * app:layout_pin="false" />
 * </com.peter.viewgrouptutorial.coordinatorlayout.FloatLinearLayout>
 * >
 * </com.google.android.material.appbar.AppBarLayout>
 */
public class FloatLinearLayout extends LinearLayout {
    int currentOffset;
    private AppBarLayout.OnOffsetChangedListener onOffsetChangedListener;

    public FloatLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setChildrenDrawingOrderEnabled(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (((LayoutParams) child.getLayoutParams()).pin) {
                setMinimumHeight(child.getMeasuredHeight());
                break;
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        // Add an OnOffsetChangedListener if possible
        final ViewParent parent = getParent();
        if (parent instanceof AppBarLayout) {
            // Copy over from the ABL whether we should fit system windows
            ViewCompat.setFitsSystemWindows(this, ViewCompat.getFitsSystemWindows((View) parent));

            if (onOffsetChangedListener == null) {
                onOffsetChangedListener = new FloatLinearLayout.OffsetUpdateListener();
            }
            ((AppBarLayout) parent).addOnOffsetChangedListener(onOffsetChangedListener);

            // We're attached, so lets request an inset dispatch
            ViewCompat.requestApplyInsets(this);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        // Remove our OnOffsetChangedListener if possible and it exists
        final ViewParent parent = getParent();
        if (onOffsetChangedListener != null && parent instanceof AppBarLayout) {
            ((AppBarLayout) parent).removeOnOffsetChangedListener(onOffsetChangedListener);
        }

        super.onDetachedFromWindow();
    }

    @Override
    protected int getChildDrawingOrder(int childCount, int i) {
        return childCount - i - 1;
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    public static class LayoutParams extends LinearLayout.LayoutParams {
        boolean pin = false;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            TypedArray a = c.obtainStyledAttributes(attrs, R.styleable.FloatLinearLayout_Layout);
            pin = a.getBoolean(R.styleable.FloatLinearLayout_Layout_layout_pin, false);
            a.recycle();
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(ViewGroup.LayoutParams p) {
            super(p);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(LinearLayout.LayoutParams source) {
            super(source);
        }
    }

    private class OffsetUpdateListener implements com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener {

        @Override
        public void onOffsetChanged(AppBarLayout layout, int verticalOffset) {
            currentOffset = verticalOffset;
            for (int i = 0, z = getChildCount(); i < z; i++) {
                final View child = getChildAt(i);
                final FloatLinearLayout.LayoutParams lp = (FloatLinearLayout.LayoutParams) child.getLayoutParams();
                final ViewOffsetHelper offsetHelper = getViewOffsetHelper(child);
                if (lp.pin) {
                    int offset = -currentOffset - getTop();
                    offsetHelper.setTopAndBottomOffset(MathUtils.clamp(offset, 0, offset));
                }
            }
        }
    }

    @NonNull
    static ViewOffsetHelper getViewOffsetHelper(@NonNull View view) {
        ViewOffsetHelper offsetHelper = (ViewOffsetHelper) view.getTag(com.google.android.material.R.id.view_offset_helper);
        if (offsetHelper == null) {
            offsetHelper = new ViewOffsetHelper(view);
            view.setTag(com.google.android.material.R.id.view_offset_helper, offsetHelper);
        }
        return offsetHelper;
    }

}
