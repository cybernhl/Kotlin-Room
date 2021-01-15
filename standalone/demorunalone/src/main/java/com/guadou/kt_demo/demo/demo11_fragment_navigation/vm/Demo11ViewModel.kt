package com.guadou.kt_demo.demo.demo11_fragment_navigation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guadou.lib_baselib.base.BaseViewModel

class Demo11ViewModel : BaseViewModel() {

    val mBackOneLiveData = MutableLiveData<String>()

    fun testToast(): String {
        return "Test Toast"
    }
}