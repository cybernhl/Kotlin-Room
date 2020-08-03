package com.guadou.kt_demo.ui.demo7

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.dp2px
import com.guadou.lib_baselib.ext.engine.extLoad
import kotlinx.android.synthetic.main.activity_demo7.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 加载各种图片Glide
 *
 */
class Demo7Activity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo7Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo7

    @SuppressLint("SetTextI18n")
    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initLitener()
    }

    private fun initRV() {

    }


    private fun initLitener() {

        //.默认的加载图片
        btn_img_load_1.click {
            iv_img_1.extLoad("https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0", isCenterCrop = true)
        }


        // 带占位图
        btn_img_load_2.click {
            iv_img_2.extLoad(
                "http://pic73.nipic.com/file/20150723/455997_210818004000_2.jpg",
                R.mipmap.home_list_plachholder,
                isCenterCrop = true
            )
        }

        //占位图-圆形
        btn_img_load_3.click {
            iv_img_3.extLoad(
                "https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0",
                R.mipmap.home_list_plachholder,
                isCircle = true,
                isCenterCrop = true
            )
        }

        // 圆角
        btn_img_load_4.click {
            iv_img_4.extLoad(
                "https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0",
                R.mipmap.home_list_plachholder,
                roundRadius = dp2px(10f),
                isCenterCrop = true
            )
        }

        // 过度动画
        btn_img_load_5.click {

            iv_img_5.extLoad(
                "http://yyjobs-admin-dev.guabean.com/storage/202006/23/cXRkXxth9SZA9Zzsx7DcEFvWKQ0mfPkUkRlwHKSZ.png",
                R.mipmap.home_list_plachholder,
                isCenterCrop = true,
                isCrossFade = true
            )
        }


    }


}