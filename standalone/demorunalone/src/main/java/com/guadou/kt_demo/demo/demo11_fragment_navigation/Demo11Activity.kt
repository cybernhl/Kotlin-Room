package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import androidx.navigation.fragment.NavHostFragment
import com.github.fragivity.loadRoot
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo11Binding
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast

/**
 * Fragment导航
 */
class Demo11Activity : BaseVDBActivity<Demo11ViewModel, ActivityDemo11Binding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo11Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_11, BR.viewModel, mViewModel)
    }

    override fun startObserve() {
    }

    override fun init() {
        toast(mViewModel.testToast())

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navHostFragment.loadRoot(Demo11OneFragment1::class)
    }


}