package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.activity

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo1Binding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.UserServer
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class Demo1Activity : BaseVDBActivity<EmptyViewModel, ActivityDemo1Binding>() {

    @Inject
    lateinit var userServer: UserServer

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo1Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_1)
            .addBindingParams(BR.click, ClickProxy())
    }


    override fun startObserve() {
    }

    override fun init() {
        toast("ViewModel: $mViewModel  --- ${userServer.getDaoContent()}")
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun jumpLoadingActivity() {
            JumpLoadingActivity.startInstance()
        }

        fun LoadingActivity() {
            NormalLoadingActivity.startInstance()
        }

        fun placeholderActivity() {
            PlaceHolderLoadingActivity.startInstance()
        }

        fun jumpLoadingFragment(){
            EmptyFragmentActivity.startInstance(1)
        }

        fun roteLoadingFragment(){
            EmptyFragmentActivity.startInstance(2)
        }

        fun placeholderFragment(){
            EmptyFragmentActivity.startInstance(3)
        }
    }
}