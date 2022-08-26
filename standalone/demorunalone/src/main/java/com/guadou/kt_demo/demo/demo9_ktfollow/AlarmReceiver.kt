package com.guadou.kt_demo.demo.demo9_ktfollow

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.Build
import android.os.SystemClock

import com.guadou.lib_baselib.utils.ActivityManage
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils


class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {

        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager

        //执行的任务
        val intent1 = Intent(CommUtils.getContext(), AlarmReceiver::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getBroadcast(context, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

        // 重复定时任务，延时180秒发送
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 180000, pendingIntent)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 180000, pendingIntent)
        } else {
            alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), 180000, pendingIntent);
        }

        YYLogUtils.w("AlarmReceiver接受广播事件 ====> 开启循环动作")

        //检测Activity栈里面是否有MainActivity
        if (ActivityManage.getActivityStack() == null || ActivityManage.getActivityStack().size == 0) {
            //重启首页
           //commContext().gotoActivity<DemoMainActivity>()
        } else {
            YYLogUtils.w("不需要重启，已经有栈在运行了 Size：" + ActivityManage.getActivityStack().size)
        }
    }

}