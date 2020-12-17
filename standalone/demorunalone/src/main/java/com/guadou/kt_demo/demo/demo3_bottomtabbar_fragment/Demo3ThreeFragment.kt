package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import androidx.fragment.app.viewModels
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.toast
import kotlinx.android.synthetic.main.fragment_demo3_page.*


class Demo3ThreeFragment :BaseFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): Demo3ThreeFragment {
            return Demo3ThreeFragment()
        }
    }

    override fun initVM(): EmptyViewModel {
        val viewModel: EmptyViewModel by viewModels()
        return viewModel
    }

    override fun inflateLayoutById(): Int  = R.layout.fragment_demo3_page


    override fun startObserve() {

    }

    override fun init() {
        tv_content.text = "Three Page"
        toast("Three Page")
    }
}