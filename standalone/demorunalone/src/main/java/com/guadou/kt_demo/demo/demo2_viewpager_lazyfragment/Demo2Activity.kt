package com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo2Binding
import com.guadou.lib_baselib.annotation.NetWork
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.bindFragment
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.NetWorkUtil


/**
 * ViewPager+LazyFragment
 */
class Demo2Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo2Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_2)

    }

    override fun startObserve() {
    }

    override fun init() {

        mBinding.viewPager.bindFragment(
            supportFragmentManager,
            listOf(LazyLoad1Fragment.obtainFragment(), LazyLoad2Fragment.obtainFragment(), LazyLoad3Fragment.obtainFragment()),
            listOf("Demo1", "Demo2", "Demo3"),
            behavior = 1
        )

        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager)
    }


    override fun needRegisterNetworkChangeObserver(): Boolean {
        return true
    }

    @NetWork(netWorkType = NetWorkUtil.NetworkType.NETWORK_WIFI)
    fun activeWifi() {
        toast("当前是Wifi状态-> 开始下载插件")
    }

    @NetWork(netWorkType = NetWorkUtil.NetworkType.NETWORK_NO)
    fun activeNoNet() {
        toast("当前没网了-> 加载缓存展示一下")
    }
}