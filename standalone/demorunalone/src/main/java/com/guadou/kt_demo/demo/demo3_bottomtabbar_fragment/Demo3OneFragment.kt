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
        val type_profile_page = 0x21
        val type_dialog = 0x22
        val type_picker = 0x23
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

            //启动动态代理
            DynamicProxyUtils.hookAms()

            gotoProfilePage()
        }

        //Intent的方式
        mBtnProfile2.click {

            if (!LoginManager.isLogin()) {
                val intent = Intent(mActivity, Demo3Activity::class.java)
                intent.addCategory(type_dialog.toString())
                intent.data = Uri.parse("android://ktdemo/demo3")
                gotoLoginPage(intent)

//                val intent = Intent(mActivity, LoginDemoActivity::class.java)
//                intent.putExtra("type", type_dialog)
//                startActivityForResult(intent, 1010)

            } else {
                gotoNextPage()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        YYLogUtils.w("基于Result实现")
        if (requestCode == 1010 && resultCode == -1) {

            when (data?.getIntExtra("type", 0) ?: 0) {
                type_profile_page -> {
                    //跳转到个人中心页面
                    toast("跳转到个人中心页面")
                }
                type_dialog -> {
                    //展示弹窗
                    toast("展示弹窗")
                }
                type_picker -> {
                    //展示PickerView
                    toast("展示PickerView")
                }
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