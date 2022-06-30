package com.guadou.kt_demo.demo.demo11_fragment_navigation.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guadou.lib_baselib.base.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class Demo11ViewModel @Inject constructor() : BaseViewModel() {

    private val _backOneLiveData = MutableLiveData<String>()
    val mBackOneLD: LiveData<String> get() = _backOneLiveData

    private val _msgFlow = MutableStateFlow("")
    val mMsgFlow: StateFlow<String> = _msgFlow.asStateFlow()

    fun setCallbackValue() {
        _backOneLiveData.value = "test value"
        _msgFlow.value = "test flow value"
    }

    fun testToast(): String {
        return "Test Toast"
    }
}