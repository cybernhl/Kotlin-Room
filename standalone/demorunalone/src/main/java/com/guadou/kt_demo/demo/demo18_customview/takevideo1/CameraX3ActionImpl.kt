package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl1.MyGLSurfaceView
import java.io.File

/**
 * 使用自定义的 SurfaceView 实现 CameraX
 */
@SuppressLint("RestrictedApi")
class CameraX3ActionImpl : ICameraAction {
    private var mVecordFile: File? = null // 输出的文件
    private lateinit var mContext: Context
    private lateinit var mGLSurfaceView: MyGLSurfaceView  //自定义的GLSurfaceView

    override fun setOutFile(file: File) {
        mVecordFile = file
    }

    override fun getOutFile(): File {
        return mVecordFile!!
    }

    override fun initCamera(context: Context): View {
        mGLSurfaceView = MyGLSurfaceView(context)

        mContext = context
        mGLSurfaceView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        return mGLSurfaceView
    }

    override fun initCameraRecord() {}

    override fun startCameraRecord() {
        mGLSurfaceView.getCameraXController().startCameraRecord(outFile)
    }

    override fun stopCameraRecord(cameraCallback: ICameraCallback) {
        mGLSurfaceView.getCameraXController().stopCameraRecord(cameraCallback)
    }

    override fun releaseCameraRecord() {
    }

    override fun releaseAllCamera() {}

    override fun clearWindow() {}

    override fun isShowCameraView(isVisible: Boolean) {
        mGLSurfaceView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}