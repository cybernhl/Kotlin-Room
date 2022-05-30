package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import android.os.Build
import androidx.navigation.fragment.NavHostFragment
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.utils.navigation.loadRoot

/**
 * Fragment导航
 */
class Demo11Activity : BaseVMActivity<Demo11ViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo11Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo_11
    }

    override fun init() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.loadRoot(Demo11OneFragment1::class)

        YYLogUtils.w("当前设备Android系统：" + Build.VERSION.SDK_INT)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }

    override fun startObserve() {

    }

}