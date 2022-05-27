package com.guadou.kt_demo.demo.demo15_aidl.service3

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo15Service1Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils


/**
 *  通过Binder的方式
 */
class Service3Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo15Service1Binding>() {

    private var mMessenger: Messenger? = null

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mMessenger = Messenger(iBinder)
        }

        override fun onServiceDisconnected(componentName: ComponentName) {}
    }


    //回调的消息
    private val mReplyHandler: Handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(message: Message) {
            // 处理Service回复的消息
            if (message.what == 2) {
                val bundle = message.data
                val msg = bundle.getString("string")

                YYLogUtils.w("Receive Service Message :$msg")
            }
        }
    }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Service3Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo15_service1)
    }

    override fun startObserve() {
    }

    override fun init() {
        //自动启动Service并绑定
        val intent = Intent(this, MyService3::class.java)
        bindService(intent, mConnection, BIND_AUTO_CREATE)

        //发送数据触发事件
        mBinding.btnSend.click {

            val msg = Message()
            msg.what = 1
            msg.data = Bundle().apply {
                putString("string", "Hello service3")
            }
            msg.replyTo = Messenger(mReplyHandler)

            try {
                // Activity里面使用这个Messenger对象给Service发消息
                mMessenger?.send(msg)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }


}