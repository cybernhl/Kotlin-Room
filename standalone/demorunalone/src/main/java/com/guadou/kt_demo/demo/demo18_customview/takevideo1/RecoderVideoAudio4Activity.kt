package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoCameraXRecoderUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface.VideoSurfaceRecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils

/**
 * VideoCaptureUtils的录制，直接通过 Surface 录制
 */
class RecoderVideoAudio4Activity : BaseVMActivity<EmptyViewModel>() {
    private lateinit var videoSurfaceRecoderUtils: VideoSurfaceRecoderUtils

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio4Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)

        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val iv_catch = findViewById<ImageView>(R.id.iv_catch)

        startBtn.text = "Surface录制"

        initCamera(flContainer)

        startBtn.click {
            videoSurfaceRecoderUtils.startRecord()
        }
        endBtn.click {
            videoSurfaceRecoderUtils.stopRecord {
                toast("完成录制,地址：${it}")
            }
        }

        playBtn.click {
            RecoderVideoPlarerActivity.startInstance(videoSurfaceRecoderUtils.getOutputPath())
        }
    }

    private fun initCamera(container: ViewGroup) {

        videoSurfaceRecoderUtils = VideoSurfaceRecoderUtils()

        videoSurfaceRecoderUtils.setupCamera(this, container)

    }

    override fun onDestroy() {
        super.onDestroy()

        videoSurfaceRecoderUtils.destoryAll()
    }

}