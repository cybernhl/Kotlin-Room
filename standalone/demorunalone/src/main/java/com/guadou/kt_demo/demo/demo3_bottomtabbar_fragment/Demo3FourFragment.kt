package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter.LoginInterceptChain
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter.LoginNextInterceptor
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*


class Demo3FourFragment : BaseVMFragment<EmptyViewModel>() {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView

    companion object {
        fun obtainFragment(): Demo3FourFragment {
            return Demo3FourFragment()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page4

    override fun startObserve() {

    }

    override fun init() {


        mBtnCleanToken.click {
            SP().remove(Constants.KEY_TOKEN)
            toast("清除成功")
        }

        //拦截器的方式
        mBtnProfile.click {
            checkLogin()
        }

    }

    private fun checkLogin() {
        LoginInterceptChain.addInterceptor(LoginNextInterceptor {
            gotoProfilePage()
        }).process()
    }

    private fun gotoProfilePage() {
        gotoActivity<ProfileDemoActivity>()
    }

    override fun initViews(view: View) {
        mBtnCleanToken = view.findViewById(R.id.btn_clean_token)
        mBtnProfile = view.findViewById(R.id.btn_profile)

    }


}