package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.content.Intent
import android.os.Bundle
import com.guadou.cpt_main.ui.LoginFragment
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo11Page3Binding
import com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt.Demo10Activity
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.navigation.*

class Demo11OneFragment3(private val name: String?) : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page3Binding>() {

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page3)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {
        toast("name:$name")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 120 && resultCode == -1) {
            toast("接收到返回的数据：" + data?.getStringExtra("text"))
        }
    }

    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Page3 - onResume")
    }

    override fun onPause() {
        super.onPause()
        YYLogUtils.w("Page3 - onPause")
    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("Page3 - onStart")
    }

    override fun onStop() {
        super.onStop()
        YYLogUtils.w("Page3 - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page3 - onDestroy")
    }

    inner class ClickProxy {
        fun back2Page1() {
            navigator.popTo(Demo11OneFragment1::class, true)
        }

        fun back2Page2() {
            navigator.pop()
        }

        fun nav2Login() {
            navigator.start(LoginFragment::class) {
                applySlideInOut()
            }
        }

        fun receiveBackData() {
            //接收Activity返回的数据
            startActivityForResult(Intent(mActivity, Demo10Activity::class.java), 120)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        YYLogUtils.w("Demo3 onSaveInstanceState")

    }

}