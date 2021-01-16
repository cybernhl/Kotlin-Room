package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.toast
import kotlinx.android.synthetic.main.fragment_demo3_page.*


class Demo3OneFragment : BaseVMFragment<EmptyViewModel>() {

    companion object {
        fun obtainFragment(): Demo3OneFragment {
            return Demo3OneFragment()
        }
    }


    override fun inflateLayoutById(): Int = R.layout.fragment_demo3_page


    override fun startObserve() {

    }

    override fun init() {

        tv_content.text = "One Page"

        toast("One Page")
    }
}