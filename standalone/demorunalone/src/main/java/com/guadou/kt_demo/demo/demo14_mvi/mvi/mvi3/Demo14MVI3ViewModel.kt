package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi3

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.lib_baselib.base.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class Demo14MVI3ViewModel @Inject constructor(
    private val mRepository: Demo5Repository,
    val savedState: SavedStateHandle
) : BaseViewModel() {

    //创建意图管道，容量无限大 （可以用Flow的监听效果，观察者模式改变之后就自动更新）
    //为什么用Channel不用Flow，是因为只需要单向流动就行了，Channel是单对单，Flow的单对多
    //Channel的发送send和接收receive,发出的事件只能被接收一次，接收之后就不能再次接收了，很适合这个场景。 万一屏幕旋转重建了也不会再度触发事件。
    val mainIntentChannel = Channel<MVI3Intent>(Channel.UNLIMITED)

    //可变状态数据流（读写）（StateFlow替代LiveData，难道每一个页面都需要定义一个）
    private val _uiState = MutableStateFlow<MVI3State>(MVI3State.Idle)
    //可观察状态数据流（只读）
    val uiState: StateFlow<MVI3State> get() = _uiState


    init {
        //之前我们是用dispatch主动分发，这里是通过Channel的方式自动分发的。
        viewModelScope.launch {
            //收集意图 （观察者模式改变之后就自动更新）用于协程通信的，所以需要在协程中调用
            mainIntentChannel.consumeAsFlow().collect { value ->
                when (value) {
                    //根据意图事件分别调用不同的方法
                    is MVI3Intent.GetIndustry -> requestIndustry()
                    is MVI3Intent.GetSchool -> requestSchool()
                    else -> {}
                }
            }
        }
    }


    //获取行业数据
    private fun requestIndustry() {

        viewModelScope.launch {
            //请求Loading
            _uiState.value = MVI3State.Loading

            val result = mRepository.getIndustry()

            result.checkResult(success = {
                //请求成功
                _uiState.value = MVI3State.Industries(it!!)

            }, error = {
                //请求失败
                MVI3State.Error(it ?: "UnKnown Error")
            })

        }

    }

    //获取学校数据
    private fun requestSchool() {

        viewModelScope.launch {
            //请求Loading
            _uiState.value = MVI3State.Loading

            val result = mRepository.getSchool()

            result.checkResult(success = {
                //请求成功
                _uiState.value = MVI3State.Schools(it!!)

            }, error = {
                //请求失败
                MVI3State.Error(it ?: "UnKnown Error")
            })

        }
    }


}
