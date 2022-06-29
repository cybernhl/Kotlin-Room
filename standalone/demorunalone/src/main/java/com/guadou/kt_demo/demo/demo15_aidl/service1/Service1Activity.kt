package com.guadou.kt_demo.demo.demo15_aidl.service1

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo15Service1Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.log.YYLogUtils


/**
 *  通过Binder的方式
 */
class Service1Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo15Service1Binding>() {

    var mBinder: MyBinder1? = null

    private val mConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            mBinder = iBinder as MyBinder1

            //设置监听回调
            mBinder?.setOnCallbackListener { msg ->
                YYLogUtils.w("Receive Service Message :$msg")
            }
        }

        override fun onServiceDisconnected(componentName: ComponentName) {}
    }


    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Service1Activity::class.java).apply {
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
        val intent = Intent(this, MyService1::class.java)
        bindService(intent, mConnection, BIND_AUTO_CREATE)

        //发送数据触发事件
        mBinding.btnSend.click {
            //点击调用Service方法，并在上面收到回调
            mBinder?.doServiceMethod("Msg From Activity")

        }
    }


}