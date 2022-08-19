package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.view.View
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.FunctionManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.IFunction
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptCoroutinesManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptThreadManager
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils
import kotlinx.coroutines.launch


class Demo3ThreeFragment : BaseVMFragment<EmptyViewModel>(), LifecycleEventObserver {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView
    private lateinit var mBtnProfile2: TextView

    companion object {
        fun obtainFragment(): Demo3ThreeFragment {
            return Demo3ThreeFragment()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.fragment_demo3_page3

    override fun startObserve() {

    }

    override fun init() {

        lifecycle.addObserver(this)
        lifecycle.addObserver(LoginInterceptCoroutinesManager.get())

        mBtnCleanToken.click {
            SP().remove(Constants.KEY_TOKEN)
            toast("清除成功")
        }

        //线程的方式
        mBtnProfile.click {
            checkLogin()
        }

        //协程的方式
        mBtnProfile2.click {
            LoginInterceptCoroutinesManager.get().checkLogin(loginAction = {
                gotoLoginPage()
            }, nextAction = {
                gotoProfilePage()
            })

        }
    }

    private fun checkLogin() {
        LoginInterceptThreadManager.get().checkLogin({
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
        mBtnProfile2 = view.findViewById(R.id.btn_profile_2)
    }

    //新版的Lifecycle监听
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_CREATE) {
            YYLogUtils.w("lifecycle的回调 create 方式3")
        } else if (event == Lifecycle.Event.ON_DESTROY) {
            YYLogUtils.w("lifecycle的回调 destroy 方式3")
        }
    }

}