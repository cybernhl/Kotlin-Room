package com.guadou.kt_demo.demo.demo14_mvi.mvvm1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DemoViewModel @Inject constructor(
    private val mRepository: Demo5Repository,
    val savedState: SavedStateHandle
) : BaseViewModel() {

    val liveData = MutableLiveData<List<Industry>?>()

    //获取行业数据
    fun requestIndustry() {

        viewModelScope.launch {
            //开始Loading
            loadStartLoading()

            val result = mRepository.getIndustry()

            result.checkResult({
                //处理成功的信息
                toast("list:$it")
                liveData.value = it
            }, {
                //失败
                liveData.value = null
            })

            loadHideProgress()
        }
    }
}