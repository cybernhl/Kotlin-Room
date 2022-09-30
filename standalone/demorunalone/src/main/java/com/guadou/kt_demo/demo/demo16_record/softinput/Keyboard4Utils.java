package com.guadou.kt_demo.demo.demo16_record.softinput;

import static android.view.WindowInsetsAnimation.Callback.DISPATCH_MODE_STOP;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowInsetsAnimation;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public final class Keyboard4Utils {

    public static int sDecorViewInvisibleHeightPre;
    private static ViewTreeObserver.OnGlobalLayoutListener onGlobalLayoutListener;

    private Keyboard4Utils() {
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            invokeAbove31(activity, listener);
        } else {
            invokeBelow31(activity, listener);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private static void invokeAbove31(Activity activity, KeyboardHeightListener listener) {

        activity.getWindow().getDecorView().setWindowInsetsAnimationCallback(new WindowInsetsAnimation.Callback(DISPATCH_MODE_STOP) {
            @NonNull
            @Override
            public WindowInsets onProgress(@NonNull WindowInsets windowInsets, @NonNull List<WindowInsetsAnimation> list) {

                int height = windowInsets.getInsets(WindowInsetsCompat.Type.ime()).bottom;

                listener.onKeyboardHeightChanged(height);

                return windowInsets;
            }
        });
    }

    private static void invokeBelow31(Activity activity, KeyboardHeightListener listener) {

        final int flags = activity.getWindow().getAttributes().flags;

        if ((flags & WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS) != 0) {
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        final FrameLayout contentView = activity.findViewById(android.R.id.content);
        sDecorViewInvisibleHeightPre = getDecorViewInvisibleHeight(activity);

        onGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = getDecorViewInvisibleHeight(activity);
                if (sDecorViewInvisibleHeightPre != height) {

                    listener.onKeyboardHeightChanged(height);

                    sDecorViewInvisibleHeightPre = height;
                }
            }
        };

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
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