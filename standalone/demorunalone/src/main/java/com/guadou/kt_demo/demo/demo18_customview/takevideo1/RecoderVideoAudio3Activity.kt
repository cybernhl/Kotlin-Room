package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoAudioRecoderUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoCameraXRecoderUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoSoftRecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter

/**
 * 录制音视频文件(特效)
 */
class RecoderVideoAudio3Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var videoCameraXRecoderUtils: VideoCameraXRecoderUtils
    private var gpuImage: GPUImage? = null

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio3Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)

        gpuImage = GPUImage(this)
        gpuImage!!.setFilter(GPUImageSketchFilter())

        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val iv_catch = findViewById<ImageView>(R.id.iv_catch)
        startBtn.text = "特效录制"

        initCamera(flContainer)

        startBtn.click {
            videoCameraXRecoderUtils.startRecord()
        }
        endBtn.click {
            videoCameraXRecoderUtils.stopRecord {
                toast("完成录制${it}")
            }
        }

        videoCameraXRecoderUtils.setBitmapCallback {
            it?.let {

                val bitmap = gpuImage!!.getBitmapWithFilterApplied(it)

                iv_catch.post {
                    iv_catch.setImageBitmap(bitmap)
                }
            }
        }

        playBtn.click {
            RecoderVideoPlarerActivity.startInstance(videoCameraXRecoderUtils.getOutputPath())
        }
    }

    private fun initCamera(container: ViewGroup) {

        videoCameraXRecoderUtils = VideoCameraXRecoderUtils()

        videoCameraXRecoderUtils.setupCamera(this, container)

    }

    override fun onDestroy() {
        super.onDestroy()

        videoCameraXRecoderUtils.destoryAll()
    }

}