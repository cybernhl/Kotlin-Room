package com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl1

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.util.Size
import android.view.Surface
import androidx.annotation.WorkerThread
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl.WaterMark
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.Filter
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2.ScreenFilter
import java.util.concurrent.Executors
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


/**
 * GL渲染对象，实现 CameraX的 Preview.SurfaceProvider 与 SurfaceTexture.OnFrameAvailableListener 用于CameraX中使用
 *
 * 1.在onSurfaceCreated方法中，生成一个纹理对象并创建一个SurfaceTexture。SurfaceTexture可以从CameraX获取摄像头预览数据，
 *   并将其作为纹理供OpenGL渲染使用。
 * 2.在onSurfaceChanged方法中，通知回调对象(MyGLRenderCallback)表明Surface的大小已经变化。
 *    然后，初始化一个滤镜对象(Filter)并调用其onReady方法，准备渲染。
 * 3.在onDrawFrame方法中，执行实际的渲染操作。首先清除颜色缓冲区，然后使用SurfaceTexture更新纹理图像和纹理变换矩阵。
 *   最后，将纹理传递给滤镜对象进行渲染。
 * 4.MyGLRender还实现了Preview.SurfaceProvider接口，用于绑定CameraX。
 *   在onSurfaceRequested方法中，通过创建一个新的SurfaceTexture，并将其提供给CameraX来获取摄像头预览数据的Surface。
 * 5.在onFrameAvailable方法中，当CameraX有新的帧可用时，通知回调对象(MyGLRenderCallback)。然后确定渲染回调执行到3 onDrawFrame()
 */
class MyGLRender(private val context: Context, private val callback: MyGLRenderCallback) : GLSurfaceView.Renderer,
    Preview.SurfaceProvider, SurfaceTexture.OnFrameAvailableListener {

//    private var mWaterMark: WaterMark? = null
    private var textures: IntArray = IntArray(1)
    private var surfaceTexture: SurfaceTexture? = null
    private var textureMatrix: FloatArray = FloatArray(16)
    private val executor = Executors.newSingleThreadExecutor()
    private var filter: Filter? = null

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        gl?.let {
            it.glGenTextures(textures.size, textures, 0)
            surfaceTexture = SurfaceTexture(textures[0])

            filter = NoFilter(context)
        }
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        callback.onSurfaceChanged()
//        mWaterMark = WaterMark(context)
//        mWaterMark?.renderSize(width,height)
        filter?.onReady(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        val surfaceTexture = this.surfaceTexture
        if (gl == null || surfaceTexture == null) return

        gl.glClearColor(0f, 0f, 0f, 0f)
        gl.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        surfaceTexture.updateTexImage()
        surfaceTexture.getTransformMatrix(textureMatrix)

//        mWaterMark?.drawSelf()

        filter?.setTransformMatrix(textureMatrix)
        filter?.onDrawFrame(textures[0])
    }

    // Preview.SurfaceProvider 接口的实现，用于绑定CameraX
    override fun onSurfaceRequested(request: SurfaceRequest) {
        val resetTexture = resetPreviewTexture(request.resolution) ?: return
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
    }

}