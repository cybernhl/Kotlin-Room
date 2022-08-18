package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread;

import android.os.Handler;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;


/**
 * 登录拦截的线程管理
 */
public class LoginInterceptThreadManager  {

    private static LoginInterceptThreadManager threadManager;

    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static final Handler mHandler = new Handler();

    private LoginInterceptThreadManager() {
    }

    public static LoginInterceptThreadManager get() {
        if (threadManager == null) {
            threadManager = new LoginInterceptThreadManager();
        }

        return threadManager;
    }

    /**
     * 检查是否需要登录
     */
    public void checkLogin(Runnable nextRunnable, Runnable loginRunnable) {



        if (LoginManager.isLogin()) {
            //已经登录
            mHandler.post(nextRunnable);
            return;
        }

        //如果没有登录-先去登录页面
        mHandler.post(loginRunnable);


//        singleThreadExecutor.execute(() -> {
//
//            try {
//                YYLogUtils.w("开始运行-停止");
//
//                synchronized (singleThreadExecutor) {
//                    singleThreadExecutor.wait();
//
//                    YYLogUtils.w("等待notifyAll完成了,继续执行");
//
//                    if (LoginManager.isLogin()) {
//                        mHandler.post(nextRunnable);
//                    }
//                }
//
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        });


        //在开启一个线程
        singleThreadExecutor.execute(new FutureTask<Boolean>(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                YYLogUtils.w("call()");

                synchronized (mHandler) {
                    mHandler.wait();
                    YYLogUtils.w("等待notifyAll,继续执行");
                    return LoginManager.isLogin();
                }
            }
        }) {
            @Override
            protected void done() {

                YYLogUtils.w("done()");

                try {
                    final boolean result = get();
                    mHandler.removeCallbacksAndMessages(null);
                    if (result) {
                        mHandler.post(nextRunnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loginFinished() {
        if (mHandler == null) return;
        if (singleThreadExecutor == null) return;

        synchronized (mHandler) {
            mHandler.notifyAll();
        }
//        synchronized (singleThreadExecutor) {
//            singleThreadExecutor.notifyAll();
//        }
    }

}
