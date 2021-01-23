package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.github.fragivity.navigator
import com.github.fragivity.push
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.FragmentDemo11Page1Binding
import com.guadou.kt_demo.demo.demo11_fragment_navigation.vm.Demo11ViewModel
import com.guadou.lib_baselib.base.fragment.BaseVDBFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.applySlideInOut
import com.guadou.lib_baselib.ext.getActivityVM
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.Log.YYLogUtils


class Demo11OneFragment1 : BaseVDBFragment<EmptyViewModel, FragmentDemo11Page1Binding>() {

    val callback: (Int, String) -> Unit = { int, str ->
        toast("int : $int ; str: $str")
    }

    companion object {
        fun obtainFragment(): Demo11OneFragment1 {
            return Demo11OneFragment1()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_demo11_page1)
            .addBindingParams(BR.click, ClickProxy())
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

    inner class ClickProxy {

        fun nav2Page2() {
            //跳转的几种方式，跳转Class文件
//            navigator.push(Demo11OneFragment2::class, bundleOf("text" to "携带数据给Page Two")) {
//                applySlideInOut()
//            }

//            navigator.push {
//                return@push Demo11OneFragment2(callback)
//            }

            //可以直接跳转实例对象
//            navigator.push(optionsBuilder = {
//                applySlideInOut()
//            }, block = {
//                return@push Demo11OneFragment2(callback)
//            })
            //简化
            navigator.push({ applySlideInOut() }) {
                Demo11OneFragment2(callback)
            }

        }
    }
}