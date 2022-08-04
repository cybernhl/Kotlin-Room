package com.guadou.kt_demo.demo.demo9_ktfollow

import android.content.Intent
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.os.Message
import androidx.lifecycle.MutableLiveData
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoCountDownBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.countDown
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.*
import java.util.*
import java.lang.Runnable


/**
 * 倒计时的实现 Kotlin-Flow流
 */
class DemoCountDwonActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoCountDownBinding>(), Runnable {

    private val click by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoCountDwonActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_count_down)
            .addBindingParams(BR.click, click)
    }

    override fun init() {

    }

    override fun startObserve() {

    }

    //在ViewModel和Activity,Fragment中都能用
//    private var mTimeDownScope: CoroutineScope? = null

    private var timer: CountDownTimer? = null

    private var handlerNum = 60

    private val mHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    if (handlerNum > 0) {
                        handlerNum--
                        YYLogUtils.w("当时计数：" + handlerNum)
                        click.countDownHander()
                    } else {
                        click.stopCountDownHander()
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mflag = false

        click.stopCountDownHander()

//        mTimeDownScope?.cancel()

        timer?.cancel()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {
        var mTimeLiveData = MutableLiveData<String>()

        @ExperimentalCoroutinesApi
        fun startCountDown() {

            var timeDownScope: CoroutineScope? = null

            countDown(
                time = 60,
                start = {
                    timeDownScope = it
                    YYLogUtils.e("开始")

                },
                end = {
                    YYLogUtils.e("结速倒计时")
                    toast("结速倒计时")

                },
                next = {

                    YYLogUtils.w("当时计数：" + it)

                    if (it == 50) {
                        timeDownScope?.cancel()
                    }

                    mTimeLiveData.value = it.toString()

                })
        }


        //倒计时的方式一
        fun countDownTimer() {
            var num = 60

            timer = object : CountDownTimer((num + 1) * 1000L, 1000L) {

                override fun onTick(millisUntilFinished: Long) {

                    YYLogUtils.w("当时计数：" + num)

                    if (num == 0) {
                        YYLogUtils.w("重新开始")
                        num = 60
                    } else {
                        num--
                    }

                }

                override fun onFinish() {
                    YYLogUtils.w("倒计时结束了..." + num)

                }
            }

            timer?.start()
        }


        fun countDownHander() {
            mHandler.sendEmptyMessageDelayed(1, 1000)
        }

        fun stopCountDownHander() {
            mHandler.removeCallbacksAndMessages(null)
        }

        fun countDownTimer2() {
            var num = 60

            val timer = Timer()
            val timeTask = object : TimerTask() {
                override fun run() {
                    num--
                    YYLogUtils.w("当时计数：" + num)
                    if (num < 0) {
                        timer.cancel()
                    }
                }
            }

            timer.schedule(timeTask, 1000, 1000)

        }


        fun countDownThread() {

            if (!mThread.isAlive) {

                mflag = true

                if (mThread.state == Thread.State.TERMINATED) {
                    mThread = Thread(this@DemoCountDwonActivity)
                    if (mThreadNum == -1) mThreadNum = 60
                    mThread.start()
                } else {
                    mThread.start()
                }
            } else {

                mflag = false

            }

        }

    }

    private var mThread: Thread = Thread(this)
    private var mflag = false
    private var mThreadNum = 60

    override fun run() {
        while (mflag && mThreadNum >= 0) {
            try {
                Thread.sleep(1000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            val message = Message.obtain()
            message.what = 1
            message.arg1 = mThreadNum
            handler.sendMessage(message)

            mThreadNum--
        }
    }

    private val handler = Handler(Looper.getMainLooper()) { msg ->

        if (msg.what == 1) {
            val num = msg.arg1
            //由于需要主线程显示UI，这里使用Handler通信
            YYLogUtils.w("当时计数：" + num)
        }

        true
    }


}