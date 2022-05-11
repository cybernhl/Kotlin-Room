package com.guadou.kt_demo.demo.demo14_mvi.mvp

import android.widget.Button
import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry
import com.guadou.lib_baselib.base.activity.AbsActivity
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.utils.NetWorkUtil
import com.guadou.lib_baselib.view.LoadingDialogManager


class MVPActivity : AbsActivity(), IDemoView {

    private lateinit var mPresenter: DemoPresenter

    override fun setContentView() {
        setContentView(R.layout.activity_demo14_1)
    }

    override fun init() {
        //创建Presenter
        mPresenter = DemoPresenter(this)

        val btnGetData = findViewById<Button>(R.id.btn_get_data)
        btnGetData.click {
            //通过Presenter调用接口
            mPresenter.requestIndustry(lifecycleScope)
        }
    }

    //回调再次触发
    override fun showLoading() {
        LoadingDialogManager.get().showLoading(this)
    }

    override fun hideLoading() {
        LoadingDialogManager.get().dismissLoading()
    }

    override fun getIndustrySuccess(list: List<Industry>?) {
        //popupIndustryData
    }

    override fun getIndustryFailed(msg: String?) {
        //showErrorMessage
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetWorkUtil.NetworkType?) {
    }

}