package com.guadou.kt_demo.demo.demo18_customview

import android.Manifest
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo18HomeBinding
import com.guadou.kt_demo.demo.demo18_customview.circleView.DemoCircleViewActivity
import com.guadou.kt_demo.demo.demo18_customview.range.RangeViewActivity
import com.guadou.kt_demo.demo.demo18_customview.star.StarScoreViewActivity
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.RecoderVideo1Activity
import com.guadou.kt_demo.demo.demo18_customview.temperature.TemperatureViewActivity
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.*
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extRequestPermission
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

        fun temperatureView() {
            TemperatureViewActivity.startInstance()
        }

        fun starView() {
            StarScoreViewActivity.startInstance()
        }

        fun rangeView() {
            RangeViewActivity.startInstance()
        }

        fun viewGroup1() {
            ViewGroup1Activity.startInstance()
        }

        fun viewGroup2() {
            ViewGroup2Activity.startInstance()
        }

        fun viewGroup3() {
            ViewGroup3Activity.startInstance()
        }

        fun viewGroup4() {
            ViewGroup4Activity.startInstance()
        }

        fun viewGroup5() {
            ViewGroup5Activity.startInstance()
        }

        fun viewGroup6() {
            ViewGroup6Activity.startInstance()
        }

        fun videoRecoder1() {
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideo1Activity.startInstance()
            }
        }

    }

}