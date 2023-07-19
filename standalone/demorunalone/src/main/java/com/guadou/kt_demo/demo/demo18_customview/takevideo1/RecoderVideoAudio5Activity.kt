package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller.CameraXController
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.newki.glrecordx.camerax.GLCameraXView
import com.newki.glrecordx.camerax.model.MagicFilterType
import com.newki.glrecordx.camerax.widget.SlideGpuFilterGroup

import java.io.File

/**
 * 录制音视频文件(特效录制CameraX)
 */
class RecoderVideoAudio5Activity : BaseVMActivity<EmptyViewModel>(), SlideGpuFilterGroup.OnFilterChangeListener {

    private lateinit var outFile: File

    private lateinit var mRecordCameraView: GLCameraXView
    private var isRecording: Boolean = false
    private val cameraXController: CameraXController = CameraXController()

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio5Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video5

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)

        outFile = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.mp4")
        if (!outFile.exists()) {
            outFile.createNewFile()
        }

        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val changeCamera = findViewById<Button>(R.id.change_camera)
        val changeFilter = findViewById<Button>(R.id.change_filter)

        startBtn.text = "特效录制"

        mRecordCameraView = GLCameraXView(this, 9 / 16f)
        mRecordCameraView.switchCameraLensFacing(true, 1)
        mRecordCameraView.setOnFilterChangeListener(this)
        cameraXController.setUpCamera(this, mRecordCameraView)
        flContainer.addView(mRecordCameraView)

        // ==> 设置按钮的监听
        //切换前后摄像头
        changeCamera.click {
            val facing = cameraXController.switchCamera(this)
            mRecordCameraView.switchCameraLensFacing(true, facing)
        }
        //切换滤镜
        changeFilter.click {
            mRecordCameraView.nextFilter()
        }
        //启动录制
        startBtn.click {
            startRecording()
        }
        //停止录制
        endBtn.click {
            stopRecording()
        }
        //去播放
        playBtn.click {
            RecoderVideoPlarerActivity.startInstance(outFile.absolutePath)
        }

    }

    private fun startRecording() {
        isRecording = true
        mRecordCameraView.takeVideo(outFile.absolutePath)

    }

    private fun stopRecording() {
        isRecording = false
        mRecordCameraView.stopRecord()

    }

    override fun onDestroy() {
        super.onDestroy()
        //CameraX自动绑定生命周期解绑
    }

    override fun onFilterChange(type: MagicFilterType?) {
        runOnUiThread {
            if (type === MagicFilterType.NONE) {
                toast("当前没有设置滤镜--$type")
            } else {
                toast("当前滤镜切换为--$type")
            }
        }
    }

}