package com.guadou.kt_demo
import com.guadou.kt_demo.databinding.ActivityDemoMainBinding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.kt_demo.demo.demo11_fragment_navigation.Demo11Activity
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

        fun navDemo1(){
            Demo1Activity.startInstance()
        }

        fun navDemo2(){
            Demo2Activity.startInstance()
        }

        fun navDemo3(){
            Demo3Activity.startInstance()
        }

        fun navDemo4(){
            Demo4Activity.startInstance()
        }

        fun navDemo5(){
            Demo5Activity.startInstance()
        }

        fun navDemo6(){
            Demo6Activity.startInstance()
        }

        fun navDemo7(){
            Demo7Activity.startInstance()
        }

        fun navDemo8(){
            Demo8Activity.startInstance()
        }

        fun navDemo9(){
            DemoCountDwonActivity.startInstance()
        }

        fun navDemo10(){
            Demo10Activity.startInstance()
        }

        fun navDemo11(){
            Demo11Activity.startInstance()
        }
    }

}
