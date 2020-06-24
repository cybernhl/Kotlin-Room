package com.guadou.kt_zoom.mvvm

import androidx.lifecycle.MutableLiveData
import com.guadou.cs_cptservices.Constants
import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.kt_zoom.http.CachedRetrofit
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.ControlledRunner
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.testxiecheng.base.OkResult
import kotlinx.coroutines.*

import okhttp3.Call
import java.lang.Exception

class MainViewModel(private val mMainRepository: MainRepository) : BaseViewModel() {

    val mIndustryLiveData = MutableLiveData<List<Industry>>()
    val mSchoolliveData = MutableLiveData<List<SchoolBean>>()

    //获取行业数据
    fun getIndustry() {

        //默认执行
        launchOnUI {

            loadStartLoading()

            val industryResult = async {
                mMainRepository.getIndustry()
            }
            val schoolResult = async {
                mMainRepository.getSchool()
            }

            val first = System.currentTimeMillis()

//            loadDB()  //默认是阻塞的,串联的

            async {
                //loadDB()    //加上aync就是非阻塞的 并发的  ，也可以不定义方法直接用aync包裹代码

                YYLogUtils.w("thread5:" + CommUtils.isRunOnUIThread())
                YYLogUtils.w("thread戒指数据库")
                delay(3000)
            }

            val last = System.currentTimeMillis()

            YYLogUtils.w("thread4:" + (last - first).toString())
            YYLogUtils.w("thread4:" + CommUtils.isRunOnUIThread())


            //也可以使用函数的方式判断
            industryResult.await().checkResult({
                loadSuccess()
                mIndustryLiveData.postValue(it)
            }, {
                loadError(it)
                toast(it!!)
                mIndustryLiveData.postValue(null)
            })

            schoolResult.await().checkResult({
                loadSuccess()
                mSchoolliveData.postValue(it)
            }, {
                loadError(it)
                toast(it!!)
                mSchoolliveData.postValue(null)
            })

//            loadError("Custom Error")
//            loadSuccess()
        }

    }

    //如果想执行一个额外的异步线程，可以这么使用
    private suspend fun loadDB() = coroutineScope {
        launch {
            //阻塞式的
            YYLogUtils.w("thread3:" + CommUtils.isRunOnUIThread())
            YYLogUtils.w("thread戒指数据库")
            delay(3000)
        }

    }

}