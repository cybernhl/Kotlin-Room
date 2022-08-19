package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.TextView
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.Login
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginCallback
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.dynamic.DynamicProxyUtils
import com.guadou.lib_baselib.base.fragment.BaseVMFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils


class Demo3OneFragment : BaseVMFragment<EmptyViewModel>() {

    private lateinit var mBtnCleanToken: TextView
    private lateinit var mBtnProfile: TextView
    private lateinit var mBtnProfile2: TextView

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
//            gotoProfilePage2()

//            DynamicProxyUtils.hookAms()
            gotoProfilePage()
        }

        //Intent的方式
        mBtnProfile2.click {

            if (!LoginManager.isLogin()) {
                val intent = Intent(mActivity, Demo3Activity::class.java)
                intent.addCategory("showDialog")
                intent.data = Uri.parse("android://ktdemo/demo3")
                gotoLoginPage(intent)
            } else {
                gotoNextPage()
            }

        }
    }

    fun gotoLoginPage(targetIntent: Intent) {
        val intent = Intent(mActivity, LoginDemoActivity::class.java)
        intent.putExtra("targetIntent", targetIntent)
        startActivity(intent)
    }

    fun gotoNextPage() {
        val intent = Intent(mActivity, ProfileDemoActivity::class.java)
        startActivity(intent)
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
        mBtnProfile2 = view.findViewById(R.id.btn_profile2)
    }

}