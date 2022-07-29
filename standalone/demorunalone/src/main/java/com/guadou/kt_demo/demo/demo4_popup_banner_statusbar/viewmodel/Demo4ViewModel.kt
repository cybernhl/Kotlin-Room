package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.viewmodel

import androidx.lifecycle.*
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.bean.NewsBean
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.OkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class Demo4ViewModel @Inject constructor(
    val mRepository: Demo5Repository,
    val savedState: SavedStateHandle
) : BaseViewModel() {

    val channel = Channel<String>(Channel.CONFLATED)

    private val _searchLD = MutableLiveData<String>()
    val searchLD: LiveData<String> = _searchLD

    private val _searchFlow = MutableStateFlow("")
    val searchFlow: StateFlow<String> = _searchFlow

    val sharedFlow = MutableSharedFlow<String>()

    fun changeSearch(keyword: String) {
        viewModelScope.launch {
            sharedFlow.emit(keyword)

            _searchFlow.value = keyword
            _searchLD.value = keyword
            channel.trySend(keyword)
        }

    }


    fun getNewsDetail(): LiveData<NewsBean?> {

        return liveData {

            val detail = mRepository.fetchNewsDetail()

            if (detail is OkResult.Success) {
                emit(detail.data)
            } else {
                emit(null)
            }

        }

    }


    private val _stateFlow = MutableStateFlow("")
    val stateFlow: StateFlow<String> = _searchFlow

    fun changeState() {

        viewModelScope.launch {
            val detail = mRepository.changeState()

            detail.checkSuccess {

                //进一系列的数据合流

                //进行一系列的排序、转换之后设置给Flow

                _stateFlow.value = it ?: ""
            }

        }

    }


}