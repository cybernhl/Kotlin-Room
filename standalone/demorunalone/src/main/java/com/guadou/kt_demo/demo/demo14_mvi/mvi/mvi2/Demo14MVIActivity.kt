package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2

import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.databinding.ActivityDemo14JavaMviBinding
import com.guadou.lib_baselib.ext.ToastUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils.w
import com.guadou.lib_baselib.utils.NetWorkUtil.NetworkType
import com.guadou.lib_baselib.utils.log.YYLogUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Demo14MVIActivity : BaseVB2Activity<Demo14JMVI2ViewModel, ActivityDemo14JavaMviBinding>() {

    override fun init() {

        w("init - mBinding.btnGetData:" + mBinding.btnGetData)
        mBinding.btnGetData.text = "点击获取数据"
        mBinding.btnGetData.setOnClickListener { view ->
            w("点击到按钮:$view")
            mViewModel.sendUiIntent(Demo14Intent.GetSchool)
        }


        lifecycleScope.launchWhenStarted {
            mViewModel.uiStateFlow
                .map { it.schoolUiState }
                .distinctUntilChanged()
                .collect { schoolState ->
                    when (schoolState) {
                        is SchoolUiState.INIT -> {}
                        is SchoolUiState.SUCCESS -> {
                            YYLogUtils.w("Flow回调 -  加载学校数据成功")
                        }
                    }
                }
        }

        lifecycleScope.launchWhenResumed {
            //一次性状态的接收与处理
            mViewModel.uiEffectFlow
                .collect {
                    when (it) {
                        is Demo14Effect.NavigationToSchoolDetail -> {
                            ToastUtils.makeText(mActivity, "Flow回调 - 跳转到学校详情：${it.id}")
                        }
                    }
                }
        }

    }

    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetworkType) {}
}