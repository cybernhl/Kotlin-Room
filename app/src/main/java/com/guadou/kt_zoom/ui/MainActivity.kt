package com.guadou.kt_zoom.ui

import android.graphics.Color
import androidx.lifecycle.Observer
import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.lib_baselib.base.BasePlaceHolderActivity
import com.guadou.lib_baselib.ext.*

import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderView

import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : BasePlaceHolderActivity<MainViewModel>() {

    override fun initVM(): MainViewModel = getViewModel()

    override fun startObserve() {

        mViewModel.mIndustryLiveData.observe(this, Observer {

            YYLogUtils.w("Industory数据加载完成")

//            tv_request_content.text = it?.toString()

//            iv_image.load(it[0].industry_image_new)

        })

        mViewModel.mSchoolliveData.observe(this, Observer {

            YYLogUtils.w("School数据加载完成")
        })
    }

    override fun inflateLayoutById(): Int = R.layout.activity_main

    override fun inflatePlaceHolderLayoutRes(): Int = R.layout.layout_placeholder1

    override fun init() {

        setStatusBarColor(Color.WHITE)


        initListener()

    }

    private fun initListener() {

        toast("Test Toast")

        btn_toast.setOnClickListener {

            //            val dateMills = 2032934294.toDateString()
//            toastSuccess("测试成功tushi:" + dateMills)

        }

        btn_request.setOnClickListener {

            mViewModel.getIndustry()

        }

        btn_load_img.setOnClickListener {

            iv_image.load("https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0")
        }


        btn_jump_auth.setOnClickListener {
            //            YYRouterService.mainComponentServer.startAuthActivity()
            DemoTestActivity.startInstance()
        }

    }

    override fun showStateLoading() {
        mGloadingHolder.withData(GloadingPlaceHolderView.NEED_LOADING_STATUS_MAGRIN_TITLE)
        super.showStateLoading()
    }

}
