package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.FunctionManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.IFunction
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptThreadManager
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils


class Demo3ThreeFragment : BaseVMFragment<EmptyViewModel>() {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView

    companion object {
        fun obtainFragment(): Demo3ThreeFragment {
            return Demo3ThreeFragment()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page3

    override fun startObserve() {

    }

    override fun init() {

        mBtnCleanToken.click {
            SP().remove(Constants.KEY_TOKEN)
            toast("清除成功")
        }

        mBtnProfile.click {

            checkLogin()

        }

    }

    private fun checkLogin() {
        LoginInterceptThreadManager.get().checkLogin( {
            gotoProfilePage()
        }, {
            gotoLoginPage()
        })
    }

    private fun gotoLoginPage() {
        gotoActivity<LoginDemoActivity>()
    }

    private fun gotoProfilePage() {
        gotoActivity<ProfileDemoActivity>()
    }


    override fun initViews(view: View) {
        mBtnCleanToken = view.findViewById(R.id.btn_clean_token)
        mBtnProfile = view.findViewById(R.id.btn_profile)
    }
}