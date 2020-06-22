package com.guadou.kt_zoom.ui

import android.widget.Toast
import com.guadou.kt_zoom.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.image_select.openImageSelect
import com.guadou.lib_baselib.ext.toast
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.listener.OnResultCallbackListener
import kotlinx.android.synthetic.main.activity_image_select.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class ImageSelectActivity : BaseActivity<BaseViewModel>() {

    private var mImageSelectDatas = arrayListOf<LocalMedia>()

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_image_select

    override fun startObserve() {

    }

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

            })

        }
    }


}