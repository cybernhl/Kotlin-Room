package com.guadou.kt_demo.demo.demo8_recyclerview

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo8Binding
import com.guadou.kt_demo.demo.demo7_imageload_glide.layout.policy.AbsRoundCirclePolicy
import com.guadou.kt_demo.demo.demo7_imageload_glide.layout.policy.RoundCircleLayoutOutlinePolicy
import com.guadou.kt_demo.demo.demo8_recyclerview.rv1.DemoRVNormalVertalActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv2.DemoRVNormalGridActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv3.DemoRVHeadFoodVertalActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.DemoRVMobanActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv5.DemoRVMuliteActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.DemoRVDiffActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv8.CustomRV1Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll10.Scroll10Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll11.Scroll11Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll12.Scroll12Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll13.Scroll13Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll14.Scroll14Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll8.Scroll8Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.scroll9.Scroll9Activity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext


/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
class Demo8Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo8Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo8Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo8)
            .addBindingParams(BR.click, ClickProxy())
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    override fun init() {

    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun rvVertical() {
            DemoRVNormalVertalActivity.startInstance()
        }

        fun rvGrid() {
            DemoRVNormalGridActivity.startInstance()
        }

        fun rvHeaderFooter() {
            DemoRVHeadFoodVertalActivity.startInstance()
        }

        fun rvNetworkLoadMore() {
            DemoRVMobanActivity.startInstance()
        }

        fun rvMuiltType() {
            DemoRVMuliteActivity.startInstance()
        }

        fun rvDiff() {
            DemoRVDiffActivity.startInstance()
        }

        fun customType() {
            CustomRV1Activity.startInstance()
        }

        fun nest8() {
            Scroll8Activity.startInstance()
        }

        fun nest9() {
            Scroll9Activity.startInstance()
        }

        fun nest10() {
            Scroll10Activity.startInstance()
        }

        fun nest11() {
            Scroll11Activity.startInstance()
        }

        fun nest12() {
            Scroll12Activity.startInstance()
        }

        fun nest13() {
            Scroll13Activity.startInstance()
        }

        fun nest14() {
            Scroll14Activity.startInstance()
        }
    }

}