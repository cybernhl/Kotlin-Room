package com.guadou.kt_demo.demo.demo14_mvi.mvi

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo14Binding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.mvi.observeState
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Demo14Activity : BaseVDBActivity<Damo14ViewModel, ActivityDemo14Binding>() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo14Activity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo14)
            .addBindingParams(BR.click, clickProxy)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
        //监听两者数据变化
        mViewModel.viewStates.observeState(
            this,
            Damo14ViewModel.Demo14ViewState::industrys,
            Damo14ViewModel.Demo14ViewState::schools
        ) { industry, school ->

            YYLogUtils.w("industry: $industry ; school: $school")
        }

        //只监听changed的变换
        mViewModel.viewStates.observeState(this, Damo14ViewModel.Demo14ViewState::isChanged) {
            if (it) {
                val industry = mViewModel.viewStates.value?.industrys
                val school = mViewModel.viewStates.value?.schools
                mBinding.tvMessage.text = "industry: $industry ; school: $school"
            }
        }

    }

    override fun init() {
        //发送Intent指令，具体的实现由ViewModel实现
        mViewModel.dispatch(Damo14ViewModel.DemoAction.RequestAllData)
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun getData() {
            //发送Intent指令，具体的实现由ViewModel实现
//            mViewModel.dispatch(Damo14ViewModel.DemoAction.RequestIndustry)
//            mViewModel.dispatch(Damo14ViewModel.DemoAction.RequestSchool)
            mViewModel.dispatch(Damo14ViewModel.DemoAction.UpdateChanged(true))

        }
    }

}