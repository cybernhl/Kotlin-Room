package com.guadou.kt_demo.demo.demo11_fragment_navigation

import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.fragment.BaseFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.nav.nav
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.fragment_demo11_page3.*


class Demo11OneFragment3 : BaseFragment<EmptyViewModel>() {

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

    }

    override fun onDestroy() {
        super.onDestroy()
        YYLogUtils.w("Page3 - onDestroy")
    }

}