package com.guadou.kt_zoom.ui

import android.Manifest
import android.annotation.SuppressLint
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.bean.Price
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.kt_zoom.mvvm.UserPresenter
import com.guadou.lib_baselib.annotation.NetWork
import com.guadou.lib_baselib.base.BasePlaceHolderActivity
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.engine.extLoad
import com.guadou.lib_baselib.ext.engine.extRequestPermission
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toDateString
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.view.LoadingDialogManager
import com.guadou.lib_baselib.view.gloading.GloadingPlaceHolderView
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.getViewModel


class MainActivity : BasePlaceHolderActivity<MainViewModel>() {

    private val userPresenter: UserPresenter by inject()
    override fun initVM(): MainViewModel = getViewModel()

    override fun startObserve() {

        mViewModel.mIndustryLiveData.observe(this, Observer {

            tv_request_content.text = it?.toString()

//            iv_image.load(it[0].industry_image_new)

        })

        mViewModel.mSchoolliveData.observe(this, Observer {

            YYLogUtils.w("School数据加载完成")
        })
    }

    override fun inflateLayoutById(): Int = R.layout.activity_main

    override fun inflatePlaceHolderLayoutRes(): Int = R.layout.layout_placeholder1

    override fun init() {

//        setStatusBarColor(Color.RED)
        StatusBarUtils.immersive(this)

        initListener()

    }

    @SuppressLint("WrongConstant")
    private fun initListener() {

        toast("Test Toast")

        btn_toast.click {
            val dateMills = 2032934294.toDateString()
            toast("测试成功tushi:$dateMills")
            iv_image.extLoad("https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0")

            LoadingDialogManager.get().showLoading(this)

            //Gson的解析容错测试
            val json = "{\"price\":\"\",\"value\":2.12,\"type\":\"3\",\"id\":\"\",\"msg\":\"\"}"
            val price = Gson().fromJson(json, Price::class.java)
            YYLogUtils.e(price.toString())

            userPresenter.testUser()
        }

        btn_request.setOnClickListener {

            mViewModel.getIndustry()
//            mViewModel.testChongfu()
        }

        btn_load_img.click {

            extRequestPermission(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                block = {
                    gotoActivity<ImageSelectActivity>()
                }
            )

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


    @NetWork(netWorkType = NetWorkUtil.NetworkType.NETWORK_NO)
    fun checkNetWork() {
        toast("没有网络了")
    }
}
