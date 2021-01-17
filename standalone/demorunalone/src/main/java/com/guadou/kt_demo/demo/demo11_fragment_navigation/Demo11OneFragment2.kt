package com.guadou.kt_demo.demo.demo11_fragment_navigation

import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.FragmentDemo11Page2Binding
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.getActivityVM
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class Demo11OneFragment2 : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page2Binding>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment2 {
            return Demo11OneFragment2()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page2)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {
        val bundleText = arguments?.getString("text")
        toast(bundleText)
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page2 - onDestroy")
    }

    inner class ClickProxy {

        fun back2Page1() {
            //设置回调
            val viewModel = getActivityVM(Demo11ViewModel::class.java)
            viewModel.mBackOneLiveData.value = "返回给One Page的数据"

            nav().navigateUp()
        }

        fun nav2Page3() {
            nav().navigate(R.id.action_page2_to_page3)
        }
    }
}