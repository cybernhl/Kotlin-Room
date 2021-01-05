package com.guadou.kt_demo.demo.demo11_fragment_navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.fragment_demo11_page1.*


class Demo11OneFragment1 : BaseFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): Demo11OneFragment1 {
            return Demo11OneFragment1()
        }
    }

    override fun initVM(): EmptyViewModel {
        val viewModel: EmptyViewModel by viewModels()
        return viewModel
    }

    override fun inflateLayoutById(): Int = R.layout.fragment_demo11_page1


    override fun startObserve() {

    }

    override fun init() {

        btn_to_page2.click {
           nav().navigate(R.id.action_page1_to_page2)
        }

    }

    override fun onStart() {
        super.onStart()
        YYLogUtils.w("Page1 - onStart")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        YYLogUtils.w("Page1 - onCreateView")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page1 - onDestroy")
    }
}