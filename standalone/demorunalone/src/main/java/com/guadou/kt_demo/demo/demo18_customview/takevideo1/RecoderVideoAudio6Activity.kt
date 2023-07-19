package com.guadou.kt_demo.demo.demo18_customview.takevideo1


import android.graphics.Point
import android.hardware.Camera
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.newki.glrecord.GLCamera1View
import com.newki.glrecord.model.MagicFilterType
import com.newki.glrecord.widget.FocusImageView
import com.newki.glrecord.widget.SlideGpuFilterGroup
import java.io.File

/**
 * 录制音视频文件(特效录制Camera1)
 */
class RecoderVideoAudio6Activity : BaseVMActivity<EmptyViewModel>(), SlideGpuFilterGroup.OnFilterChangeListener, View.OnTouchListener {

    private lateinit var mRecorderFocusIv: FocusImageView
    private lateinit var outFile: File
    private var screenHeight: Int = 0
    private var screenWidth: Int = 0
    private lateinit var mRecordCameraView: GLCamera1View
    private var isRecording: Boolean = false


    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio6Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video6

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)

        val mDisplayMetrics = applicationContext.resources.displayMetrics
        screenWidth = mDisplayMetrics.widthPixels
        screenHeight = mDisplayMetrics.heightPixels

        outFile = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.mp4")
        if (!outFile.exists()) {
            outFile.createNewFile()
        }

        val flContainer = findViewById<FrameLayout>(R.id.fl_container)
        mRecorderFocusIv = findViewById(R.id.recorder_focus_iv)
        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val changeCamera = findViewById<Button>(R.id.change_camera)
        val changeFilter = findViewById<Button>(R.id.change_filter)


        startBtn.text = "特效录制"

        mRecordCameraView = GLCamera1View(this)
        mRecordCameraView.setOnTouchListener(this)
        mRecordCameraView.setOnFilterChangeListener(this)
        flContainer.addView(mRecordCameraView)

        // ==> 设置按钮的监听
        //切换前后摄像头
        changeCamera.click {
            mRecordCameraView.switchCamera()
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
        mRecordCameraView.setSavePath(outFile.absolutePath)
        mRecordCameraView.startRecord()
    }

    private fun stopRecording() {
        isRecording = false
        mRecordCameraView.stopRecord()

    }

    override fun onDestroy() {
        super.onDestroy()
        mRecordCameraView.onDestroy()
    }

    // 自定义对焦的处理
    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        mRecordCameraView.onTouch(event)

        if (mRecordCameraView.cameraId == 1) {
            return false
        }

        when (event.action) {
            MotionEvent.ACTION_UP -> {
                val sRawX = event.rawX
                val sRawY = event.rawY
                var rawY: Float = sRawY * screenWidth / screenHeight
                val rawX = rawY
                rawY = (screenWidth - sRawX) * screenHeight / screenWidth
                val point = Point(rawX.toInt(), rawY.toInt())

                mRecordCameraView.onFocus(point, Camera.AutoFocusCallback { success, camera ->
                    if (success) {
                        mRecorderFocusIv.onFocusSuccess()
                    } else {
                        mRecorderFocusIv.onFocusFailed()
                    }
                })

                mRecorderFocusIv.startFocus(Point(sRawX.toInt(), sRawY.toInt()))
            }
        }
        return true
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