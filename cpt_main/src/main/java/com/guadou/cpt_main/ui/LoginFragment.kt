package com.guadou.cpt_main.ui

import com.guadou.cpt_main.R
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.nav.nav
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseVMFragment<EmptyViewModel>() {

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