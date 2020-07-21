package com.guadou.kt_demo.ui.demo5

import android.annotation.SuppressLint
import android.content.Intent
import androidx.lifecycle.Observer
import com.guadou.kt_demo.R
import com.guadou.kt_demo.ui.demo5.mvvm.Demo5ViewModel
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import kotlinx.android.synthetic.main.activity_demo5.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 网络请求的实例代码
 *
 * 一定要注意 Repository和ViewModel 都要在di中注册
 */
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

    override fun initVM(): Demo5ViewModel = getViewModel()

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