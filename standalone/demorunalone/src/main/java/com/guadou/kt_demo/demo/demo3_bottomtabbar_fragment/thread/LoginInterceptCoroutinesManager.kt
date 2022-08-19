package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor


class LoginInterceptCoroutinesManager private constructor() : DefaultLifecycleObserver, CoroutineScope by MainScope() {

    companion object {
        private var instance: LoginInterceptCoroutinesManager? = null
            get() {
                if (field == null) {
                    field = LoginInterceptCoroutinesManager()
                }
                return field
            }

        fun get(): LoginInterceptCoroutinesManager {
            return instance!!
        }
    }

    //    private val channel = Channel<Boolean>()
//    private val broadcastChannel = BroadcastChannel<Boolean>(Channel.BUFFERED)
//    lateinit var sendChannel: SendChannel<Boolean>
    private lateinit var mCancellableContinuation: CancellableContinuation<Boolean>

    fun checkLogin(loginAction: () -> Unit, nextAction: () -> Unit) {

        launch {

            if (LoginManager.isLogin()) {
                nextAction()
                return@launch
            }

            loginAction()

            val isLogin = suspendCancellableCoroutine<Boolean> {

                mCancellableContinuation = it

                YYLogUtils.w("暂停协程，等待唤醒")
            }


            YYLogUtils.w("已经恢复协程，继续执行")
            if (isLogin) {
                nextAction()
            }

//            val receiveChannel = broadcastChannel.openSubscription()
//            val isLogin = receiveChannel.receive()

//            YYLogUtils.w("收到消息：" + isLogin)
//
//            if (isLogin) {
//                nextAction()
//            }


//            sendChannel = actor {
//
//                val isLogin = receive()
//                YYLogUtils.w("收到消息：" + isLogin)
//                if (isLogin) {
//                    nextAction()
//                }
//
//            }

//            val isLogin = channel.receive()
//
//            YYLogUtils.w("收到消息：" + isLogin)
//
//            if (isLogin) {
//                nextAction()
//            }
        }
    }

    fun loginFinished() {
        if (!this@LoginInterceptCoroutinesManager::mCancellableContinuation.isInitialized) return

        if (mCancellableContinuation.isCancelled) return

        mCancellableContinuation.resume(LoginManager.isLogin(), null)


//        launch {

//            async {
//                YYLogUtils.w("发送消息：" + LoginManager.isLogin())
//                channel.send(LoginManager.isLogin())
//            }

//            if (this@LoginInterceptCoroutinesManager::sendChannel.isInitialized) {
//                YYLogUtils.w("发送消息：" + LoginManager.isLogin())
//                sendChannel.send(LoginManager.isLogin())
//            }

//            YYLogUtils.w("发送消息：" + LoginManager.isLogin())
//            broadcastChannel.send(LoginManager.isLogin())
//        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        YYLogUtils.w("LoginInterceptCoroutinesManager - onDestroy")
//        broadcastChannel.cancel()
        mCancellableContinuation.cancel()
        cancel()
    }

}