package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface

import android.app.Activity
import android.graphics.SurfaceTexture
import android.media.Image

import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.BaseCommonCameraProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import java.io.File
import java.util.concurrent.Executors


class VideoSurfaceRecoderUtils {

    private lateinit var textureView: AspectTextureView
    private var surfaceTexture: SurfaceTexture? = null
    private var mCamera2Provider: Camera2SurfaceProvider? = null
    private var mPreviewSize: Size? = null
    private val mExecutorService = Executors.newSingleThreadExecutor()

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

        textureView = AspectTextureView(activity)
        textureView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mCamera2Provider = Camera2SurfaceProvider(activity)
        mCamera2Provider?.initTexture(textureView)

        mCamera2Provider?.setCameraInfoListener(object : BaseCommonCameraProvider.OnCameraInfoListener {
            override fun getBestSize(outputSizes: Size?) {
                mPreviewSize = outputSizes
            }

            override fun onFrameCannback(image: Image) {
            }

            override fun initEncode() {
            }

            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
                this@VideoSurfaceRecoderUtils.surfaceTexture = surfaceTexture
            }
        })

        container.addView(textureView)
    }

    /**
     * 开始录制
     */
    fun startRecord() {

        val outputFileOptions: VideoCaptureUtils.OutputFileOptions = VideoCaptureUtils.OutputFileOptions.Builder(file).build()

        mCamera2Provider?.videoCaptureUtils?.startRecording(outputFileOptions, mExecutorService, object : VideoCaptureUtils.OnVideoSavedCallback {
            override fun onVideoSaved(outputFileResults: VideoCaptureUtils.OutputFileResults) {

                YYLogUtils.w("保存成功：" + outputFileResults.savedUri.toString(), "VideoCaptureUtils")

                textureView.post {
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
        mCamera2Provider?.videoCaptureUtils?.stopRecording()
    }


    fun getOutputPath(): String {
        return file.absolutePath
    }

    /**
     * 内部包含Camera2Provider对象，释放资源会全部释放
     */
    fun destoryAll() {
        mCamera2Provider?.closeCamera()
    }

}