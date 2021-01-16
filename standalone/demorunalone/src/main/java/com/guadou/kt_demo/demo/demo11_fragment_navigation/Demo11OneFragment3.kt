package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.fragment_demo11_page3.*


class Demo11OneFragment3 : BaseVMFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment3 {
            return Demo11OneFragment3()
        }
    }

    override fun inflateLayoutById(): Int = R.layout.fragment_demo11_page3


    override fun startObserve() {

    }

    override fun init() {

        btn_to_page1.click {
            nav().navigate(R.id.action_back_page1)
        }

        btn_to_page2.click {
            nav().navigate(R.id.action_page3_to_page2)
        }

        btn_to_login.click {
            nav().navigate(R.id.action_goto_login)
        }

        btn_back_data.click {
            //接收Activity返回的数据
            startActivityForResult(Intent(mActivity, Demo10Activity::class.java), 120)
        }
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

}