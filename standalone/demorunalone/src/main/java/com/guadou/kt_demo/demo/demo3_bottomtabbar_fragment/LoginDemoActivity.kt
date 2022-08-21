package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment

import android.content.Intent
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo3LoginBinding
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function.FunctionManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.loginIntercepter.LoginInterceptChain
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptCoroutinesManager
import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.thread.LoginInterceptThreadManager
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.SP
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.putString
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 登录页面
 */
class LoginDemoActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo3LoginBinding>() {

    private var mTargetIntent: Intent? = null
    private var mTargetType = 0

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, LoginDemoActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataFromIntent(intent: Intent) {
        mTargetIntent = intent.getParcelableExtra("targetIntent")
        mTargetType = intent.getIntExtra("type", 0)
        YYLogUtils.w("mTargetIntent:" + mTargetIntent)
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo3_login)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun doLogin() {
            showStateLoading()

            CommUtils.getHandler().postDelayed({
                showStateSuccess()
                SP().putString(Constants.KEY_TOKEN, "abc")


                //发送通知的方式
                FunctionManager.get().finishLogin()

                //线程
                LoginInterceptThreadManager.get().loginFinished()

                //协程
                LoginInterceptCoroutinesManager.get().loginFinished()

                //方法池的方式
                FunctionManager.get().invokeFunction("gotoProfilePage")

                setResult(-1, Intent().apply { putExtra("type", mTargetType) })   //设置Result

                if (mTargetIntent != null) {
                    startActivity(mTargetIntent)
                }

                //拦截器放行
                LoginInterceptChain.loginFinished()

                finish()

            }, 500)

        }

    }
}