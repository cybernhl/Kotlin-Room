package com.guadou.kt_demo

import android.content.Intent
import android.net.Uri
import com.guadou.kt_demo.databinding.ActivityDemoMainBinding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.kt_demo.demo.demo12_databinding_texing.Demo12Activity
import com.guadou.kt_demo.demo.demo13_motionlayout.Demo13MotionActivity
import com.guadou.kt_demo.demo.demo14_mvi.Demo14Activity
import com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.activity.Demo1Activity
import com.guadou.kt_demo.demo.demo2_viewpager_lazyfragment.Demo2Activity
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.Demo3Activity
import com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.Demo4Activity
import com.guadou.kt_demo.demo.demo5_network_request.Demo5Activity
import com.guadou.kt_demo.demo.demo6_imageselect_premision_rvgird.Demo6Activity
import com.guadou.kt_demo.demo.demo7_imageload_glide.Demo7Activity
import com.guadou.kt_demo.demo.demo8_recyclerview.Demo8Activity
import com.guadou.kt_demo.demo.demo9_ktfollow.DemoCountDwonActivity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast

/**
 * 演示Demo的首页
 */
class DemoMainActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoMainBinding>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_main, BR.viewModel, mViewModel)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {
    }

    override fun init() {
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun navDemo1() {
            Demo1Activity.startInstance()
        }

        fun navDemo2() {
            Demo2Activity.startInstance()
        }

        fun navDemo3() {
            Demo3Activity.startInstance()
        }

        fun navDemo4() {
            Demo4Activity.startInstance()
        }

        fun navDemo5() {
            Demo5Activity.startInstance()
        }

        fun navDemo6() {
            Demo6Activity.startInstance()
        }

        fun navDemo7() {
            Demo7Activity.startInstance()
        }

        fun navDemo8() {
            Demo8Activity.startInstance()
        }

        fun navDemo9() {
            DemoCountDwonActivity.startInstance()
        }

        fun navDemo10() {
            Demo10Activity.startInstance()
        }

        fun navDemo11() {
//            Demo11Activity.startInstance()

            //可以直接跳转到Fragment3页面  在Demo11Activity下面 并且还load了root的fragment
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("demo11://com.guadou.kt_demo.demo.demo11_fragment_navigation.Demo11OneFragment3/"))
            startActivity(intent)
            toast("以路由的方式直接跳转到demo3所在的activity,并且在下面压一个root-demo1")

        }

        fun navDemo12() {
            Demo12Activity.startInstance()
        }

        fun navDemo13() {
            Demo13MotionActivity.startInstance()
        }

        fun navDemo14() {
            Demo14Activity.startInstance()
        }


    }

}
