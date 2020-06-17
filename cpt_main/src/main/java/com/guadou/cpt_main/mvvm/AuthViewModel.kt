package com.guadou.cpt_main.mvvm

import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.toast

class AuthViewModel(private val mAuthRepository: AuthRepository) : BaseViewModel() {

    fun getServiceTime() {

        launchOnUI {

            loadStartProgress()

            val serverTime = mAuthRepository.getServerTime()

            serverTime.checkSuccess {
                toast(it.timestamps.toString())
            }

            loadSuccess()

        }

    }

}