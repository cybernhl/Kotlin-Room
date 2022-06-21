package com.guadou.kt_demo.demo.demo16_record;


import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import com.guadou.lib_baselib.utils.Log.YYLogUtils;

import java.util.List;

public class BackgroundCheck {

    public static boolean isBackground(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = ((ActivityManager.RunningTaskInfo) tasks.get(0)).topActivity;

            return topActivity != null && !TextUtils.isEmpty(topActivity.getPackageName()) &&
                    !topActivity.getPackageName().equals(context.getPackageName());
        }

        return false;
    }


    public static boolean isBackgroundProcess(Context context) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        boolean isBackground = true;
        String processName = "empty";
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                processName = appProcess.processName;
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_CACHED) {
                    isBackground = true;
                } else if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                        || appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    isBackground = false;
                } else {
                    isBackground = true;
                }
            }
        }

        YYLogUtils.i("是否在后台：" + isBackground + " processName:" + processName);

        return isBackground;
    }

}
