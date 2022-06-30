package com.guadou.kt_demo.demo.demo5_network_request.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.bean.OkResult
import com.guadou.lib_baselib.ext.ControlledRunner
import com.guadou.lib_baselib.ext.SingleRunner
import com.guadou.lib_baselib.ext.checkNet
import com.guadou.lib_baselib.ext.toastError
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

/**
 * Repository一定要通过默认的构造方法传入进来
 * 在DI中注册之后会自动加载
 */
@HiltViewModel
class Demo5ViewModel @Inject constructor(
    private val mRepository: Demo5Repository,
    val savedState: SavedStateHandle
) : BaseViewModel() {

    private val _industryLD = MutableLiveData<List<Industry>>()
    val mIndustryLD: LiveData<List<Industry>> = _industryLD

    private val _schoollLD = MutableLiveData<List<SchoolBean>>()
    val mSchoolLD: LiveData<List<Industry>> = _industryLD

    var mContentLiveData: MutableLiveData<String> = MutableLiveData()

    /**
     * 网络请求串联-一个加载完成再请求下一个
     */
    fun netWorkSeries() {

        //检查网络的状态，可选用
        checkNet({

            viewModelScope.launch {
                //开始Loading
                loadStartProgress()

                val startTimeStamp = System.currentTimeMillis()
                val res = withContext(Dispatchers.Default) {
                    //异步执行
                    delay(1000)
                    return@withContext "1234"
                }
                val endTimeStamp = System.currentTimeMillis()
                YYLogUtils.w("res: $res  time: ${endTimeStamp - startTimeStamp}")

                //网络请求获取行业数据
                val industrys = mRepository.getIndustry()

                //返回的数据是封装过的，检查是否成功
                industrys.checkResult({
                    //成功
                    _industryLD.postValue(it)
                }, {
                    //失败
                    toastError(it)
                })

                //上面的请求执行完毕才会执行这个请求
                val schools = mRepository.getSchool()
                //返回的数据是封装过的，检查是否成功
                schools.checkSuccess {
                    _schoollLD.postValue(it)
                }

                //完成Loading
                loadHideProgress()
            }

        })

    }

    /**
     * 网络请求的并发处理，一同发出请求，等待对方一起返回数据
     */
    fun netSupervene() {
        //检查网络的状态，可选用
        checkNet({


            viewModelScope.launch {

                //开始Loading
                loadStartProgress()

                val industryResult = async {
                    mRepository.getIndustry()
                }

                val schoolResult = async {
                    mRepository.getSchool()
                }


                val localDBResult = async {
                    //loadDB()
                    YYLogUtils.w("thread:" + CommUtils.isRunOnUIThread())

                    delay(10000)
                }

                //一起处理数据
                val industry = industryResult.await()
                val school = schoolResult.await()

                //如果都成功了才一起返回
                if (industry is OkResult.Success && school is OkResult.Success) {
                    loadHideProgress()

                    _industryLD.postValue(industry.data!!)
                    _schoollLD.postValue(school.data!!)
                }


                YYLogUtils.e(localDBResult.await().toString() + "完成")

            }

        })
    }


    /**
     * 网络请求去重
     */
    private var controlledRunner = ControlledRunner<OkResult<List<Industry>>>()  //取消之前的
    private val singleRunner = SingleRunner()       //任务队列，排队，单独的
    fun netDuplicate() {

        viewModelScope.launch {
            //比较常用
            //取消上一次的，执行这一次的
            controlledRunner.cancelPreviousThenRun {
                return@cancelPreviousThenRun mRepository.getIndustry()
            }.checkSuccess {
                YYLogUtils.e("请求成功：")
                _industryLD.postValue(it)
            }

            //前一个执行完毕了，再执行下一个
//                singleRunner.afterPrevious {
//                    mMainRepository.getIndustry()
//                }.checkSuccess {
//                    YYLogUtils.e("测试重复的数据:" + it.toString())
//                }

        }

    }

    fun testRepository(): String {
        return mRepository.toString()
    }

    /**
     * 测试返回为null
     */
    fun testNullNet() {

        launchOnUI {
            loadStartProgress()
            val result =
                mRepository.getGiroAppointmentData("bearereyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC95eWpvYnMtYXBpLWRldi5ndWFiZWFuLmNvbVwvaW5kZXgucGhwXC9hcGlcL2VtcGxveWVlXC9hdXRoXC9sb2dpbiIsImlhdCI6MTYxNTY5OTA3NiwiZXhwIjoxNjUxNjk5MDc2LCJuYmYiOjE2MTU2OTkwNzYsImp0aSI6IkpsZVRuVkNGeUFaeFhmZloiLCJzdWIiOjExNjU0LCJwcnYiOiI4NjY1YWU5Nzc1Y2YyNmY2YjhlNDk2Zjg2ZmE1MzZkNjhkZDcxODE4In0.2NIA9HATAxJYGPwrWqRfxtEZZW4GTbj7SQzkG1krAD0")
            loadHideProgress()

            result.checkResult({
                YYLogUtils.w("testNullNet - success :$it")
            }, {
                YYLogUtils.w("testNullNet - error :$it")
            })
        }
    }

}