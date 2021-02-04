package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.loadRoot
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.AbsActivity
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.NetWorkUtil

/**
 * Fragment导航
 */
class Demo11Activity : AbsActivity() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo11Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun setContentView() {
        setContentView(R.layout.activity_demo_11)
    }

    override fun init() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.loadRoot(Demo11OneFragment1::class)
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }


}