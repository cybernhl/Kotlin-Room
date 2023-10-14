package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2


import androidx.lifecycle.viewModelScope
import com.guadou.lib_baselib.base.vm.BaseViewModel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


abstract class BaseVB2ViewModel<UiIntent : IUiIntent, UiState : IUiState, UIEffect : IUIEffect> : BaseViewModel() {

    private val _uiStateFlow  by lazy {
        MutableStateFlow(value = initUiState())
    }
    val uiStateFlow: StateFlow<UiState> by lazy { _uiStateFlow.asStateFlow() }

    //页面事件的 Channel 分发
    private val _uiIntentFlow = Channel<UiIntent>(Channel.UNLIMITED)

    //更新页面状态
    fun updateUiState(reducer: UiState.() -> UiState) {
        _uiStateFlow.update { reducer(_uiStateFlow.value) }
    }

    //更新State
    fun <T> sendUiState2(reducer: T.() -> T) {

    }

    //发送页面事件
    fun sendUiIntent(uiIntent: UiIntent) {
        viewModelScope.launch {
            _uiIntentFlow.send(uiIntent)
        }
    }

    init {
        // 这里是通过Channel的方式自动分发的。
        viewModelScope.launch {
            //收集意图 （观察者模式改变之后就自动更新）用于协程通信的，所以需要在协程中调用
            _uiIntentFlow.consumeAsFlow().collect { intent ->
                handleIntent(intent)
            }
        }

    }

    //一次性事件，无需更新
    private val _effectFlow = MutableSharedFlow<UIEffect>()
    val uiEffectFlow: SharedFlow<UIEffect> by lazy { _effectFlow.asSharedFlow() }

    //两种方式发射
    protected fun sendEffect(builder: suspend () -> UIEffect?) = viewModelScope.launch {
        builder()?.let { _effectFlow.emit(it) }
    }

    //两种方式发射
    protected suspend fun sendEffect(effect: UIEffect) = _effectFlow.emit(effect)


    //每个页面的 UiState 都不相同，必须实自己去创建
    protected abstract fun initUiState(): UiState

    //每个页面处理的 UiIntent 都不同，必须实现自己页面对应的状态处理
    protected abstract fun handleIntent(intent: UiIntent)

}