package com.guadou.kt_demo.demo.demo14_mvi.mvi

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.lib_baselib.base.mvi.BaseViewState
import com.guadou.lib_baselib.base.mvi.setState
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.OkResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class Damo14ViewModel @ViewModelInject constructor(
    private val mRepository: Demo5Repository,
    @Assisted val savedState: SavedStateHandle
) : BaseViewModel() {

    private val _viewStates: MutableLiveData<Demo14ViewState> = MutableLiveData(Demo14ViewState())

    //只需要暴露一个LiveData，包括页面所有状态
    val viewStates: LiveData<Demo14ViewState> = _viewStates

    //Action分发入口
    fun dispatch(action: DemoAction) {
        when (action) {
            is DemoAction.RequestIndustry -> requestIndustry()
            is DemoAction.RequestSchool -> requestSchool()
            is DemoAction.RequestAllData -> getTotalData()
            is DemoAction.UpdateChanged -> changeData(action.isChange)
        }
    }

    //获取行业数据
    private fun requestIndustry() {

        viewModelScope.launch {
            //开始Loading
            loadStartLoading()

            val result = mRepository.getIndustry()

            result.checkSuccess {
                _viewStates.setState {
                    copy(industrys = it ?: emptyList())
                }
            }

            loadHideProgress()
        }
    }

    //获取学校数据
    private fun requestSchool() {

        viewModelScope.launch {
            //开始Loading
            loadStartLoading()

            val result = mRepository.getSchool()

            result.checkSuccess {
                _viewStates.setState {
                    copy(schools = it ?: emptyList())
                }
            }

            loadHideProgress()
        }
    }

    //获取全部数据
    private fun getTotalData() {

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
    private fun changeData(isChanged: Boolean) {
        _viewStates.setState {
            copy(isChanged = isChanged)
        }
    }

    data class Demo14ViewState(
        val industrys: List<Industry> = emptyList(),
        val schools: List<SchoolBean> = emptyList(),
        var isChanged: Boolean = false
    ) : BaseViewState()


    //如果想再度封装，也可以把回调的结果封装成类似Action的对象，由页面判断回调的是哪一种类型，进行相关的操作
    //这样就不需要使用LiveData回调了，LiveData就只是作为保存数据的功能，由DemoEvent回调
//    sealed class DemoEvent {
//        object PopBack : DemoEvent()
//        data class ErrorMessage(val message: String) : DemoEvent()
//    }

    sealed class DemoAction {
        object RequestIndustry : DemoAction()
        object RequestSchool : DemoAction()
        object RequestAllData : DemoAction()

        data class UpdateChanged(val isChange: Boolean) : DemoAction()
    }

}