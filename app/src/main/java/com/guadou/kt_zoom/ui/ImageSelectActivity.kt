package com.guadou.kt_zoom.ui

import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.mvvm.ImageSelectViewModel
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.ext.engine.image_select.openCamera
import com.guadou.lib_baselib.ext.engine.image_select.openImageSelect
import com.guadou.lib_baselib.utils.ETMoneyValueFilter
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_image_select.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ImageSelectActivity : BaseActivity<ImageSelectViewModel>() {

    private var mImageSelectDatas = arrayListOf<LocalMedia>()

    override fun initVM(): ImageSelectViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_image_select

    override fun startObserve() {

    }

    @ExperimentalCoroutinesApi
    override fun init() {

        btn_select.click {

            openImageSelect(mImageSelectDatas, object : OnResultCallbackListener<LocalMedia> {

                override fun onResult(result: MutableList<LocalMedia>?) {
                    result?.also {
                        mImageSelectDatas.clear()
                        mImageSelectDatas.addAll(it)
                    }
                }

                override fun onCancel() {
                    toast("取消了选择")
                }

            }, selectNum = 6, canTackPhoto = true, canCrop = true, canCompress = true)

        }

        btn_carme.click {

            openCamera(mImageSelectDatas,
                object : OnResultCallbackListener<LocalMedia> {
                    override fun onResult(result: MutableList<LocalMedia>?) {

                        result?.also {
                            mImageSelectDatas.clear()
                            mImageSelectDatas.addAll(it)
                        }

                    }

                    override fun onCancel() {
                        toast("取消了选择")
                    }

                }, true
            )
        }

        //转换
        btn_transfrom.click {
//            val str = "abc,def,ght,lkh"
//            YYLogUtils.e(str.toCommaList().toString())
//
//            val list = listOf("taiwan","xianggang","aomen")
//            YYLogUtils.e(list.toCommaStr())


//            val money = "43178.9"
//            YYLogUtils.e(money.formatMoney())


            mViewModel.startCountDown()

        }

        et_input.textChangeCallback {
            YYLogUtils.e(it)
        }

        et_input.filters = arrayOf(ETMoneyValueFilter().setDigits(3))

    }


}