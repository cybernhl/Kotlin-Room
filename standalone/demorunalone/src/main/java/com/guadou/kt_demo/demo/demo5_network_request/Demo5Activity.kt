package com.guadou.kt_demo.demo.demo5_network_request

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.Observer
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo5.*

/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
@AndroidEntryPoint  //注解可加可不加 因为只是用ViewModel注入的话是不需要注解的，如果还想注入别的东西 需要加
class Demo5Activity : BaseActivity<Demo5ViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo5Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_demo5

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
        //行业回调
        mViewModel.mIndustryLiveData.observe(this, Observer {
            tv_net_content.text = it.toString()
        })

        //学校回调
        mViewModel.mSchoolliveData.observe(this, Observer {
            tv_net_content.text = tv_net_content.text.toString() + "\n" + "学校的数据===>：" + "\n"
            tv_net_content.text = tv_net_content.text.toString() + it.toString()
        })

    }

    override fun init() {

        initLitener()
    }

    private fun initLitener() {
        YYLogUtils.e("Viewmodel:" + mViewModel.toString())
        btn_net_1.click {

            tv_net_content.text = ""
            mViewModel.netWorkSeries()
        }

        btn_net_2.click {
            tv_net_content.text = ""
            mViewModel.netSupervene()
        }

        btn_net_3.setOnClickListener {
            //没有防抖动-狂点试试看Log
            mViewModel.netDuplicate()
        }
    }

}