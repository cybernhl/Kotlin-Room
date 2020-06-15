package com.guadou.kt_zoom.mvvm

import androidx.lifecycle.MutableLiveData
import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.lib_baselib.base.BaseViewModel
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

            YYLogUtils.w("thread1:" + CommUtils.isRunOnUIThread())
            val industryResult = async {
                YYLogUtils.w("thread2:" + CommUtils.isRunOnUIThread())
                mMainRepository.getIndustry()
            }

//            val schoolResult = async {
//                YYLogUtils.w("thread3:" + CommUtils.isRunOnUIThread())
//                mMainRepository.getSchool()
//            }


            //协程内部嵌套普通的网络请求回调之类的也是可以的
//            OkhttpUtil.okHttpGet("http://yyjobs-api-dev.guabean.com/index.php/api/employee/extra/school",
//                null,
//                mapOf(
//                    "Content-Type" to Constants.NETWORK_CONTENT_TYPE,
//                    "Accept" to Constants.NETWORK_ACCEPT_V1
//                ),
//                object : CallBackUtil.CallBackString() {
//                    override fun onFailure(call: Call?, e: Exception?) {
//                        e?.printStackTrace()
//                    }
//
//                    override fun onResponse(call: Call?, response: String?) {
//
//                        YYLogUtils.e(response)
//
//                        loadSuccess()
//                    }
//
//                })

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
            //可以直接data获取到成功的数据
//            var industryStr = ""
//            val okResultIndustry = industryResult.await()
//            if (okResultIndustry is OkResult.Success) {
//                val beanIndustry = okResultIndustry.data
//                industryStr = beanIndustry.toString()
//            }

//            var schoolStr = ""
//            val okResultSchool = schoolResult.await()
//            if (okResultSchool is OkResult.Success) {
//                val beanSchool = okResultSchool.data
//                schoolStr = beanSchool.toString()
//            }

//            YYLogUtils.e("请求结果:" + industryStr + " " + schoolStr)
//            toast(industryStr + " " + schoolStr)

//            toast("网络请求完成")



            //也可以使用函数的方式判断
            industryResult.await().checkResult({
                loadSuccess()
                mIndustryLiveData.postValue(it)
            }, {
                loadError(it)
                toast(it!!)
                mIndustryLiveData.postValue(null)
            })

//
//            schoolResult.checkResult({
//                loadSuccess()
//                mSchoolliveData.postValue(it)
//            }, {
//                loadError(it)
//                toast(it!!)
//                mSchoolliveData.postValue(null)
//            })

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