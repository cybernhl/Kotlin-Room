package com.guadou.kt_demo.demo.demo9_ktfollow

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.countDown
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.activity_demo_count_down.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * 倒计时的实现 Kotlin-Flow流
 */
class DemoCountDwonActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoCountDwonActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }


    override fun inflateLayoutById(): Int = R.layout.activity_demo_count_down

    override fun startObserve() {

    }

    @ExperimentalCoroutinesApi
    override fun init() {

        btn_send.click {
            startCountDown()
        }
    }

    //在ViewModel和Activity,Fragment中都能用
    private var mTimeDownScope: CoroutineScope? = null
    @ExperimentalCoroutinesApi
    private fun startCountDown() {

        countDown(
            time = 60,
            start = {
                mTimeDownScope = it
                YYLogUtils.e("开始")

            },
            end = {
                YYLogUtils.e("结速倒计时")
                toast("结速倒计时")

            },
            next = {

                if (it == 50) {
                    mTimeDownScope?.cancel()
                }

                tv_show_time.text = it.toString()

            })

    }
}