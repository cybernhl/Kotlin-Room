package com.guadou.kt_zoom.mvvm

import androidx.lifecycle.MutableLiveData
import com.guadou.kt_zoom.bean.Industry
import com.guadou.kt_zoom.bean.SchoolBean
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.testxiecheng.base.OkResult

import kotlinx.coroutines.async
import okhttp3.Call
import java.lang.Exception

class MainViewModel(private val mMainRepository: MainRepository) : BaseViewModel() {

    val mIndustryLiveData = MutableLiveData<List<Industry>>()
    val mSchoolliveData = MutableLiveData<List<SchoolBean>>()

    //获取行业数据
    fun getIndustry() {

        //默认执行
        launchOnUI {

            loadStartProgress()

            val industryResult = async { mMainRepository.getIndustry() }

            val schoolResult = async { mMainRepository.getSchool() }


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


            //可以直接data获取到成功的数据
            var industryStr = ""
            val okResultIndustry = industryResult.await()
            if (okResultIndustry is OkResult.Success) {
                val beanIndustry = okResultIndustry.data
                industryStr = beanIndustry.toString()
            }

            var schoolStr = ""
            val okResultSchool = schoolResult.await()
            if (okResultSchool is OkResult.Success) {
                val beanSchool = okResultSchool.data
                schoolStr = beanSchool.toString()
            }

            YYLogUtils.e(industryStr + " " + schoolStr)




            //也可以使用函数的方式判断
//            industryResult.checkResult({
//                loadSuccess()
//                mIndustryLiveData.postValue(it)
//            }, {
//                loadError(it)
//                toast(it!!)
//                mIndustryLiveData.postValue(null)
//            })
//
//
//            schoolResult.checkResult({
//                loadSuccess()
//                mSchoolliveData.postValue(it)
//            }, {
//                loadError(it)
//                toast(it!!)
//                mSchoolliveData.postValue(null)
//            })


        }

    }

}