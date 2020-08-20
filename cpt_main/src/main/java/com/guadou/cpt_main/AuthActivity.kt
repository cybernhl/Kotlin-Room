package com.guadou.cpt_main

import android.content.Intent
import com.guadou.cpt_main.mvvm.AuthViewModel
import com.guadou.cpt_main.others.MemberServer
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : BaseActivity<AuthViewModel>() {

    @Inject
    lateinit var userServer: MemberServer

    companion object {
        fun startInstance() {
            val context = CommUtils.getContext()
            val intent = Intent(context, AuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }

    override fun initVM(): AuthViewModel = getViewModel()

    override fun startObserve() {
    }

    override fun inflateLayoutById(): Int = R.layout.activity_auth

    override fun init() {


        YYLogUtils.e("viewmodel:" + mViewModel.toString())
        YYLogUtils.e("userServer:" + userServer.toString())
        userServer.testUser()

        btn_login.setOnClickListener {
            mViewModel.getServiceTime()
        }
    }

}