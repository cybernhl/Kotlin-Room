package com.guadou.kt_demo.demo.demo11_fragment_navigation.nav2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.getAllFragments
import androidx.navigation.getAllNavFragments
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo11_fragment_navigation.Demo11OneFragment1
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneActivityCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.IOneFragmentCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.ITwoActivityCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.callback.ITwoFragmentCallback
import com.guadou.kt_demo.demo.demo11_fragment_navigation.di.Book
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.ext.commContext
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
class DemoNav2Activity : BaseVMActivity<Nav2ViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoNav2Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_demo_nav2
    }

    override fun init() {

    }


    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host).navigateUp()
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }

    override fun startObserve() {
    }


}