package com.guadou.kt_demo.demo.demo14_mvi.mvi

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.lib_baselib.base.mvi.setState
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.OkResult
import kotlinx.coroutines.async

class Damo14ViewModel @ViewModelInject constructor(
    private val mRepository: Demo5Repository,
    @Assisted val savedState: SavedStateHandle
) : BaseViewModel() {

    private val _viewStates: MutableLiveData<Demo14ViewState> = MutableLiveData(Demo14ViewState())

    //只需要暴露一个LiveData，包括页面所有状态
    val viewStates: LiveData<Demo14ViewState> = _viewStates


    //获取数据
    fun fetchDatas() {

        //默认执行在主线程的协程-必须用（可选择默认执行在IO线程的协程）
        launchOnUI {

            //开始Loading
            loadStartProgress()

            val industryResult = async {
                mRepository.getIndustry()
            }

            val schoolResult = async {
                mRepository.getSchool()
            }

            //一起处理数据
            val industry = industryResult.await()
            val school = schoolResult.await()

            //如果都成功了才一起返回
            if (industry is OkResult.Success && school is OkResult.Success) {
                loadHideProgress()

                //设置多种LiveData
                _viewStates.setState {
                    copy(industrys = industry.data ?: emptyList(), schools = school.data ?: emptyList())
                }

            }

        }

    }

    //改变状态
    fun changeData(){
        _viewStates.setState {
            copy(isChanged = true)
        }
    }
}