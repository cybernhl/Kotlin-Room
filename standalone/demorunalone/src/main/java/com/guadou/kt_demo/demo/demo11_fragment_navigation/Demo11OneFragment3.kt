package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.FragmentDemo11Page3Binding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class Demo11OneFragment3 : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page3Binding>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment3 {
            return Demo11OneFragment3()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page3)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120 && resultCode == -1) {
            toast("接收到返回的数据：" + data?.getStringExtra("text"))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page3 - onDestroy")
    }

    inner class ClickProxy {
        fun back2Page1() {
            nav().navigate(R.id.action_back_page1)
        }

        fun back2Page2() {
            nav().popBackStack(R.id.page2, false)
//            nav().navigateUp()
        }

        fun nav2Login(){
            nav().navigate(R.id.action_goto_login)
        }

        fun receiveBackData(){
            //接收Activity返回的数据
            startActivityForResult(Intent(mActivity, Demo10Activity::class.java), 120)
        }
    }

}