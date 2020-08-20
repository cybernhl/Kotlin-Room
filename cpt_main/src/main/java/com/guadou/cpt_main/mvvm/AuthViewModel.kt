package com.guadou.cpt_main.mvvm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.toast

class AuthViewModel @ViewModelInject constructor(
    private val mAuthRepository: AuthRepository,
    @Assisted val savedState: SavedStateHandle
) : BaseViewModel() {

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