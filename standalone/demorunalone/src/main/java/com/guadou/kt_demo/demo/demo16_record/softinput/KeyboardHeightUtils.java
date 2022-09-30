package com.guadou.kt_demo.demo.demo16_record.softinput;

import android.app.Activity;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.guadou.kt_demo.R;


public class KeyboardHeightUtils extends PopupWindow {

    private KeyboardHeightListener mListener;
    private View popupView;
    private View parentView;
    private Activity activity;

    public KeyboardHeightUtils(Activity activity) {
        super(activity);
        this.activity = activity;

        LayoutInflater inflator = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        this.popupView = inflator.inflate(R.layout.keyboard_popup_window, null, false);
        setContentView(popupView);

        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);

        parentView = activity.findViewById(android.R.id.content);

        setWidth(0);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);

        popupView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                if (popupView != null) {
                    handleOnGlobalLayout();
                }
            }
        });
    }

    public void start() {

        parentView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View view) {
                if (!isShowing() && parentView.getWindowToken() != null) {
                    setBackgroundDrawable(new ColorDrawable(0));
                    showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0);
                }
            }

            @Override
            public void onViewDetachedFromWindow(View view) {
            }
        });

    }

    public void close() {
        this.mListener = null;
        dismiss();
    }

    public void registerKeyboardHeightListener(KeyboardHeightListener listener) {
        this.mListener = listener;
    }

    private void handleOnGlobalLayout() {

        Point screenSize = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(screenSize);

        Rect rect = new Rect();
        popupView.getWindowVisibleDisplayFrame(rect);

        int keyboardHeight = screenSize.y - rect.bottom;

        notifyKeyboardHeightChanged(keyboardHeight);
    }


    private void notifyKeyboardHeightChanged(int height) {
        if (mListener != null) {
            mListener.onKeyboardHeightChanged(height);
        }
    }

    public interface KeyboardHeightListener {
        void onKeyboardHeightChanged(int height);
    }

}
