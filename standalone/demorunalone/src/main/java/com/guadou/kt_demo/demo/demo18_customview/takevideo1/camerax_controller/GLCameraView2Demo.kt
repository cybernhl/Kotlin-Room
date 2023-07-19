package com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller

import android.content.Context
import android.media.Image
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.Surface
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller.CameraXController
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller.CameraXController2Demo
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.GLCameraRender

/**
 * GLSurfaceView的滤镜实现，内部使用自己的 GLCameraRender 实现滤镜的渲染
 */
class GLCameraView2Demo : GLSurfaceView {

    private val cameraXController: CameraXController2Demo = CameraXController2Demo()
    private val callback = object : GLCameraRender2Demo.Callback {
        override fun onSurfaceChanged() {
            //关联并绑定CameraX
            setUpCamera()

            cameraXController.setImageCallback(mImageCallback)
        }

        override fun onFrameAvailable() {
            //确认渲染
            requestRender()
        }
    }

    // GLSurfaceView.Renderer 渲染对象
    private val cameraRender = GLCameraRender2Demo(context, callback)

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.GLCameraView)
        val type = typeArray.getString(R.styleable.GLCameraView_type) ?: "Normal"
        cameraRender.type = type
        typeArray.recycle()
    }

    init {
        setEGLContextClientVersion(2)

        //设置GL渲染对象
        setRenderer(cameraRender)

        renderMode = RENDERMODE_WHEN_DIRTY
    }

    private fun setUpCamera() {
        cameraXController.setUpCamera(context, cameraRender)
    }

    fun getCameraXController(): CameraXController2Demo {
        return cameraXController
    }

    var mImageCallback: ((b: Image?) -> Unit)? = null

    fun setImageCallback(block: ((b: Image?) -> Unit)? = null) {
        mImageCallback = block
    }

    fun setRecording(isRecording: Boolean) {
        cameraRender.setRecording(isRecording)
    }

    fun setVideoEncoderInputSurface(surface: Surface) {
        // 将视频编码器的输入表面传递给滤镜对象
        cameraRender.setInputSurface(surface)
    }

}