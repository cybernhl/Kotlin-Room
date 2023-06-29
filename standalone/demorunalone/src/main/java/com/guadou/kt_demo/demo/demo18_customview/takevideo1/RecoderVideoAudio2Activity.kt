package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoAudioRecoderUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoSoftRecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils

/**
 * 录制音视频文件(软编)
 */
class RecoderVideoAudio2Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var videoSoftRecoderUtils: VideoSoftRecoderUtils

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio2Activity>()
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

        startBtn.text = "软编录制"

        initCamera(flContainer)

        startBtn.click {
            videoSoftRecoderUtils.startRecord()
        }
        endBtn.click {
            videoSoftRecoderUtils.stopRecord {
                toast("完成录制${it}")
            }
        }

        playBtn.click {
            RecoderVideoPlarerActivity.startInstance(videoSoftRecoderUtils.getOutputPath())
        }
    }

    private fun initCamera(container: ViewGroup) {

        videoSoftRecoderUtils = VideoSoftRecoderUtils()

        videoSoftRecoderUtils.setupCamera(this, container)

    }

    override fun onDestroy() {
        super.onDestroy()

        videoSoftRecoderUtils.destoryAll()
    }

}