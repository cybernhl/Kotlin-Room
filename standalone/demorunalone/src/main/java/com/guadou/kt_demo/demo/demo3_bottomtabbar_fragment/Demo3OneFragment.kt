package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.Login
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginCallback
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*


class Demo3OneFragment : BaseVMFragment<EmptyViewModel>() {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView

    companion object {
        fun obtainFragment(): Demo3OneFragment {
            return Demo3OneFragment()
        }
    }


    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page


    override fun startObserve() {

    }

    override fun init() {

        mBtnCleanToken.click {
            SP().remove(Constants.KEY_TOKEN)
            toast("清除成功")
        }

        mBtnProfile.click {

            //带回调继续执行的登录方式
//            gotoProfilePage()

            //不带回调的登录方式
            gotoProfilePage2()
        }

    }

    @LoginCallback
    private fun gotoProfilePage() {
        gotoActivity<ProfileDemoActivity>()
    }

    @Login
    private fun gotoProfilePage2() {
        gotoActivity<ProfileDemoActivity>()
    }

    override fun initViews(view: View) {
        mBtnCleanToken = view.findViewById(R.id.btn_clean_token)
        mBtnProfile = view.findViewById(R.id.btn_profile)
    }
}