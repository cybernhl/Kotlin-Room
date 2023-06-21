package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.media.MediaPlayer
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio.AudioRecoderUtils
import com.guadou.kt_demo.demo.demo5_network_request.FileIOUtilKt.file
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CheckUtil
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils

/**
 * 单独录制音频
 */
class RecoderAudio1Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var audioRecoder: AudioRecoderUtils
    private var mediaPlayer: MediaPlayer? = null

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderAudio1Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_audio

    override fun startObserve() {

    }

    override fun init() {

        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val videoPathTv = findViewById<TextView>(R.id.tv_video_path)

        audioRecoder = AudioRecoderUtils()

        startBtn.click {
            videoPathTv.text = ""
            audioRecoder.startAudioRecord()
        }
        endBtn.click {
            audioRecoder.stopAudioRecord {
                toast("录制完成！")
                videoPathTv.text = it
            }
        }

        playBtn.click {
            val path = videoPathTv.text.toString()

            if (!CheckUtil.isEmpty(path)) {
                mediaPlayer = MediaPlayer()
                mediaPlayer?.apply {
                    setDataSource(path)
                    setOnErrorListener { _, what, extra ->
                        YYLogUtils.w("播放错误类型  $what  错误码 $extra")
                        false
                    }
                    prepare()
                    start()
                }
            } else {
                toast("没有录制完成的音频")
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

        mediaPlayer?.release()
        mediaPlayer = null

        audioRecoder.release()
    }

}