package com.guadou.kt_demo.demo.demo8_recyclerview

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo8_recyclerview.rv1.DemoRVNormalVertalActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv2.DemoRVNormalGridActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv3.DemoRVHeadFoodVertalActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.DemoRVMobanActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv5.DemoRVMuliteActivity
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.DemoRVDiffActivity
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import kotlinx.android.synthetic.main.activity_demo8.*

/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
class Demo8Activity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo8Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo8

    @SuppressLint("SetTextI18n")
    override fun startObserve() {

    }

    override fun init() {

        initLitener()
    }


    private fun initLitener() {

        //垂直
        btn_rv_1.click {
            DemoRVNormalVertalActivity.startInstance()
        }

        //Grid
        btn_rv_2.click {
            DemoRVNormalGridActivity.startInstance()
        }

        //扩展加头和脚
        btn_rv_3.click {
            DemoRVHeadFoodVertalActivity.startInstance()
        }

        //网络列表模板
        btn_rv_4.click {
            DemoRVMobanActivity.startInstance()
        }

        //多布局
        btn_rv_5.click {
            DemoRVMuliteActivity.startInstance()
        }

        //Diff
        btn_rv_6.click {
            DemoRVDiffActivity.startInstance()
        }
    }


}