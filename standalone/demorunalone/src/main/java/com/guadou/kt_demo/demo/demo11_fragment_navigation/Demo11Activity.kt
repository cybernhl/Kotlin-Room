package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.getAllFragments
import androidx.navigation.getAllNavFragments
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneActivityCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneFragmentCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.ITwoActivityCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.ITwoFragmentCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.navigation.IOnBackPressed
import com.guadou.lib_baselib.utils.navigation.loadRootFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Fragment导航
 */
@AndroidEntryPoint
class Demo11Activity : BaseVMActivity<Demo11ViewModel>(), IOneFragmentCallback, ITwoFragmentCallback {

    @Inject
    lateinit var mMsg: String

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
//        navHostFragment.loadRootFragment(Demo11OneFragment1::class)
        navHostFragment.loadRootFragment {
            Demo11OneFragment1("测试直接构造方法初始化")
        }

        YYLogUtils.w("当前设备Android系统：" + Build.VERSION.SDK_INT)

        toast(mMsg)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }

    override fun startObserve() {

    }

    override fun onBackPressed() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        val fragments = navHostFragment.getAllFragments()  //内部可以获取到Fragment栈
        val fragment = fragments[fragments.size - 1]

        if (fragment !is IOnBackPressed) {
            super.onBackPressed()
        } else {
            if ((fragment as IOnBackPressed).onBackPressed()) {
                super.onBackPressed()
            }
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        YYLogUtils.w("ACTIVITY onSaveInstanceState")
        super.onSaveInstanceState(outState)
    }

    // =======================  callback  =========================

    override fun callActOne(str: String) {
        YYLogUtils.w("str:$str")

        getAllNavFragments(R.id.nav_host).firstOrNull {
            it is ITwoActivityCallback
        }?.let {
            (it as ITwoActivityCallback).callTwoFragment("yeye")
        }
    }

    override fun callActTwo(str: String) {
        YYLogUtils.w("str:$str")

        getAllNavFragments(R.id.nav_host).firstOrNull {
            it is IOneActivityCallback
        }?.let {
            (it as IOneActivityCallback).callOneFragment("hehe")
        }
    }

}