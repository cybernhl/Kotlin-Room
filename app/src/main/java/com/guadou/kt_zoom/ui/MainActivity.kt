package com.guadou.kt_zoom.ui

import androidx.lifecycle.Observer
import com.guadou.cs_router.YYRouterService
import com.guadou.kt_zoom.R
import com.guadou.kt_zoom.mvvm.MainViewModel
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.glideconfig.GlideApp
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MainActivity : BaseActivity<MainViewModel>() {


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

    override fun init() {

        initListener()
    }

    private fun initListener() {

        toast("Test Toast")

        btn_toast.setOnClickListener {

            val dateMills = 2032934294.toDateString()

            toastSuccess("测试成功tushi:" + dateMills)
        }

        btn_request.setOnClickListener {

            mViewModel.getIndustry()

        }

        btn_load_img.setOnClickListener {

            iv_image.load("https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0")
        }


        btn_jump_auth.setOnClickListener {
            YYRouterService.mainComponentServer.startAuthActivity()
        }
    }


}
