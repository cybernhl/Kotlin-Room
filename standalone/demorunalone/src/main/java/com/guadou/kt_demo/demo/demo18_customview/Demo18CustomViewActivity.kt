package com.guadou.kt_demo.demo.demo18_customview

import android.Manifest
import android.content.Intent
import com.guadou.cs_cptservices.ui.GlobalWebActivity
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo18HomeBinding
import com.guadou.kt_demo.demo.demo18_customview.circleView.DemoCircleViewActivity
import com.guadou.kt_demo.demo.demo18_customview.context.DemoContextActivity
import com.guadou.kt_demo.demo.demo18_customview.range.RangeViewActivity
import com.guadou.kt_demo.demo.demo18_customview.star.StarScoreViewActivity
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.*
import com.guadou.kt_demo.demo.demo18_customview.temperature.TemperatureViewActivity
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.*
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.engine.extRequestPermission
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
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
//            ViewGroup6Activity.startInstance()

            val url = "<p><span class=\\\"l31u4noy8\\\">1、树立正确的职</span>业观。职业只有分工不同，没有高低贵贱之分，一个职业<span " +
                    "class=\\\"w151ns\\\">劳动者应该以正确</span>的态度对待各种职业劳动，在各<span class=\\\"n4v03k3qhw\\\">自的岗位上忠于职守，诚实劳动</span>。</p><p>2、热爱自己本职工作。只有对自己的工作发自<b class=\\\"o534495ug\\\"></b>内心的热爱，才能专心致志地搞<strong class=\\\"f2z3l6u39u6\\\"></strong>好工作，只有干一行、爱一行，才能认认真真“钻一<span class=\\\"uitnm43u0\\\">行”，才能出成绩，出效益</span>。</p><p>3、做好自己的本职工作。勤勤恳恳、兢兢业业地<b class=\\\"hf32r15\\\"></b>做好本职工作，是敬业的最高境<span class=\\\"k5cq41g0\\\">界，也是崇高思想道德品</span>质的表现。而玩忽职守，摸拿公物，想尽办法实施内盗，不仅是失职行为，也是对自己的背叛，更是对 职业道德的亵渎。</p><p>4、自觉遵守职业纪律。职业纪律是一种行为规范，他要求每个员工在职业活动<span class=\\\"i9c36d\\\">中遵守秩序，执行命令，不</span>折不扣地履行自己的职责，他能保障劳动者的根本利益，因此，每一位职工必须遵守职业纪律，这也是爱岗敬业的保障。</p><p>其实我说的这些适合每个工作岗位。希望能帮助到你！</p>";

            GlobalWebActivity.startInstance("Title",url)
        }


        fun videoRecoder() {
            //单独视频录制
            extRequestPermission(Manifest.permission.CAMERA) {
                RecoderVideo1Activity.startInstance()
            }
        }

        fun audioRecoder() {
            //单独音频录制
            extRequestPermission(Manifest.permission.RECORD_AUDIO) {
                RecoderAudio1Activity.startInstance()
            }
        }

        fun videoRecoderAll() {
            //音视频录制
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio1Activity.startInstance()
            }
        }

        fun softRecoderAll() {
            //软编录制
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio2Activity.startInstance()
            }
        }

        fun effectsRecoder() {
            //特效 - 1
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio3Activity.startInstance()
            }
        }

        fun gpuimageRecord() {
            //特效 - 2
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
//                RecoderVideoAudio7Activity.startInstance()
                RecoderVideoAudio8Activity.startInstance()
            }
        }

        fun effectsRecoder2() {
            //特效-Camerax
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio5Activity.startInstance()
            }
        }

        fun effectsRecoder3() {
            //特效-camera1
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio6Activity.startInstance()
            }
        }

        fun surfaceRecoder() {
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoAudio4Activity.startInstance()
            }
        }

        fun videoRecodeCustomView() {
            //音视频录制带自定义Button
            extRequestPermission(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO) {
                RecoderVideoWithAudioActivity.startInstance()
            }
        }

        //测试上下文
        fun testContext(){
            DemoContextActivity.startInstance()
        }

    }

}