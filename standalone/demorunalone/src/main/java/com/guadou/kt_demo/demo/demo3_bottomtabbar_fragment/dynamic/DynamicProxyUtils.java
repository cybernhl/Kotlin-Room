package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.dynamic;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;

import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.LoginDemoActivity;
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 动态代理+Hook的方式
 */
public class DynamicProxyUtils {

    //修改启动模式
    public static void hookAms() {
        try {

            Field singletonField;
            Class<?> iActivityManager;
            // 1，获取Instrumentation中调用startActivity(,intent,)方法的对象
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 10.0以上是ActivityTaskManager中的IActivityTaskManagerSingleton
                Class<?> activityTaskManagerClass = Class.forName("android.app.ActivityTaskManager");
                singletonField = activityTaskManagerClass.getDeclaredField("IActivityTaskManagerSingleton");
                iActivityManager = Class.forName("android.app.IActivityTaskManager");
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 8.0,9.0在ActivityManager类中IActivityManagerSingleton
                Class activityManagerClass = ActivityManager.class;
                singletonField = activityManagerClass.getDeclaredField("IActivityManagerSingleton");
                iActivityManager = Class.forName("android.app.IActivityManager");
            } else {
                // 8.0以下在ActivityManagerNative类中 gDefault
                Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");
                singletonField = activityManagerNative.getDeclaredField("gDefault");
                iActivityManager = Class.forName("android.app.IActivityManager");
            }
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            // 2，获取Singleton中的mInstance，也就是要代理的对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);

            Method getMethod = singletonClass.getDeclaredMethod("get");
            Object mInstance = getMethod.invoke(singleton);
            if (mInstance == null) {
                return;
            }

            //开始动态代理
            Object proxy = Proxy.newProxyInstance(
                    Thread.currentThread().getContextClassLoader(),
                    new Class[]{iActivityManager},
                    new AmsHookBinderInvocationHandler(mInstance));

            //现在替换掉这个对象
            mInstanceField.set(singleton, proxy);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //动态代理执行类
    public static class AmsHookBinderInvocationHandler implements InvocationHandler {

        private Object obj;

        public AmsHookBinderInvocationHandler(Object rawIActivityManager) {
            obj = rawIActivityManager;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            if ("startActivity".equals(method.getName())) {

                //如果已经登录-直接放行
                if (LoginManager.isLogin()){
                    return method.invoke(obj, args);
                }

                //如果未登录-获取到原始意图，再替换Intent携带数据到LoginActivity中
                Intent raw;
                int index = 0;
                for (int i = 0; i < args.length; i++) {
                    if (args[i] instanceof Intent) {
                        index = i;
                        break;
                    }
                }

                //原始意图
                raw = (Intent) args[index];
                YYLogUtils.w("原始意图：" + raw);


                //设置新的Intent-直接制定LoginActivity
                Intent newIntent = new Intent();
                String targetPackage = "com.guadou.kt_demo";
                ComponentName componentName = new ComponentName(targetPackage, LoginDemoActivity.class.getName());
                newIntent.setComponent(componentName);
                newIntent.putExtra("targetIntent", raw);


                YYLogUtils.w("改变了Activity启动");

                args[index] = newIntent;

                YYLogUtils.w("拦截activity的启动成功" + " --->");

                return method.invoke(obj, args);

            }

            //如果不是拦截的startActivity方法，就直接放行
            return method.invoke(obj, args);
        }

    }
}
