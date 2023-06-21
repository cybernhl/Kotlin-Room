package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.app.Activity
import android.graphics.SurfaceTexture
import android.media.Image
import android.media.MediaCodec
import android.media.MediaCodecInfo
import android.media.MediaFormat
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl3.BaseCommonCameraProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl3.Camera2ImageReaderProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils.Camera2ImageUtils
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils.Camera2ImageUtils.getBytesFromImageAsType
import com.guadou.lib_baselib.utils.CommUtils
import java.io.File
import java.io.FileOutputStream
import java.util.*
import java.util.concurrent.LinkedBlockingQueue


class VideoH264RecoderUtils {

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
                    val bytesFromImageAsType = getBytesFromImageAsType(image, Camera2ImageUtils.YUV420SP)
                    originVideoDataList.offer(bytesFromImageAsType)
                }
            }

            override fun initEncode() {
                mediaCodecEncodeToH264()
            }

            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
                this@VideoH264RecoderUtils.surfaceTexture = surfaceTexture
            }
        })

        container.addView(textureView)
    }


    /**
     * 准备数据编码成H264文件
     */
    fun mediaCodecEncodeToH264() {

        if (mPreviewSize == null) return

        //配置MediaFormat信息(指定H264格式)
        val videoMediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, mPreviewSize!!.width, mPreviewSize!!.height)

        //由于本身就是使用的5.0的Camera2,可以直接用新加的颜色格式
        videoMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible)

        //设置帧率
        videoMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)

        //设置比特率
        videoMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, mPreviewSize!!.width * mPreviewSize!!.height * 5)

        //设置每秒关键帧间隔
        videoMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1)

        //创建编码器
        val videoMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)

        //这里采取的是异步回调的方式，与dequeueInputBuffer，queueInputBuffer 这样的方式获取数据有区别的
        // 一种是异步方式，一种是同步的方式。
        videoMediaCodec.setCallback(callback)
        videoMediaCodec.configure(videoMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        videoMediaCodec.start()
    }

    /**
     * 具体的音频编码Codec回调
     */
    val callback = object : MediaCodec.Callback() {

        val currentTime = Date().time * 1000  //以微秒为计算单位

        override fun onOutputFormatChanged(codec: MediaCodec, format: MediaFormat) {
        }

        override fun onError(codec: MediaCodec, e: MediaCodec.CodecException) {
            Log.e("error", e.message ?: "Error")
        }

        /**
         * 系统获取到有可用的输出buffer，写入到文件
         */
        override fun onOutputBufferAvailable(codec: MediaCodec, index: Int, info: MediaCodec.BufferInfo) {
            //获取outputBuffer
            val outputBuffer = codec.getOutputBuffer(index)
            val byteArray = ByteArray(info.size)
            outputBuffer?.get(byteArray)

            when (info.flags) {
                MediaCodec.BUFFER_FLAG_CODEC_CONFIG -> {  //编码配置完成
                    // 创建一个指定大小为 info.size 的空的 ByteArray 数组，数组内全部元素被初始化为默认值0
                    configBytes = ByteArray(info.size)
                    // arraycopy复制数组的方法，
                    // 5个参数，1.源数组 2.源数组的起始位置 3. 目标数组 4.目标组的起始位置 5. 要复制的元素数量
                    // 这里就是把配置信息全部写入到configBytes数组，用于后期的编码
                    System.arraycopy(byteArray, 0, configBytes, 0, info.size)
                }
                MediaCodec.BUFFER_FLAG_END_OF_STREAM -> {  //完成处理
                    //当全部写完之后就回调出去
                    endBlock?.invoke(file.absolutePath)
                }
                MediaCodec.BUFFER_FLAG_KEY_FRAME -> {  //包含关键帧（I帧），解码器需要这些帧才能正确解码视频序列
                    // 创建一个临时变量数组，指定大小为 info.size + 配置信息 的空数组
                    val keyframe = ByteArray(info.size + configBytes!!.size)
                    // 先 copy 写入配置信息，全部写完
                    System.arraycopy(configBytes, 0, keyframe, 0, configBytes!!.size)
                    // 再 copy 写入具体的帧数据，从配置信息的 end 索引开始写，全部写完
                    System.arraycopy(byteArray, 0, keyframe, configBytes!!.size, byteArray.size)

                    //全部写完之后我们就能写入到文件中
                    outputStream.write(keyframe, 0, keyframe.size)
                    outputStream.flush()
                }
                else -> {  //其他帧的处理
                    // 其他的数据不需要写入关键帧的配置信息，直接写入到文件即可
                    outputStream.write(byteArray)
                    outputStream.flush()
                }
            }

            //释放
            codec.releaseOutputBuffer(index, false)
        }

        /**
         * 当系统有可用的输入buffer，读取同步队列中的数据
         */
        override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {
            val inputBuffer = codec.getInputBuffer(index)
            val yuvData = originVideoDataList.poll()

            //如果获取到自定义结束符，优先结束掉
            if (yuvData != null && yuvData.size == 1 && yuvData[0] == (-333).toByte()) {
                isEndTip = true
            }

            //正常的写入
            if (yuvData != null && !isEndTip) {
                inputBuffer?.put(yuvData)
                codec.queueInputBuffer(
                    index, 0, yuvData.size,
                    surfaceTexture!!.timestamp / 1000,  //注意这里没有用系统时间，用的是surfaceTexture的时间
                    0
                )
            }

            //如果没数据，写入空数据，等待执行...
            if (yuvData == null && !isEndTip) {
                codec.queueInputBuffer(
                    index, 0, 0,
                    surfaceTexture!!.timestamp / 1000,  //注意这里没有用系统时间，用的是surfaceTexture的时间
                    0
                )
            }

            if (isEndTip) {
                codec.queueInputBuffer(
                    index, 0, 0,
                    surfaceTexture!!.timestamp / 1000,  //注意这里没有用系统时间，用的是surfaceTexture的时间
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )

            }

        }

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