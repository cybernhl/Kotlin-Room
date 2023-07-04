package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.SurfaceTexture
import android.media.AudioRecord
import android.media.Image
import android.media.MediaRecorder
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.BaseCommonCameraProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.Camera2ImageReaderProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.newki.fdkaacutil.FDKAACUtil
import com.newki.openh264util.OpenH264Util
import com.newki.yuv.YuvUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread


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


    private lateinit var h264File: File
    private lateinit var h264OutputStream: FileOutputStream

    private var configBytes: ByteArray? = null
    private var endBlock: ((path: String) -> Unit)? = null

    //音频
    private lateinit var codec: FDKAACUtil
    private lateinit var mAudioRecorder: AudioRecord
    private val minBufferSize = AudioRecord.getMinBufferSize(
        AudioConfig.SAMPLE_RATE, AudioConfig.CHANNEL_CONFIG,
        AudioConfig.AUDIO_FORMAT
    )
    private lateinit var readBuffer: ShortArray
    private lateinit var audioFile: File
    private lateinit var audioBufferedOutputStream: BufferedOutputStream
    private lateinit var audioOutputStream: FileOutputStream
    var bufferSize = 4096

    /**
     * 初始化关联信息，设置回调处理
     */
    fun setupCamera(activity: Activity, container: ViewGroup) {


        h264File = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.h264")
        if (!h264File.exists()) {
            h264File.createNewFile()
        }
        if (!h264File.isDirectory) {
            h264OutputStream = FileOutputStream(h264File, true)
        }


        audioFile = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.aac")
        if (!audioFile.exists()) {
            audioFile.createNewFile()
        }
        if (!audioFile.isDirectory) {
            audioOutputStream = FileOutputStream(audioFile, true)
            audioBufferedOutputStream = BufferedOutputStream(audioOutputStream, 4096)
        }

        //创建音频录制器对象
        mAudioRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG,
            AudioConfig.AUDIO_FORMAT,
            minBufferSize
        )

        codec = FDKAACUtil()
        codec.init(AudioConfig.SAMPLE_RATE, 2, AudioConfig.SAMPLE_RATE * 2 * 3 / 2)
        codec.initDecoder()

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
        pEncoder = openH264Util?.createEncoder(videoWidth, videoHeight, h264File.absolutePath)

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
        startAudioRecord()
        isRecording = true
    }

    fun getOutputPath(): String {
        return h264File.absolutePath
    }

    /**
     * 内部包含Camera2Provider对象，释放资源会全部释放
     */
    fun destoryAll() {
        mCamera2Provider?.closeCamera()
        isRecording = false
        h264OutputStream.close()
    }

    /**
     * 启动音频录制
     */
    fun startAudioRecord() {

        //开启线程启动录音
        thread(priority = android.os.Process.THREAD_PRIORITY_URGENT_AUDIO) {
            isRecording = true
            var encodedData: ByteArray?
            var decodedData: ByteArray?

            if (minBufferSize > bufferSize) {
                bufferSize = minBufferSize
            }

            readBuffer = ShortArray(bufferSize shr 1)

            try {

                if (AudioRecord.STATE_INITIALIZED == mAudioRecorder.state) {

                    mAudioRecorder.startRecording()  //音频录制器开始启动录制

                    while (isRecording) {
                        val readCount: Int = mAudioRecorder.read(readBuffer, 0, bufferSize shr 1)
                        Log.d("TAG", "  buffer size: " + (bufferSize shr 1) + " readCount: " + readCount)

                        encodedData = codec.encode(readBuffer)!!
                        if (encodedData != null) {
                            Log.d("TAG", "encodedData1: " + encodedData)

                            audioBufferedOutputStream.write(encodedData)
                            audioBufferedOutputStream.flush()

                            decodedData = codec.decode(encodedData)!!
                            if (decodedData != null) {
                                Log.d("TAG", "decodedData2: " + decodedData)
                                audioBufferedOutputStream.write(encodedData)
                                audioBufferedOutputStream.flush()
                            }
                        } else {
                            Log.d("TAG", " Encode fail ")
                        }
                    }

                    encodedData = codec.encode(null);
                    while (encodedData != null) {
                        Log.d("TAG", "encodedData3: " + encodedData)
                        audioBufferedOutputStream.write(encodedData)
                        audioBufferedOutputStream.flush()

                        encodedData = codec.encode(null);
                    }

                }
            } catch (e: IOException) {
                e.printStackTrace()

            } finally {
                //释放资源
                mAudioRecorder.stop()
                mAudioRecorder.release()

                codec.release()
                codec.releaseDecoder()

                audioBufferedOutputStream.close()
            }

        }

    }

}