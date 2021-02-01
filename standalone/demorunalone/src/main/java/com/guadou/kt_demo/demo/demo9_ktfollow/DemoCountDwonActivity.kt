package com.guadou.kt_demo.demo.demo9_ktfollow

import android.content.Intent
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
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel

/**
 * 倒计时的实现 Kotlin-Flow流
 */
class DemoCountDwonActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoCountDownBinding>() {

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
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun init() {

    }

    override fun startObserve() {

    }

    //在ViewModel和Activity,Fragment中都能用
    private var mTimeDownScope: CoroutineScope? = null

    override fun onDestroy() {
        super.onDestroy()
        mTimeDownScope?.cancel()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {
        var mTimeLiveData = MutableLiveData<String>()

        @ExperimentalCoroutinesApi
        fun startCountDown() {

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

                    mTimeLiveData.value = it.toString()

                })

        }

    }

}