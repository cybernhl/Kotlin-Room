package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.content.Intent
import android.widget.MediaController
import android.widget.VideoView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoAudioRecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils


/**
 * 播放视频
 */
class RecoderVideoPlarerActivity : BaseVMActivity<EmptyViewModel>() {

    private var videoPath: String? = null
    private lateinit var videoRecodeUtils: VideoAudioRecoderUtils

    companion object {
        fun startInstance(path: String) {
            commContext().gotoActivity<RecoderVideoPlarerActivity>(bundle = arrayOf("path" to path))
        }
    }

    override fun getDataFromIntent(intent: Intent) {
        super.getDataFromIntent(intent)
        videoPath = intent.getStringExtra("path")
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video_player

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)


        val videoView = findViewById<VideoView>(R.id.video_view)

        YYLogUtils.d("播放视频videoPath：$videoPath")

        videoView.setVideoPath(videoPath)

        videoView.setOnCompletionListener {
            toast("视频播放完成后的操作")
        }
        videoView.setMediaController(MediaController(this))
        videoView.requestFocus()
        videoView.start()
    }


}