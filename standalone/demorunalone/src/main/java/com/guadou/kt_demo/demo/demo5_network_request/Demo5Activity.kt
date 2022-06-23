package com.guadou.kt_demo.demo.demo5_network_request

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo5Binding
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.track.TrackEventListener
import com.jeremyliao.liveeventbus.LiveEventBus
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
@AndroidEntryPoint
class Demo5Activity : BaseVDBActivity<Demo5ViewModel, ActivityDemo5Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo5Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo5, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, ClickProxy())
    }


    @SuppressLint("SetTextI18n")
    override fun startObserve() {
        //行业回调
        mViewModel.mIndustryLD.observe(this, {
            mViewModel.mContentLiveData.value = it.toString()

        })

        //学校回调
        mViewModel.mSchoolLD.observe(this, {
            mViewModel.mContentLiveData.value = mBinding.tvNetContent.text.toString() + "\n" + "学校的数据===>：" + "\n"
            mViewModel.mContentLiveData.value = mBinding.tvNetContent.text.toString() + it.toString()
        })

    }

    override fun init() {
        toast("测试-跳转到新页面")
        YYLogUtils.w("ViewModel: $mViewModel Repository:${mViewModel.testRepository()}")
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun testnull() {
//            mViewModel.testNullNet()
            LiveEventBus.get("newMember").post(true)

//            YYLogUtils.w("Test Log")

            val testNet = TestNet()

            testNet.setOnSuccessCallbackDsl {
                onSuccess { str ->
                    YYLogUtils.w("str: $str")
                    str + "再加一点数据"
                }
                doSth {
                    YYLogUtils.w("可以随便写点什么成功之后的逻辑")
                }
            }

            YYLogUtils.w("no:" + testNet.no)
            "".foo()

            testNet.requestNetwork(onCancel = {

                toast("test network onCancel")

            }, onFailed = {
                //先调用内部的函数处理逻辑
                it.onFailed("哎呦")  //在这里调用内部定义过的函数，如果不调用，TestNet中 YYLogUtils.w("可以随便写点什么逻辑") 不会执行

                it.onError()

                //在打印日志
                YYLogUtils.w("test network onFailed")

            },
//                onSuccess = {
//                //先打印日志
//                YYLogUtils.w("test network onSuccess")
//
//                //再调用内部的函数处理逻辑
//                onSuccess("我的成功数据字符串")    //上面是高阶函数的调用 - 这里是高阶扩展函数的调用，同样的效果，上面需要用it调用，这里直接this 调用
//
//            },
                onFinished = {
                YYLogUtils.w("当前值是10,满足条件：$it")  //这里的it是那边的回调
                true  //那边是带参数返回的，这里需要返回Booble给那边
            })

        }

        /**
         * 串联顺序执行
         */
        fun networkChuan() {

            //打印track追踪的网络请求数据
            TrackEventListener.networkTrackCallback = TrackEventListener.NetworkTrackCallback { map -> //可以通过IO写入到文件-上传到服务器
                YYLogUtils.i("track map :$map")
            }

            mViewModel.mContentLiveData.value = ""
            mViewModel.netWorkSeries()
        }

        /**
         * 并发
         */
        fun networkBing() {
            mViewModel.mContentLiveData.value = ""
            mViewModel.netSupervene()
        }

        /**
         * 去重
         */
        fun networkDup() {
            mViewModel.mContentLiveData.value = ""
            //没有防抖动-狂点试试看Log
            mViewModel.netDuplicate()
        }

        fun testConcurrency() {

//            for (i in 1..10) {
//                testFun(i)
//            }

            testFun(0)
//            countDown(10, next = { time ->
//                testFun(time)
//            }, start = {}, end = {})

        }
    }

    private fun testFun(index: Int) {
        lifecycleScope.launch {

            val one = async {
                withContext(Dispatchers.Default) {
                    TestUtils.getInstance().value = 1
                    true
                }
            }

            val two = async {
                withContext(Dispatchers.IO) {
                    TestUtils.getInstance().value = 2
                    true
                }

            }

            if (one.await() && two.await()) {
                YYLogUtils.w("index:" + index + " values :" + TestUtils.getInstance().value)
            }
        }


    }

}