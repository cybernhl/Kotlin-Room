package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.FunctionManager
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*


class Demo3TwoFragment : BaseVMFragment<EmptyViewModel>() {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView

    companion object {
        fun obtainFragment(): Demo3TwoFragment {
            return Demo3TwoFragment()
        }
    }


    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page2


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
        if (SP().getString(Constants.KEY_TOKEN, "").checkEmpty()) {

//            FunctionManager.get().addFunction(object : Function("gotoProfilePage") {
//                override fun function() {
//                    gotoProfilePage()
//                }
//            })

            FunctionManager.get().addLoginCallback(this) {
                gotoProfilePage()
            }

            gotoLoginPage()

        } else {

            gotoProfilePage()
        }
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