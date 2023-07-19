package com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.media.MediaRecorder

import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.ICameraCallback
import com.guadou.lib_baselib.utils.log.YYLogUtils

import java.io.File
import java.util.concurrent.Executors


/**
 * CameraX 绑定自定义的 GLTextTure
 */
@SuppressLint("RestrictedApi")
class CameraXController2Demo {

    private var mCameraProvider: ProcessCameraProvider? = null
    private var mLensFacing = 0
    private var mCameraSelector: CameraSelector? = null

    private var mCameraCallback: ICameraCallback? = null
    private val mExecutorService = Executors.newSingleThreadExecutor()

    //初始化 CameraX 相关配置
    fun setUpCamera(context: Context, surfaceProvider: Preview.SurfaceProvider) {

        //获取屏幕的分辨率与宽高比
        val displayMetrics = context.resources.displayMetrics
        val screenAspectRatio = aspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels)

        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({

            mCameraProvider = cameraProviderFuture.get()

            //镜头选择
            mLensFacing = lensFacing
            mCameraSelector = CameraSelector.Builder().requireLensFacing(mLensFacing).build()

            //预览对象
            val preview: Preview = Preview.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .build()

            preview.setSurfaceProvider(surfaceProvider)

            val imageAnalysis =  ImageAnalysis.Builder()
                .setTargetAspectRatio(screenAspectRatio)
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()

            // 在每一帧上应用颜色矩阵
            imageAnalysis.setAnalyzer(mExecutorService, object : ImageAnalysis.Analyzer {
                @SuppressLint("UnsafeOptInUsageError")
                override fun analyze(image: ImageProxy) {

                    mImageCallback?.invoke(image.image)

                    image.close()
                }
            });


            //绑定到页面
            mCameraProvider?.unbindAll()
            val camera = mCameraProvider?.bindToLifecycle(
                context as LifecycleOwner,
                mCameraSelector!!,
                preview,
                imageAnalysis,
            )

            val cameraInfo = camera?.cameraInfo
            val cameraControl = camera?.cameraControl

        }, ContextCompat.getMainExecutor(context))
    }

    //根据屏幕宽高比设置预览比例为4:3还是16:9
    private fun aspectRatio(widthPixels: Int, heightPixels: Int): Int {
        val previewRatio = Math.max(widthPixels, heightPixels).toDouble() / Math.min(widthPixels, heightPixels).toDouble()
        return if (Math.abs(previewRatio - 4.0 / 3.0) <= Math.abs(previewRatio - 16.0 / 9.0)) {
            AspectRatio.RATIO_4_3
        } else {
            AspectRatio.RATIO_16_9
        }
    }

    //优先选择哪一个摄像头镜头
    private val lensFacing: Int
        private get() {
            if (hasBackCamera()) {
                return CameraSelector.LENS_FACING_BACK
            }
            return if (hasFrontCamera()) {
                CameraSelector.LENS_FACING_FRONT
            } else -1
        }

    //是否有后摄像头
    private fun hasBackCamera(): Boolean {
        if (mCameraProvider == null) {
            return false
        }
        try {
            return mCameraProvider!!.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
        } catch (e: CameraInfoUnavailableException) {
            e.printStackTrace()
        }
        return false
    }

    //是否有前摄像头
    private fun hasFrontCamera(): Boolean {
        if (mCameraProvider == null) {
            return false
        }
        try {
            return mCameraProvider!!.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)
        } catch (e: CameraInfoUnavailableException) {
            e.printStackTrace()
        }
        return false
    }


    // 开始录制
    fun startCameraRecord(outFile: File) {

    }

    // 停止录制
    fun stopCameraRecord(cameraCallback: ICameraCallback?) {
        mCameraCallback = cameraCallback
    }

    // 释放资源
    fun releseAll() {
        mExecutorService.shutdown()
        mCameraProvider?.unbindAll()
        mCameraProvider?.shutdown()
        mCameraProvider = null
    }

    var mImageCallback: ((b: Image?) -> Unit)? = null

    fun setImageCallback(block: ((b: Image?) -> Unit)? = null) {
        mImageCallback = block
    }

}