package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.viewmodel

import androidx.lifecycle.*
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.kt_demo.demo.demo5_network_request.mvvm.Demo5Repository
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.bean.NewsBean
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.OkResult
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.utils.okhttp.CallBackUtil
import com.guadou.lib_baselib.utils.okhttp.OkhttpUtil
import com.guadou.testxiecheng.base.BaseBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.Call
import okhttp3.Response
import java.lang.Exception
import javax.inject.Inject
import kotlin.concurrent.thread
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


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


    fun suspendSth() {

        viewModelScope.launch {

            val school = mRepository.getSchool()

            if (school is OkResult.Success) {

                val lastSchool = handleSchoolData(school.data)

                YYLogUtils.w("处理过后的School:" + lastSchool)
            }

        }



        viewModelScope.launch {


            val school = mRepository.getSchool()  //一个是使用Retrofit + suspend

            try {
                val industry = getIndustry()        //一个是OkHttpUtils回调的方式
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private suspend fun getIndustry(): String? {
        return suspendCancellableCoroutine { cancellableContinuation ->

            OkhttpUtil.okHttpGet("http://www.baidu.com/api/industry", object : CallBackUtil.CallBackString() {

                override fun onFailure(call: Call, e: Exception) {
                    cancellableContinuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: String?) {
                    cancellableContinuation.resume(response)
                }

            })
        }
    }

    private var mCancellableContinuation: CancellableContinuation<SchoolBean?>? = null
    private var mSchoolList: List<SchoolBean>? = null

    private suspend fun handleSchoolData(data: List<SchoolBean>?): SchoolBean? {

        mSchoolList = data

        return suspendCancellableCoroutine {

            mCancellableContinuation = it

            YYLogUtils.w("开启线程睡眠5秒再说")
            thread {
                Thread.sleep(5000)

                YYLogUtils.w("就是不返回，哎，就是玩...")

                it?.resume(mSchoolList?.last(), null)
            }

        }
    }

    //我想什么时候返回就什么时候返回
    fun resumeCoroutine() {

        YYLogUtils.w("点击恢复协程-返回数据")

        if (mCancellableContinuation?.isCancelled == true) {
            return
        }

        mCancellableContinuation?.resume(mSchoolList?.last(), null)

    }


}