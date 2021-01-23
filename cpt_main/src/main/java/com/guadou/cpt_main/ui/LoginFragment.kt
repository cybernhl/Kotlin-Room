package com.guadou.cpt_main.ui

import com.github.fragivity.navigator
import com.github.fragivity.pop
import com.guadou.cpt_main.R
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : BaseVMFragment<EmptyViewModel>() {

    override fun getLayoutIdRes(): Int = R.layout.fragment_login


    override fun startObserve() {

    }

    override fun init() {

        btn_back.click {
            navigator.pop()
        }

    }
}