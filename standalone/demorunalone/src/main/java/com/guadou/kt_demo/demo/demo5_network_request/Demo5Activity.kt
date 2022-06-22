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
import com.guadou.lib_baselib.utils.Log.YYLogUtils
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
            mViewModel.testNullNet()
            LiveEventBus.get("newMember").post(true)
        }

        /**
         * 串联顺序执行
         */
        fun networkChuan() {
            //打印track追踪的网络请求数据
            TrackEventListener.networkTrackCallback = object : TrackEventListener.NetworkTrackCallback {
                override fun onCallEnd(map: Map<String, Any>) {
                    YYLogUtils.i("track map :$map")
                    //可以通过IO写入到文件-上传到服务器
                }
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