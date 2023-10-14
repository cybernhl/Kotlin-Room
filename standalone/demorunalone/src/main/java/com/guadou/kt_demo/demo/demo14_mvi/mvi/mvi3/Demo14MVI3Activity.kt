package com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi3

import androidx.lifecycle.lifecycleScope
import com.guadou.kt_demo.databinding.ActivityDemo14JavaMviBinding
import com.guadou.kt_demo.demo.demo14_mvi.mvi.mvi2.BaseVB2Activity
import com.guadou.lib_baselib.utils.NetWorkUtil.NetworkType
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.guadou.lib_baselib.view.LoadingDialogManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class Demo14MVI3Activity : BaseVB2Activity<Demo14MVI3ViewModel, ActivityDemo14JavaMviBinding>() {


    override fun init() {
        observeViewModel()

        mBinding.btnGetData.text = "点击获取数据"
        mBinding.btnGetData.setOnClickListener { view ->

            lifecycleScope.launch {
                mViewModel.mainIntentChannel.send(MVI3Intent.GetIndustry)
            }
        }
    }

    /**
     * 观察ViewModel
     */
    private fun observeViewModel() {
        lifecycleScope.launch {
            //状态收集，（实际就是StateFlow的监听）
            mViewModel.uiState.collect {
                when (it) {
                    is MVI3State.Idle -> {
                    }
                    is MVI3State.Loading -> {
                        LoadingDialogManager.get().showLoading(this@Demo14MVI3Activity)
                    }
                    is MVI3State.Industries -> {
                        LoadingDialogManager.get().dismissLoading()
                        YYLogUtils.w("indusory:$it.indusory")
                    }
                    is MVI3State.Schools -> {
                        LoadingDialogManager.get().dismissLoading()
                        YYLogUtils.w("schools:${it.schools}")
                    }
                    is MVI3State.Error -> {
                        LoadingDialogManager.get().dismissLoading()
                        YYLogUtils.d("错误: $it.error")
                    }
                }
            }
        }
    }


    override fun onNetworkConnectionChanged(isConnected: Boolean, networkType: NetworkType) {}
}