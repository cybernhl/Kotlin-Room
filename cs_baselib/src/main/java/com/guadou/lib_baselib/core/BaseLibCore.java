package com.guadou.lib_baselib.core;

import android.app.Application;
import android.graphics.Color;
import android.os.Handler;

import androidx.core.content.ContextCompat;

import com.guadou.basiclib.R;
import com.guadou.lib_baselib.receiver.ConnectivityReceiver;
import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.ThreadPoolUtils;
import com.guadou.lib_baselib.view.titlebar.EasyTitleBar;


/**
 * 底层的模组依赖库都在这里初始化
 * 当前类在Application中初始化
 */
public class BaseLibCore {

    /**
     * 初始化全局工具类和图片加载引擎
     */
    public static void init(Application application, Handler handler, int mainThread) {

        //CommUtil初始化
        CommUtils.init(application, handler, mainThread);

        //初始化全局通用的线程池
        ThreadPoolUtils.init();

        //EaseTitleBar的初始化
        EasyTitleBar.init()
                .backIconRes(R.mipmap.back_black)
                .backgroud(Color.WHITE)
                .titleSize(18)
                .showLine(false)
                .lineHeight(1)
                .menuImgSize(23)
                .menuTextSize(16)
                .lineColor(ContextCompat.getColor(application.getApplicationContext(), R.color.divider_color))
                .titleColor(ContextCompat.getColor(application.getApplicationContext(), R.color.black))
                .viewPadding(10)
                .titleBarHeight(48);


    }

    /**
     * 注册网络监听
     */
    public static void registerNetworkObserver(Application application) {
        ConnectivityReceiver.registerReceiver(application);
    }

    public static void unregisterNetworkObserver(Application application) {
        ConnectivityReceiver.unregisterReceiver(application);
    }


}
