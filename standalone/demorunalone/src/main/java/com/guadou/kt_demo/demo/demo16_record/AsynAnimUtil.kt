package com.guadou.kt_demo.demo.demo16_record

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Handler
import android.os.HandlerThread
import android.view.ViewPropertyAnimator
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.guadou.lib_baselib.ext.runOnUIThread
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class AsynAnimUtil private constructor() : LifecycleObserver {

    private var mHandlerThread: HandlerThread? = HandlerThread("anim_run_in_thread")

    private var mHandler: Handler? = mHandlerThread?.run {
        start()
        Handler(this.looper)
    }

    private var mOwner: LifecycleOwner? = null
    private var mAnim: ViewPropertyAnimator? = null

    companion object {
        val instance: AsynAnimUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            AsynAnimUtil()
        }
    }

    //启动动画
    fun startAnim(owner: LifecycleOwner?, animator: ViewPropertyAnimator) {
        try {
            if (mOwner != owner) {
                mOwner = owner
                addLoopLifecycleObserver()
            }

            if (mHandlerThread?.isAlive != true) {
                YYLogUtils.w("handlerThread restart")
                mHandlerThread = HandlerThread("anim_run_in_thread")
                mHandler = mHandlerThread?.run {
                    start()
                    Handler(this.looper)
                }
            }

            mHandler?.post {
                mAnim = animator.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        destory()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        super.onAnimationCancel(animation)
                        destory()
                    }

                    override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                        super.onAnimationEnd(animation, isReverse)
                        destory()
                    }
                })
                mAnim?.start()
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    // 绑定当前页面生命周期
    private fun addLoopLifecycleObserver() {
        runOnUIThread {
            mOwner?.lifecycle?.addObserver(this)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        YYLogUtils.i("AsynAnimUtil Lifecycle -> onDestroy")
        mAnim?.cancel()
        destory()
    }

    private fun destory() {
        YYLogUtils.w("handlerThread quit")

        try {
            mHandlerThread?.quitSafely()

            mAnim = null
            mOwner = null
            mHandler = null
            mHandlerThread = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}