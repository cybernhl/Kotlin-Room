package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.UserServer
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo_1.*
import javax.inject.Inject

@AndroidEntryPoint
class Demo1Activity : BaseVMActivity<EmptyViewModel>() {

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

    override fun inflateLayoutById(): Int = R.layout.activity_demo_1

    override fun startObserve() {
    }

    override fun init() {
        toast("ViewModel: $mViewModel  --- ${userServer.getDaoContent()}")

        btn_demo_1.click {
            JumpLoadingActivity.startInstance()
        }

        btn_demo_2.click {
            NormalLoadingActivity.startInstance()
        }

        btn_demo_3.click {
            PlaceHolderLoadingActivity.startInstance()
        }

        btn_demo_4.click {
            EmptyFragmentActivity.startInstance(1)
        }

        btn_demo_5.click {
            EmptyFragmentActivity.startInstance(2)
        }

        btn_demo_6.click {
            EmptyFragmentActivity.startInstance(3)
        }
    }
}