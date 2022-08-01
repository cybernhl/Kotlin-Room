package com.guadou.kt_demo.demo.demo11_fragment_navigation.nav2

import androidx.lifecycle.MutableLiveData
import com.guadou.lib_baselib.base.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Nav2ViewModel  @Inject constructor():BaseViewModel() {

    var mEtOneText = MutableLiveData<String>()

    var mEtTwoText = MutableLiveData<String>()

}