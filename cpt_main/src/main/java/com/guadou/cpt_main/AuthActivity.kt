package com.guadou.cpt_main

import android.content.Intent
import com.guadou.cpt_main.mvvm.AuthViewModel
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.utils.CommUtils
import kotlinx.android.synthetic.main.activity_auth.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class AuthActivity : BaseActivity<AuthViewModel>() {

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

        btn_login.setOnClickListener {
            mViewModel.getServiceTime()
        }
    }

}