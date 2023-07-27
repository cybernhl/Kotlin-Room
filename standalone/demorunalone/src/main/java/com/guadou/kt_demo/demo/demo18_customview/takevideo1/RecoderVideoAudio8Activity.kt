package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Point
import android.hardware.Camera
import android.opengl.GLSurfaceView
import android.view.Surface
import android.view.SurfaceHolder
import android.view.TextureView
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.CameraHelper
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.CameraListener
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.StatusBarUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSketchFilter


/**
 * 录制音视频文件(特效录制 GPUImage)
 */
class RecoderVideoAudio8Activity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var cameraHelper: CameraHelper
    private lateinit var textureView: TextureView
    private lateinit var gpuimage: GPUImage
    private var camera: Camera? = null

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideoAudio8Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video7

    override fun startObserve() {

    }

    override fun init() {

        StatusBarUtils.immersive(this)

        val flContainer = findViewById<FrameLayout>(R.id.fl_container)

        val startBtn = findViewById<Button>(R.id.start)
        val endBtn = findViewById<Button>(R.id.end)
        val playBtn = findViewById<Button>(R.id.play)
        val iv_catch = findViewById<ImageView>(R.id.iv_catch)


        startBtn.click {

        }
        endBtn.click {

        }

        playBtn.click {

        }

        // 动态添加摄像头显示布局
        setupCamera(flContainer)

    }

    private fun setupCamera(container: FrameLayout) {
        container.removeAllViews()

        textureView = TextureView(this)
        textureView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        gpuimage = GPUImage(this)
        val glSurfaceView = GLSurfaceView(this)
        glSurfaceView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        gpuimage.setFilter(GPUImageSketchFilter())
        gpuimage.setScaleType(GPUImage.ScaleType.CENTER_CROP)
        gpuimage.setGLSurfaceView(glSurfaceView)

        textureView.getViewTreeObserver().addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                textureView.post { setupCameraHelper() }
                textureView.getViewTreeObserver().removeOnGlobalLayoutListener(this)
            }
        })

        glSurfaceView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                val surface = holder.surface
                YYLogUtils.w("surfaceCreated")
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                YYLogUtils.w("surfaceChanged")
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                YYLogUtils.w("surfaceDestroyed")
            }
        })

        container.addView(textureView)
        container.addView(glSurfaceView)

    }

    private fun setupCameraHelper() {

        cameraHelper = CameraHelper.Builder()
            .previewViewSize(Point(textureView.measuredWidth, textureView.measuredHeight))
            .rotation(windowManager.defaultDisplay.rotation)
            .specificCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
            .isMirror(false)
            .previewOn(textureView)
            .cameraListener(object : CameraListener {
                override fun onCameraOpened(camera: Camera?, cameraId: Int, displayOrientation: Int, isMirror: Boolean) {
                }

                override fun onPreview(data: ByteArray?, camera: Camera?) {
                }

                override fun onCameraClosed() {
                }

                override fun onCameraError(e: Exception?) {
                }

                override fun onCameraConfigurationChanged(cameraID: Int, displayOrientation: Int) {
                }

                override fun onSurfaceTextureUpdated() {
                    val currentFrame: Bitmap? = textureView.bitmap
                    gpuimage.setImage(currentFrame)
                    gpuimage.requestRender()
                }
            })
            .build()

        cameraHelper.start()

    }


    override fun onDestroy() {
        cameraHelper.stop()
        super.onDestroy()
    }


}