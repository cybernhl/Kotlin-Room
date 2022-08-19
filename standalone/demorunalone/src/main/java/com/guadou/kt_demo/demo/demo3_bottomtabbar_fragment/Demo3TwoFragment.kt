package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.FunctionManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.IFunction
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptCoroutinesManager
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils


class Demo3TwoFragment : BaseVMFragment<EmptyViewModel>() ,DefaultLifecycleObserver{

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView

    companion object {
        fun obtainFragment(): Demo3TwoFragment {
            return Demo3TwoFragment()
        }
    }


    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page2


    override fun startObserve() {
//        FunctionManager.get().addLoginCallback(this) {
//            gotoProfilePage()
//        }
    }

    override fun init() {

        lifecycle.addObserver(this)

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

            FunctionManager.get().addFunction(object : IFunction("gotoProfilePage") {
                override fun function() {
                    gotoProfilePage()
                }
            })

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

    override fun onCreate(owner: LifecycleOwner) {
        YYLogUtils.w("lifecycle的回调 create 方式2")
    }

    override fun onDestroy(owner: LifecycleOwner) {
        YYLogUtils.w("lifecycle的回调 destroy 方式2")
    }
}