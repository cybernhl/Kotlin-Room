package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoAudioRecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils

/**
 * 录制音视频文件
 */
class RecoderVideoAudio1Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var videoRecodeUtils: VideoAudioRecoderUtils

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio1Activity>()
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

        initCamera(flContainer)

        startBtn.click {
            videoRecodeUtils.startRecord()
        }
        endBtn.click {
            videoRecodeUtils.stopRecord {
                toast("完成录制${it}")
            }
        }

        playBtn.click {
            RecoderVideoPlarerActivity.startInstance(videoRecodeUtils.getOutputPath())
        }
    }

    private fun initCamera(container: ViewGroup) {

        videoRecodeUtils = VideoAudioRecoderUtils()

        videoRecodeUtils.setupCamera(this, container)

    }

    override fun onDestroy() {
        super.onDestroy()

        videoRecodeUtils.destoryAll()
    }

}