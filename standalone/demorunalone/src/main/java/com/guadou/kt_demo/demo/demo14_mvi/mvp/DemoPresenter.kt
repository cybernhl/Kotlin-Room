package com.guadou.kt_demo.demo.demo14_mvi.mvp

import androidx.lifecycle.LifecycleCoroutineScope
import com.guadou.kt_demo.demo.demo14_mvi.mvc.OtherModel
import com.guadou.lib_baselib.ext.toast
import kotlinx.coroutines.launch

class DemoPresenter(private val view: IDemoView) {

    private val mOtherModel: OtherModel by lazy { OtherModel() }

    //获取行业数据
    fun requestIndustry(lifecycleScope: LifecycleCoroutineScope) {

        lifecycleScope.launch {
            //开始Loading
            view.showLoading()

            val result = mOtherModel.getIndustry()

            result.checkResult({
                //处理成功的信息
                toast("list:$it")
                view.getIndustrySuccess(it)
            }, {
                //失败
                view.getIndustryFailed(it)
            })

            view.hideLoading()
        }
    }

}