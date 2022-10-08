package com.guadou.kt_demo.demo.demo17_softinput.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.core.graphics.Insets;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.guadou.lib_baselib.utils.log.YYLogUtils;
import com.guadou.lib_baselib.utils.statusBarHost.HeightValueCallback;

public class SoftInputUtil {

    private int softInputHeight = 0;
    private boolean softInputHeightChanged = false;

    private boolean isNavigationBarShow = false;
    private int navigationHeight = 0;

    private View anyView;
    private View rootView;
    private ISoftInputChanged listener;
    private boolean isSoftInputShowing = false;
    private SoftInputUtil.myListener myListener;

    public interface ISoftInputChanged {
        void onChanged(boolean isSoftInputShow, int softInputHeight, int viewOffset);
    }

    public void adjustETWithSoftInput(final View anyView, final ISoftInputChanged listener) {
        if (anyView == null || listener == null)
            return;

        //根View
        final View rootView = anyView.getRootView();
        if (rootView == null) return;

        getNavigationBarHeight(anyView, new NavigationBarCallback() {
            @Override
            public void onHeight(int height, boolean hasNav) {

                SoftInputUtil.this.navigationHeight = height;

                //anyView为需要调整高度的View，理论上来说可以是任意的View
                SoftInputUtil.this.anyView = anyView;
                SoftInputUtil.this.rootView = rootView;
                SoftInputUtil.this.listener = listener;
                SoftInputUtil.this.isNavigationBarShow = hasNav;
                SoftInputUtil.this.myListener = new myListener();

                rootView.addOnLayoutChangeListener(myListener);

            }
        });

    }

    //RootView的监听回调
    class myListener implements View.OnLayoutChangeListener {

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

            //对于Activity来说，该高度即为屏幕高度
            int rootHeight = rootView.getHeight();

            Rect rect = new Rect();
            //获取当前可见部分，默认可见部分是除了状态栏和导航栏剩下的部分
            rootView.getWindowVisibleDisplayFrame(rect);

            //还是要每次回调的时候判断是否有导航栏
            if (rootHeight - rect.bottom == navigationHeight) {
                //如果可见部分底部与屏幕底部刚好相差导航栏的高度，则认为有导航栏
                isNavigationBarShow = true;
            } else if (rootHeight - rect.bottom == 0) {
                //如果可见部分底部与屏幕底部平齐，说明没有导航栏
                isNavigationBarShow = false;
            }

            //判断软键盘是否展示并计算软键盘的高度
            boolean isSoftInputShow = false;
            int softInputHeight = 0;
            //如果有导航栏，则要去除导航栏的高度
            int mutableHeight = isNavigationBarShow ? navigationHeight : 0;
            if (rootHeight - mutableHeight > rect.bottom) {
                //除去导航栏高度后，可见区域仍然小于屏幕高度，则说明键盘弹起了
                isSoftInputShow = true;
                //键盘高度
                softInputHeight = rootHeight - mutableHeight - rect.bottom;
                if (SoftInputUtil.this.softInputHeight != softInputHeight) {
                    softInputHeightChanged = true;
                    SoftInputUtil.this.softInputHeight = softInputHeight;
                } else {
                    softInputHeightChanged = false;
                }
            }

            //获取目标View的位置坐标
            int[] location = new int[2];
            anyView.getLocationOnScreen(location);

            //条件1减少不必要的回调，只关心前后发生变化的
            //条件2针对软键盘切换手写、拼音键等键盘高度发生变化
            if (isSoftInputShowing != isSoftInputShow || (isSoftInputShow && softInputHeightChanged)) {
                if (listener != null) {
                    //第三个参数为该View需要调整的偏移量
                    //此处的坐标都是相对屏幕左上角(0,0)为基准的
                    listener.onChanged(isSoftInputShow, softInputHeight, location[1] + anyView.getHeight() - rect.bottom);
                }

                isSoftInputShowing = isSoftInputShow;
            }

        }
    }

    public void releaseETWithSoftInput() {
        if (myListener != null) {
            rootView.removeOnLayoutChangeListener(myListener);
        }
    }

    private static int getNavigationBarHeight(Context context) {
        if (context == null) return 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static void getNavigationBarHeight(View view, NavigationBarCallback callback) {

        boolean attachedToWindow = view.isAttachedToWindow();

        if (attachedToWindow) {

            WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(view);
            assert windowInsets != null;

            int height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

            boolean hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                    windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0;

            if (height > 0) {
                callback.onHeight(height, hasNavigationBar);
            } else {
                callback.onHeight(getNavigationBarHeight(view.getContext()), hasNavigationBar);
            }

        } else {

            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
                @Override
                public void onViewAttachedToWindow(View v) {

                    WindowInsetsCompat windowInsets = ViewCompat.getRootWindowInsets(v);
                    assert windowInsets != null;
                    int height = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom;

                    boolean hasNavigationBar = windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars()) &&
                            windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars()).bottom > 0;

                    if (height > 0) {
                        callback.onHeight(height, hasNavigationBar);
                    } else {
                        callback.onHeight(getNavigationBarHeight(view.getContext()), hasNavigationBar);
                    }
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                }
            });
        }
    }
}