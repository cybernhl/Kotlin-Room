package com.guadou.kt_demo.demo.demo16_record;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.guadou.lib_baselib.utils.Log.YYLogUtils;

public class MyLooperThread extends Thread {

    // 子线程的looper
    private Looper myLooper;
    // 子线程的handler
    private Handler mHandler;

    // 用于测试的textview
    private TextView testView;

    private Activity activity;

    public Looper getLooper() {
        return myLooper;
    }

    public Handler getHandler() {
        return mHandler;
    }

    public MyLooperThread(Context context, TextView view) {
        this.activity = (Activity) context;
        testView = view;
    }

    @Override
    public void run() {
        super.run();
        // 调用了此方法后，当前线程拥有了一个looper对象
        Looper.prepare();
        YYLogUtils.w("消息循环开始");

        if (myLooper == null) {
            while (myLooper == null) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 调用此方法获取当前线程的looper对象
                myLooper = Looper.myLooper();
            }
        }

        // 当前handler与当前线程的looper关联
        mHandler = new Handler(myLooper) {
            @Override
            public void handleMessage(Message msg) {
                YYLogUtils.w("处理消息：" + msg.obj);

                //此线程，此Looper创建的ui可以随便修改
                addTextViewInChildThread().setText(String.valueOf(msg.obj));

                //发现跟ui创建的位置有关。如果ui是在main线程创建的，则在子线程中不可以更改此ui；
                // 如果在含有looper的子线程中创建的ui，则可以任意修改
                // 这里传进来的是主线程的ui，不能修改！低版本可能可以修改
                //CalledFromWrongThreadException: Only the original thread that created a view hierarchy can touch its views.
//                try {
//                    if (testView != null) {
//                        testView.setText(String.valueOf(msg.obj));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//
//                }
            }
        };
        Looper.loop();
        YYLogUtils.w("looper消息循环结束，线程终止");
    }

    /**
     * 创建TextView
     */
    private TextView addTextViewInChildThread() {
        TextView textView = new TextView(activity);

        textView.setBackgroundColor(Color.GRAY);  //背景灰色
        textView.setGravity(Gravity.CENTER);  //居中展示
        textView.setTextSize(20);

        WindowManager windowManager = activity.getWindowManager();
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                0, 0,
                WindowManager.LayoutParams.FIRST_SUB_WINDOW,
                WindowManager.LayoutParams.TYPE_TOAST,
                PixelFormat.TRANSPARENT);
        windowManager.addView(textView, params);

        return textView;
    }
}

