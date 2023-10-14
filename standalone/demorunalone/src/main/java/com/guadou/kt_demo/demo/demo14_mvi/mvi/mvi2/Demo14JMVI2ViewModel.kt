package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.lib_baselib.bean.OkResult
import dagger.hilt.android.lifecycle.HiltViewModel

import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class Demo14JMVI2ViewModel @Inject constructor(
    private val mRepository: Demo5Repository,
    val savedState: SavedStateHandle
) : BaseVB2ViewModel<Demo14Intent, Demo14State, Demo14Effect>() {

    override fun initUiState(): Demo14State {
        return return Demo14State(IndustryUiState.INIT, SchoolUiState.INIT, LoadUiState.Idle);
    }

    override fun handleIntent(intent: Demo14Intent) {
        when (intent) {
            Demo14Intent.GetSchool -> requestSchool()
            else -> {}
        }
    }

    //获取学校数据
    private fun requestSchool() {

        viewModelScope.launch {

            //请求Loading
            updateUiState {
                copy(loadUiState = LoadUiState.Loading(true))
            }

            val result = mRepository.getSchool()

            if (result is OkResult.Success) {

                val data = result.data


                //请求成功
                updateUiState {
                    //调用data class 的copy 赋值更方便
                    copy(
                        schoolUiState = SchoolUiState.SUCCESS(data ?: emptyList()),
                        loadUiState = LoadUiState.ShowContent
                    )
                }

                sendEffect(Demo14Effect.NavigationToSchoolDetail(data!![0].school_id))

            } else {

                val message = (result as OkResult.Error).exception.message
                updateUiState {
                    copy(loadUiState = LoadUiState.Error(message ?: "未知错误"))
                }

            }
        }
    }


}