package com.guadou.kt_demo.demo.demo14_mvi.mvc

import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.AbsActivity
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager
import kotlinx.coroutines.launch


class MVCActivity : AbsActivity() {

    private val mOtherModel: OtherModel by lazy { OtherModel() }

    override fun setContentView() {
        setContentView(R.layout.activity_demo14_1)
    }

    override fun init() {
        val btnGetData = findViewById<Button>(R.id.btn_get_data)
        btnGetData.click {
            requestIndustry()
        }
    }

    private fun requestIndustry() {
        //MVC中Activity就是Controller,直接调用接口，获取数据之后直接操作xml控件刷新
        lifecycleScope.launch {
            //开始Loading
            LoadingDialogManager.get().showLoading(this@MVCActivity)

            val result = mOtherModel.getIndustry()

            result.checkSuccess {
                //处理成功的信息
                toast("list:$it")

                //doSth...
            }

            LoadingDialogManager.get().dismissLoading()
        }

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }
}