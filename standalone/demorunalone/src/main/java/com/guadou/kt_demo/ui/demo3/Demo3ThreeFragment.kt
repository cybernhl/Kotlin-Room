package com.guadou.kt_demo.ui.demo3

import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseFragment
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.toast
import kotlinx.android.synthetic.main.fragment_demo3_page.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class Demo3ThreeFragment :BaseFragment<BaseViewModel>() {

    companion object {
        fun obtainFragment(): Demo3ThreeFragment {
            return Demo3ThreeFragment()
        }
    }

    override fun initVM(): BaseViewModel  = getViewModel()

    override fun inflateLayoutById(): Int  = R.layout.fragment_demo3_page


    override fun startObserve() {

    }

    override fun init() {
        tv_content.text = "Three Page"
        toast("Three Page")
    }
}