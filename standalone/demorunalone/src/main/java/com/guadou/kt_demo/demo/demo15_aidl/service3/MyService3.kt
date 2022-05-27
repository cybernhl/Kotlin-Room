package com.guadou.kt_demo.demo.demo15_aidl.service3

import android.app.Service
import android.content.Intent
import android.os.*
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class MyService3 : Service() {

    // Service里面实现一个Handler用来接收消息用
    private val mHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            //  Service里面的Handler收到消息并处理
            if (message.what == 1) {
                val bundle: Bundle = message.data
                val msg = bundle.getString("string")
                YYLogUtils.w("MyService3 - Recive Message: $msg")

                // 收到消息之后立马处理回复消息的逻辑
                // 取出消息中的Messenger对象 对应的是Activity Client
                val replyMessenger: Messenger = message.replyTo
                val replyMsg = Message()
                replyMsg.what = 2
                replyMsg.data = Bundle().apply {
                    putString("string", "hello  activity3")
                }

                try {
                    // 给Activity发消息
                    replyMessenger.send(replyMsg)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder {
        //用处理消息的Handler构建一个Messenger 作为一个IBinder返回
        return Messenger(mHandler).binder
    }

}