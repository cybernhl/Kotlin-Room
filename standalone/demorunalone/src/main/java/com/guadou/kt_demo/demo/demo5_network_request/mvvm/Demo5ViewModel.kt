package com.guadou.kt_demo.demo.demo5_network_request.mvvm

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.kt_demo.demo.demo5_network_request.bean.SchoolBean
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.ControlledRunner
import com.guadou.lib_baselib.ext.SingleRunner
import com.guadou.lib_baselib.ext.checkNet
import com.guadou.lib_baselib.ext.toastError
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.testxiecheng.base.OkResult
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

/**
 * Repository一定要通过默认的构造方法传入进来
 * 在DI中注册之后会自动加载
 */
class Demo5ViewModel @ViewModelInject constructor(
    private val mRepository: Demo5Repository,
    @Assisted val savedState: SavedStateHandle
) : BaseViewModel() {

    val mIndustryLiveData = MutableLiveData<List<Industry>>()
    val mSchoolliveData = MutableLiveData<List<SchoolBean>>()

    /**
     * 网络请求串联-一个加载完成再请求下一个
     */
    fun netWorkSeries() {

        //检查网络的状态，可选用
        checkNet({

            //默认执行在主线程的协程-必须用（可选择默认执行在IO线程的协程）
            launchOnUI {

                //开始Loading
                loadStartProgress()

                val industrys = mRepository.getIndustry()

                //返回的数据是封装过的，检查是否成功
                industrys.checkResult({
                    //成功
                    mIndustryLiveData.postValue(it)
                }, {
                    //失败
                    toastError(it)
                })

                //上面的请求执行完毕才会执行这个请求
                val schools = mRepository.getSchool()
                //返回的数据是封装过的，检查是否成功
                schools.checkSuccess {
                    mSchoolliveData.postValue(it)
                }
                //只是返回成功的 2种方式都可以
//                if (schools is OkResult.Success) {
//                    mSchoolliveData.postValue(schools.data)
//                }

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


                val localDBResult = async {
                    //loadDB()    //加上aync就是非阻塞的 并发的  ，也可以不定义方法直接用aync包裹代码
                    YYLogUtils.w("thread:" + CommUtils.isRunOnUIThread())

                    delay(10000)
                }

                //一起处理数据
                val industry = industryResult.await()
                val school = schoolResult.await()

                //如果都成功了才一起返回
                if (industry is OkResult.Success && school is OkResult.Success) {
                    loadHideProgress()

                    mIndustryLiveData.postValue(industry.data)
                    mSchoolliveData.postValue(school.data)
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

        checkNet({

            launchOnUI {
                //就用它 最好用
                //取消上一次的，执行这一次的
                controlledRunner.cancelPreviousThenRun {
                    return@cancelPreviousThenRun mRepository.getIndustry()
                }.checkSuccess {
                    YYLogUtils.e("请求成功：")
                    mIndustryLiveData.postValue(it)
                }

                //前一个执行完毕了，再执行下一个
//                singleRunner.afterPrevious {
//                    mMainRepository.getIndustry()
//                }.checkSuccess {
//                    YYLogUtils.e("测试重复的数据:" + it.toString())
//                }

            }

        })
    }
}