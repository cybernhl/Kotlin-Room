package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.fragment

import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityLoadingNormalBinding
import com.guadou.lib_baselib.base.fragment.BaseVDBLoadingFragment
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.gloading.GloadingGlobalStatusView
import dagger.hilt.android.AndroidEntryPoint


/**
 * 默认是跳动动画
 */
@AndroidEntryPoint
class JumpLoadingFragment : BaseVDBLoadingFragment<EmptyViewModel, ActivityLoadingNormalBinding>() {

    companion object {
        fun obtainFragment(): JumpLoadingFragment {
            return JumpLoadingFragment()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_loading_normal)
    }

    override fun startObserve() {

    }

    override fun init() {

        //模拟的Loading的情况
        showStateLoading()

        CommUtils.getHandler().postDelayed({

            showStateSuccess()

        }, 2500)

    }

    //可选的实现
    //如果要设置空出Title的位置，需要重写这个方法设置Data
    override fun showStateLoading() {
        mGLoadingHolder.withData(GloadingGlobalStatusView.NEED_LOADING_STATUS_MAGRIN_TITLE)
        mGLoadingHolder.showLoading()
    }


}