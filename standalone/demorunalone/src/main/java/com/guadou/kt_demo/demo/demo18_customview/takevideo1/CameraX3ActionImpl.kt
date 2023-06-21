package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.renderscript.RenderScript
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.graphics.drawable.DrawableCompat.setTint
import androidx.databinding.adapters.SeekBarBindingAdapter.setProgress
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.GLCameraView
import com.guadou.lib_baselib.utils.log.YYLogUtils.e
import com.guadou.lib_baselib.utils.log.YYLogUtils.w
import java.io.File
import java.util.concurrent.Executors

/**
 * 使用自定义的 GLCameraView 实现特效滤镜
 */
@SuppressLint("RestrictedApi")
class CameraX3ActionImpl : ICameraAction {
    private var mVecordFile: File? = null // 输出的文件
    private lateinit var mContext: Context
    private lateinit var mGLCameraView: GLCameraView  //自定义的GLSurfaceView

    override fun setOutFile(file: File) {
        mVecordFile = file
    }

    override fun getOutFile(): File {
        return mVecordFile!!
    }

    override fun initCamera(context: Context): View {
        mGLCameraView = GLCameraView(context)

        mContext = context
        mGLCameraView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        return mGLCameraView
    }

    override fun initCameraRecord() {}

    override fun startCameraRecord() {
        mGLCameraView.getCameraXController().startCameraRecord(outFile)
    }

    override fun stopCameraRecord(cameraCallback: ICameraCallback) {
        mGLCameraView.getCameraXController().stopCameraRecord(cameraCallback)
    }

    override fun releaseCameraRecord() {
    }

    override fun releaseAllCamera() {}

    override fun clearWindow() {}

    override fun isShowCameraView(isVisible: Boolean) {
        mGLCameraView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}