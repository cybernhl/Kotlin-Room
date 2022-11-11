package com.guadou.kt_demo.demo.demo18_customview

import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo18HomeBinding
import com.guadou.kt_demo.demo.demo18_customview.circleView.DemoCircleViewActivity
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.result.ISAFLauncher
import com.guadou.lib_baselib.utils.result.SAFLauncher
import dagger.hilt.android.AndroidEntryPoint


/**
 * 其他
 */
@AndroidEntryPoint
class Demo18CustomViewActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo18HomeBinding>(), ISAFLauncher by SAFLauncher() {

    private val clickProxy: ClickProxy by lazy { ClickProxy() }

    companion object {
        fun startInstance() {
            commContext().gotoActivity<Demo18CustomViewActivity>()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo18_home)
            .addBindingParams(BR.click, clickProxy)
    }

    override fun startObserve() {
    }

    override fun init() {
        initLauncher()

    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        //去圆环的View
        fun circleView() {

//            getLauncher()?.launch(Intent(mActivity, Demo10Activity::class.java)) { result ->
//                val data = result.data?.getStringExtra("text")
//                toast("拿到返回数据：$data")
//            }

            getLauncher()?.launch<DemoCircleViewActivity> { result ->
                val result = result.data?.getStringExtra("text")
                toast("收到返回的数据：$result")
            }

        }

    }

}