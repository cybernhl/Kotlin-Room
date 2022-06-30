package com.guadou.kt_demo.demo.demo7_imageload_glide

import androidx.lifecycle.MutableLiveData
import com.guadou.lib_baselib.base.vm.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class Demo7ViewModel @Inject constructor() : BaseViewModel() {

    val img1LiveData = MutableLiveData<String>()
    val img2LiveData = MutableLiveData<String>()
    val img3LiveData = MutableLiveData<String>()
    val img4LiveData = MutableLiveData<String>()
    val img5LiveData = MutableLiveData<String>()


    fun setImage1() {
//        img1LiveData.value = "http://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0"
        img1LiveData.value = "http://www.guophone.com.cn/v1/files/cccc26ae0aa24aaf958b4974d337c6dc/download"
    }

    fun setImage2() {
        img2LiveData.value = "http://pic73.nipic.com/file/20150723/455997_210818004000_2.jpg"
    }

    fun setImage3() {
        img3LiveData.value = "http://i02piccdn.sogoucdn.com/438750e61adc26dc"
    }

    fun setImage4() {
        img4LiveData.value = "http://i03piccdn.sogoucdn.com/965cc2a9ea62fc08"
    }

    fun setImage5() {
        img5LiveData.value =
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/cXRkXxth9SZA9Zzsx7DcEFvWKQ0mfPkUkRlwHKSZ.png"
    }

}