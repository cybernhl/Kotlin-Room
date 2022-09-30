package com.guadou.kt_demo.demo.demo16_record.softinput;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

public final class Keyboard1Utils {

    public static int sDecorViewInvisibleHeightPre;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private Keyboard1Utils() {
    }

    private static int sDecorViewDelta = 0;

    private static int getDecorViewInvisibleHeight(final Activity activity) {
        final View decorView = activity.getWindow().getDecorView();
        if (decorView == null) return sDecorViewInvisibleHeightPre;
        final Rect outRect = new Rect();
        decorView.getWindowVisibleDisplayFrame(outRect);

        int delta = Math.abs(decorView.getBottom() - outRect.bottom);
        if (delta <= getNavBarHeight()) {
            sDecorViewDelta = delta;
            return 0;
        }
        return delta - sDecorViewDelta;
    }

    public static void registerKeyboardHeightListener(final Activity activity, final KeyboardHeightListener listener) {
        final int flags = activity.getWindow().getAttributes().flags;

        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        final FrameLayout contentView = activity.findViewById(android.R.id.content);
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity);

        ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getDecorViewInvisibleHeight(activity);
                if (sDecorViewInvisibleHeightPre != height) {

                    listener.onKeyboardHeightChanged(height);

                    sDecorViewInvisibleHeightPre = height;
                }
            }
        };
        contentView.getViewTreeObserver()
                .addOnGlobalLayoutListener(onGlobalLayoutListener);
    }

    public static void unregisterKeyboardHeightListener(Activity activity) {
        onGlobalLayoutListener = null;
        View contentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (contentView == null) return;
        contentView.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
    }

    private static int getNavBarHeight() {
        Resources res = Resources.getSystem();
        int resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId != 0) {
            return res.getDimensionPixelSize(resourceId);
        } else {
            return 0;
        }
    }

    public interface KeyboardHeightListener {
        void onKeyboardHeightChanged(int height);
    }
}