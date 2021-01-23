package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.FragmentDemo11Page1Binding
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.getActivityVM
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class Demo11OneFragment1 : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page1Binding>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment1 {
            return Demo11OneFragment1()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page1)
            .addBindingParams(BR.click,ClickProxy())
    }


    override fun startObserve() {
        //通过Activity中的LiveData来接收返回的数据
        getActivityVM(Demo11ViewModel::class.java).mBackOneLiveData.observe(this, Observer {
            toast(it)
        })
    }

    override fun init() {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Page1 - onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    override fun onResume() {
        super.onResume()
        YYLogUtils.w("Page1 - onResume")
    }

    override fun onPause() {
        super.onPause()
        YYLogUtils.w("Page1 - onPause")
    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("Page1 - onStart")
    }


    override fun onStop() {
        super.onStop()
        YYLogUtils.w("Page1 - onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page1 - onDestroy")
    }

    inner class ClickProxy{

        fun nav2Page2(){
            nav().navigate(R.id.action_page1_to_page2, Bundle().apply {
                putString("text", "携带数据给Page Two")
            })
        }
    }
}