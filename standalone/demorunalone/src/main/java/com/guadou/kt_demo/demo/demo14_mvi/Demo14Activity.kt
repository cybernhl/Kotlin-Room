package com.guadou.kt_demo.demo.demo14_mvi

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo14Binding
import com.guadou.kt_demo.demo.demo14_mvi.mvi.Damo14ViewModel
import com.guadou.kt_demo.demo.demo14_mvi.mvi.Demo14ViewState
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.mvi.observeState
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
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

    override fun startObserve() {
        mViewModel.viewStates.observeState(this, Demo14ViewState::industrys, Demo14ViewState::schools) { industry, school ->

            YYLogUtils.w("industry: " + industry + " ; school: " + school)
        }

        mViewModel.viewStates.observeState(this, Demo14ViewState::isChanged) {
            if (it) {
              val industry =  mViewModel.viewStates.value?.industrys
              val school =  mViewModel.viewStates.value?.schools
                mBinding.tvMessage.text = "industry: " + industry + " ; school: " + school
            }
        }

    }

    override fun init() {
        StatusBarUtils.immersive(this)
        mViewModel.fetchDatas()
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        fun getData() {
            mViewModel.changeData()
        }
    }

}