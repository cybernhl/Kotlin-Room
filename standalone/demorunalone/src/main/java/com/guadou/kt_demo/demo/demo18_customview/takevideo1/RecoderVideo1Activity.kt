package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.graphics.Bitmap
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.VideoH264RecoderUtils
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils

/**
 * 单独录制视频文件
 */
class RecoderVideo1Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var ivCatch: ImageView
    private lateinit var videoRecodeUtils: VideoH264RecoderUtils

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideo1Activity>()
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
        ivCatch = findViewById(R.id.iv_catch)

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

        videoRecodeUtils = VideoH264RecoderUtils()

        videoRecodeUtils.setupCamera(this, container)

        videoRecodeUtils.setBitmapCallback {
            it?.let {
                showBitMap(it)
            }
        }

    }

    private fun showBitMap(bitmap: Bitmap) {
        ivCatch.post {
            ivCatch.setImageBitmap(bitmap)
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        videoRecodeUtils.destoryAll()
    }

}