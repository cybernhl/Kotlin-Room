package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.app.Activity
import android.graphics.Bitmap
import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.ICameraCallback
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface.VideoCaptureUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface.VideoCaptureUtils.RecordConfig
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller.GLCameraView2Demo
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.newki.yuv.YuvUtils
import java.io.File
import java.util.concurrent.Executors


class VideoCameraXRecoderUtils {
    private lateinit var videoCaptureUtils: VideoCaptureUtils
    private val yuvUtils = YuvUtils()
    private var bitmap: Bitmap? = null
    private val mExecutorService = Executors.newSingleThreadExecutor()
    private lateinit var mGLCameraView: GLCameraView2Demo  //自定义的GLSurfaceView

    private var mBitmapCallback: ((b: Bitmap?) -> Unit)? = null

    private lateinit var file: File

    private var endBlock: ((path: String) -> Unit)? = null


    /**
     * 初始化关联信息，设置回调处理
     */
    fun setupCamera(activity: Activity, container: ViewGroup) {

        file = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.mp4")
        if (!file.exists()) {
            file.createNewFile()
        }

        mGLCameraView = GLCameraView2Demo(activity)

        mGLCameraView.setImageCallback { image ->

            if (image == null) return@setImageCallback

            // 使用C库获取到I420格式，对应 COLOR_FormatYUV420Planar
            val yuvFrame = yuvUtils.convertToI420(image)

            // 与MediaFormat的编码格式宽高对应
            val yuvFrameRotate = yuvUtils.rotate(yuvFrame, 90)

            bitmap = Bitmap.createBitmap(yuvFrameRotate.width, yuvFrameRotate.height, Bitmap.Config.ARGB_8888)
            yuvUtils.yuv420ToArgb(yuvFrameRotate, bitmap!!)

            mBitmapCallback?.invoke(bitmap)

        }

        mGLCameraView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        container.addView(mGLCameraView)


        //处理自己的编码器
        val recordConfig = RecordConfig.Builder().build()
        videoCaptureUtils = VideoCaptureUtils(recordConfig, Size(1920, 1080))
        mGLCameraView.setVideoEncoderInputSurface(videoCaptureUtils.mCameraSurface)
    }


    /**
     * 开始录制
     */
    fun startRecord() {
        mGLCameraView.setRecording(true)

        val outputFileOptions: VideoCaptureUtils.OutputFileOptions = VideoCaptureUtils.OutputFileOptions.Builder(file).build()

        videoCaptureUtils.startRecording(outputFileOptions, mExecutorService, object : VideoCaptureUtils.OnVideoSavedCallback {
            override fun onVideoSaved(outputFileResults: VideoCaptureUtils.OutputFileResults) {

                YYLogUtils.w("保存成功：" + outputFileResults.savedUri.toString(), "VideoCaptureUtils")

                mGLCameraView.post {
                    endBlock?.invoke(file.absolutePath)
                }
            }

            override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
                cause?.printStackTrace()
                YYLogUtils.e("message:$message code:$videoCaptureError")
            }
        })
    }

    /**
     * 停止录制
     */
    fun stopRecord(block: ((path: String) -> Unit)? = null) {
        endBlock = block

        videoCaptureUtils.stopRecording()
    }

    fun getOutputPath(): String {
        return file.absolutePath
    }

    /**
     * 内部包含Camera2Provider对象，释放资源会全部释放
     */
    fun destoryAll() {
        bitmap?.recycle()
    }

    fun setBitmapCallback(block: ((b: Bitmap?) -> Unit)? = null) {
        mBitmapCallback = block
    }
}