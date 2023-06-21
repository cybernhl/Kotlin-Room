package com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.guadou.kt_demo.R

/**
 * GLSurfaceView的滤镜实现，内部使用自己的 GLCameraRender 实现滤镜的渲染
 */
class GLCameraView : GLSurfaceView {

    private val cameraXController: CameraXController = CameraXController()
    private val callback = object : GLCameraRender.Callback {
        override fun onSurfaceChanged() {
            //关联并绑定CameraX
            setUpCamera()
        }

        override fun onFrameAvailable() {
            //确认渲染
            requestRender()
        }
    }

    // GLSurfaceView.Renderer 渲染对象
    private val cameraRender = GLCameraRender(context, callback)

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

    fun getCameraXController(): CameraXController {
        return cameraXController
    }

    /**
     * 设置默认的滤镜 ，目前支持两种简单滤镜
     *
     * WhiteBalance
     * Normal
     */
    fun setFilterType(type: String) {
        cameraRender.type = type
    }

    //根据进度设置一些滤镜的效果
    fun setProgress(progress: Float) {
        cameraRender.setProgress(progress)
    }

    fun setTint(progress: Float) {
        cameraRender.setTint(progress)
    }
}