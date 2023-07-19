package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.camera.view.PreviewView
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller.CameraXController
import java.io.File

/**
 * 使用 cameraXController 简化绑定流程
 */
@SuppressLint("RestrictedApi")
class CameraX2ActionImpl : ICameraAction {

    private var mVecordFile: File? = null // 输出的文件
    private lateinit var mContext: Context
    private val mCameraCallback: ICameraCallback? = null
    private val cameraXController: CameraXController = CameraXController()
    private lateinit var mPreviewView: PreviewView

    override fun setOutFile(file: File) {
        mVecordFile = file
    }

    override fun getOutFile(): File {
        return mVecordFile!!
    }

    override fun initCamera(context: Context): View {
        mPreviewView = PreviewView(context)
        mContext = context
        mPreviewView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mPreviewView.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (mPreviewView.isShown) {
                    startCamera()
                }
                mPreviewView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        return mPreviewView
    }

    private fun startCamera() {
        cameraXController.setUpCamera(mContext, mPreviewView.surfaceProvider)
    }

    override fun initCameraRecord() {}
    override fun startCameraRecord() {
        cameraXController.startCameraRecord(outFile)
    }

    override fun stopCameraRecord(cameraCallback: ICameraCallback) {
        cameraXController.stopCameraRecord(cameraCallback)
    }

    override fun releaseCameraRecord() {}
    override fun releaseAllCamera() {}
    override fun clearWindow() {}
    override fun isShowCameraView(isVisible: Boolean) {
        mPreviewView.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

}