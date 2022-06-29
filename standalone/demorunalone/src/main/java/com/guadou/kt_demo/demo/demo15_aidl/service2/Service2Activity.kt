package com.guadou.kt_demo.demo.demo15_aidl.service2

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.os.RemoteException
import com.guadou.kt_demo.IMyCallbackInterface
import com.guadou.kt_demo.IMyService2Interface
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo15Service1Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 *  通过Binder-AIDL的方式 跨进程通信
 */
class Service2Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo15Service1Binding>() {

    var mBinder: IMyService2Interface? = null

    private val deathRecipient = IBinder.DeathRecipient {

        YYLogUtils.w("重新绑定Service")
        if (mBinder != null) {
            mBinder = null
        }

//        val intent = Intent(this, MyService2::class.java)
//        bindService(intent, mConnection, BIND_AUTO_CREATE)
    }

    //回调
    private val listener = object : IMyCallbackInterface.Stub() {
        override fun onCallback(msg: String?) {
            YYLogUtils.w("Receive Service Message :$msg")
        }
    }

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mBinder = IMyService2Interface.Stub.asInterface(iBinder)

            try {
                //设置监听回调
                mBinder?.registerListener(listener)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

            //设置死亡容错
            try {
                mBinder?.asBinder()?.linkToDeath(deathRecipient, 0)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }

        }

        override fun onServiceDisconnected(componentName: ComponentName) {}
    }


    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Service2Activity::class.java).apply {
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
        val intent = Intent(this, MyService2::class.java)
        bindService(intent, mConnection, BIND_AUTO_CREATE)

        //发送数据触发事件
        mBinding.btnSend.click {
            //点击调用Service方法，并在上面收到回调
            mBinder?.doServiceMethod("Msg From Activity2")
        }

        mBinding.btnDestory.click {
            mBinder?.destoryService()
        }
    }

    override fun onDestroy() {
        mBinder?.unregisterListener(listener)
        mBinder?.asBinder()?.unlinkToDeath(deathRecipient, 0)

        super.onDestroy()
    }

}