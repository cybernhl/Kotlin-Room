package com.guadou.kt_demo.demo.demo15_aidl.service2

import android.os.RemoteCallbackList
import com.guadou.kt_demo.IMyCallbackInterface
import com.guadou.kt_demo.IMyService2Interface



class MyBinder2(private val service: MyService2) : IMyService2Interface.Stub() {

    private val mRemoteCallbackList: RemoteCallbackList<IMyCallbackInterface> = RemoteCallbackList()

    //调用Service的方法
    override fun doServiceMethod(message: String?) {
        val msg = service.doSomeThing(message)

        val count = mRemoteCallbackList.beginBroadcast()
        for (i in 0 until count) {
            mRemoteCallbackList.getBroadcastItem(i).onCallback(msg)
        }
        mRemoteCallbackList.finishBroadcast()
    }

    override fun destoryService() {
        service.shotdown()
    }

    override fun registerListener(listener: IMyCallbackInterface?) {
        mRemoteCallbackList.register(listener)
    }

    override fun unregisterListener(listener: IMyCallbackInterface?) {
        mRemoteCallbackList.unregister(listener)
    }

}