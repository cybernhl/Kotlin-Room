package com.guadou.kt_demo.demo.demo5_network_request

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo5Binding
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint

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

        YYLogUtils.w("ViewModel: $mViewModel Repository:${mViewModel.testRepository()}")
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun testnull() {
            mViewModel.testNullNet()
        }

        /**
         * 串联顺序执行
         */
        fun networkChuan() {
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
    }

}