package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.Image
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.BaseCommonCameraProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.Camera2ImageReaderProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.newki.openh264util.OpenH264Util
import com.newki.yuv.YuvUtils
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


/**
 * 软编
 */
class VideoSoftRecoderUtils {
    private var openH264Util: OpenH264Util? = null
    private var pEncoder: Long? = 0

    private val yuvUtils = YuvUtils()
    private var bitmap: Bitmap? = null

    private var surfaceTexture: SurfaceTexture? = null
    private var mCamera2Provider: Camera2ImageReaderProvider? = null
    private var mPreviewSize: Size? = null

    //标记当前是否正在录制中
    private var isRecording: Boolean = false

    // 输入的时候标记是否是结束标记
    private var isEndTip = false

    //子线程中使用同步队列保存数据
    private val originVideoDataList = LinkedBlockingQueue<ByteArray>()

    private lateinit var file: File
    private lateinit var outputStream: FileOutputStream

    private var configBytes: ByteArray? = null
    private var endBlock: ((path: String) -> Unit)? = null


    /**
     * 初始化关联信息，设置回调处理
     */
    fun setupCamera(activity: Activity, container: ViewGroup) {

        file = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.h264")
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!file.isDirectory) {
            outputStream = FileOutputStream(file, true)
        }

        val textureView = AspectTextureView(activity)
        textureView.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        mCamera2Provider = Camera2ImageReaderProvider(activity)
        mCamera2Provider?.initTexture(textureView)

        mCamera2Provider?.setCameraInfoListener(object :
            BaseCommonCameraProvider.OnCameraInfoListener {
            override fun getBestSize(outputSizes: Size?) {
                mPreviewSize = outputSizes
            }

            override fun onFrameCannback(image: Image) {
                if (isRecording) {

                    // 使用C库获取到I420格式，对应 COLOR_FormatYUV420Planar
                    val yuvFrame = yuvUtils.convertToI420(image)
                    // 与MediaFormat的编码格式宽高对应
                    val yuvFrameRotate = yuvUtils.rotate(yuvFrame, 90)

                    // 旋转90度之后的I420格式添加到同步队列
                    val bytesFromImageAsType = yuvFrameRotate.asArray()

                    //使用Java工具类转换Image对象为YUV420格式,对应 COLOR_FormatYUV420Flexible
//                    val bytesFromImageAsType = getBytesFromImageAsType(image, Camera2ImageUtils.YUV420SP)
                    originVideoDataList.offer(bytesFromImageAsType)

                    // 使用OpenH264编译视频文件
                    openH264Util?.encode(
                        pEncoder!!,
                        bytesFromImageAsType,
                        image.height,
                        image.width,
                    )
                }
            }

            override fun initEncode() {
                mediaCodecEncodeToH264()
            }

            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
                this@VideoSoftRecoderUtils.surfaceTexture = surfaceTexture
            }
        })

        container.addView(textureView)
    }


    /**
     * 准备数据编码成H264文件
     */
    fun mediaCodecEncodeToH264() {

        if (mPreviewSize == null) return

        //确定要竖屏的，真实场景需要根据屏幕当前方向来判断，这里简单写死为竖屏
        val videoWidth: Int
        val videoHeight: Int
        if (mPreviewSize!!.width > mPreviewSize!!.height) {
            videoWidth = mPreviewSize!!.height
            videoHeight = mPreviewSize!!.width
        } else {
            videoWidth = mPreviewSize!!.width
            videoHeight = mPreviewSize!!.height
        }
        YYLogUtils.w("MediaFormat的编码格式，宽：${videoWidth} 高:${videoHeight}")


        openH264Util = OpenH264Util()
        pEncoder = openH264Util?.createEncoder(videoWidth, videoHeight, file.absolutePath)

    }


    /**
     * 停止录制
     */
    fun stopRecord(block: ((path: String) -> Unit)? = null) {
        endBlock = block

        //写入自定义的结束符
        originVideoDataList.offer(byteArrayOf((-333).toByte()))

        isRecording = false
    }


    /**
     * 开始录制
     */
    fun startRecord() {
        isRecording = true
    }

    fun getOutputPath(): String {
        return file.absolutePath
    }

    /**
     * 内部包含Camera2Provider对象，释放资源会全部释放
     */
    fun destoryAll() {
        mCamera2Provider?.closeCamera()
        isRecording = false
        outputStream.close()
    }

}