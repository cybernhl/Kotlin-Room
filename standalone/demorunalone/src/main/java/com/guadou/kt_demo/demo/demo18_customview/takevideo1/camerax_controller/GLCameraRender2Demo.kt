package com.guadou.kt_demo.demo.demo18_customview.takevideo1.camerax_controller

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLES11Ext
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import android.view.Surface
import androidx.annotation.WorkerThread
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.Filter
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.ScreenFilter
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.WhiteBalanceFilter
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.gles.EglCore
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.gles.WindowSurface
import com.guadou.lib_baselib.utils.log.YYLogUtils
import java.util.concurrent.Executors
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * GL渲染对象，用于切换滤镜渲染预览页面
 */
class GLCameraRender2Demo(private val context: Context, private val callback: Callback) : GLSurfaceView.Renderer,
    Preview.SurfaceProvider, SurfaceTexture.OnFrameAvailableListener {

    private var mInputWindowSurface: WindowSurface? = null
    private var inputSurface: Surface? = null

    private var textureID = 0
    private val fFrame = IntArray(1)
    private val fTexture = IntArray(1)

    private var surfaceTexture: SurfaceTexture? = null
    private var textureMatrix: FloatArray = FloatArray(16)
    private val executor = Executors.newSingleThreadExecutor()
    private var filter: Filter? = null
    var type: String = "Normal"

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.let {
            textureID = createTextureID()
            surfaceTexture = SurfaceTexture(textureID)
            filter = when (type) {
                "WhiteBalance" -> WhiteBalanceFilter(context)
                else -> ScreenFilter(context)
            }
        }
    }

    /**创建显示的texture */
    private fun createTextureID(): Int {
        val texture = IntArray(1)
        GLES20.glGenTextures(1, texture, 0) //第一个参数表示创建几个纹理对象，并将创建好的纹理对象放置到第二个参数中去
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0])
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameterf(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR.toFloat()
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE
        )
        GLES20.glTexParameteri(
            GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
            GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE
        )
        return texture[0]
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        callback.onSurfaceChanged()
        filter?.onReady(width, height)

        //清除遗留的
        GLES20.glDeleteFramebuffers(1, fFrame, 0)
        GLES20.glDeleteTextures(1, fTexture, 0)

        /**创建一个帧染缓冲区对象 */
        GLES20.glGenFramebuffers(1, fFrame, 0)

        /**根据纹理数量 返回的纹理索引 */
        GLES20.glGenTextures(1, fTexture, 0)

        /**将生产的纹理名称和对应纹理进行绑定 */
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fTexture[0])

        /**根据指定的参数 生产一个2D的纹理 调用该函数前  必须调用glBindTexture以指定要操作的纹理 */
        GLES20.glTexImage2D(
            GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height,
            0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null
        )

        //设置缩小过滤为使用纹理中坐标最接近的一个像素的颜色作为需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST.toFloat())
        //设置放大过滤为使用纹理中坐标最接近的若干个颜色，通过加权平均算法得到需要绘制的像素颜色
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR.toFloat())
        //设置环绕方向S，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE.toFloat())
        //设置环绕方向T，截取纹理坐标到[1/2n,1-1/2n]。将导致永远不会与border融合
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE.toFloat())

        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
    }

    override fun onDrawFrame(gl: GL10?) {
        val surfaceTexture = this.surfaceTexture
        if (gl == null || surfaceTexture == null) return
        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT)
//


        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(textureMatrix)
//        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fFrame[0])
//        GLES20.glFramebufferTexture2D(
//            GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0,
//            GLES20.GL_TEXTURE_2D, fTexture[0], 0
//        )

        filter?.setTransformMatrix(textureMatrix)
        filter?.onDrawFrame(fTexture[0])
    }

    // Preview.SurfaceProvider 接口的实现，用于绑定CameraX
    override fun onSurfaceRequested(request: SurfaceRequest) {
        val resetTexture: SurfaceTexture = resetPreviewTexture(request.resolution) ?: return
        val surface = Surface(resetTexture)
        request.provideSurface(surface, executor) {
            surface.release()
            surfaceTexture?.release()
        }
    }

    @WorkerThread
    private fun resetPreviewTexture(size: Size): SurfaceTexture? {
        return this.surfaceTexture?.let { surfaceTexture ->
            surfaceTexture.setOnFrameAvailableListener(this)
            surfaceTexture.setDefaultBufferSize(size.width, size.height)
            surfaceTexture
        }
    }

    // 当PreView.SurfaceProvider 设置完成，surface设置已完成之后回调出去
    override fun onFrameAvailable(surfaceTexture: SurfaceTexture?) {
        callback.onFrameAvailable()
        mInputWindowSurface?.swapBuffers()
    }

    fun setProgress(progress: Float) {
        if (filter is WhiteBalanceFilter) {
            (filter as WhiteBalanceFilter).temperature = progress
        }
    }

    fun setTint(progress: Float) {
        if (filter is WhiteBalanceFilter) {
            (filter as WhiteBalanceFilter).tint = progress
        }
    }

    fun setRecording(isRecording: Boolean) {

        if (isRecording) {
            val eglContext = EGL14.eglGetCurrentContext()
            YYLogUtils.w("当前的eglContext：$eglContext inputSurface：$inputSurface")

            val mEglCore = EglCore(eglContext, EglCore.FLAG_RECORDABLE)
            mInputWindowSurface = WindowSurface(mEglCore, inputSurface, true)
            mInputWindowSurface?.makeCurrent()
        } else {
            mInputWindowSurface?.release()
        }

    }

    fun setInputSurface(surface: Surface) {
        this.inputSurface = surface
    }

    interface Callback {
        fun onSurfaceChanged()
        fun onFrameAvailable()
    }
}