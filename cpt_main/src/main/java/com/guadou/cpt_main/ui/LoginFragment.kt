package com.guadou.cpt_main.ui

import android.net.Uri
import com.guadou.cpt_main.R
import com.guadou.lib_baselib.base.BaseFragment
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.nav.nav
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseFragment<EmptyViewModel>() {

    override fun initVM(): EmptyViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.fragment_login


    override fun startObserve() {

    }

    override fun init() {

        btn_back.click {
            //两种返回方式都可以
//            nav().navigate(R.id.action_back_page1)

            nav().popBackStack(R.id.page1, false)
        }

    }
}