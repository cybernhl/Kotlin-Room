package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.app.Activity
import android.graphics.SurfaceTexture
import android.media.*
import android.util.Log
import android.util.Size
import android.view.ViewGroup
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.BaseCommonCameraProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.Camera2ImageReaderProvider
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils.Camera2ImageUtils
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import com.theeasiestway.yuv.YuvUtils
import java.io.File
import java.nio.ByteBuffer
import java.util.concurrent.LinkedBlockingQueue
import kotlin.concurrent.thread

/**
 * 音视频录制
 */
class VideoAudioRecoderUtils {
    private val yuvUtils = YuvUtils()

    private var audioRecorder: AudioRecord? = null
    private var audioCodec: MediaCodec? = null
    private var videoCodec: MediaCodec? = null
    private lateinit var audioMediaFormat: MediaFormat
    private lateinit var videoMediaFormat: MediaFormat

    private var muxerThread: MuxThread? = null
    private lateinit var videoThread: VideoEncodeThread
    private lateinit var audioThread: AudioEncodeThread

    private var surfaceTexture: SurfaceTexture? = null
    private var mCamera2Provider: Camera2ImageReaderProvider? = null
    private var mPreviewSize: Size? = null

    private val minSize = AudioRecord.getMinBufferSize(
        AudioConfig.SAMPLE_RATE, AudioConfig.CHANNEL_CONFIG,
        AudioConfig.AUDIO_FORMAT
    )
    private var prevOutputPTSUs = 0L

    private var isFirstFrame = true // 用来判断是否为第一帧

    private lateinit var file: File
    private var endBlock: ((path: String) -> Unit)? = null


    companion object {
        var audioAddTrack = -1
        var videoAddTrack = -1

        const val TIME_OUT_US = 10000L  // Wait Time 10毫秒

        //AAC数据流  1024为一帧大小  读取时候按照一帧读取，添加时间戳
        const val SAMPLES_PER_FRAME = 1024   //1024

        @Volatile
        var isRecording = false

        @Volatile
        private var audioExit = false

        @Volatile
        private var videoExit = false
    }

    /**
     * 初始化关联信息，设置回调处理
     */
    fun setupCamera(activity: Activity, container: ViewGroup) {

        file = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.mp4")
        if (!file.exists()) {
            file.createNewFile()
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

                    //使用Java工具类转换Image对象为YUV420格式
//                    val bytesFromImageAsType = Camera2ImageUtils.getBytesFromImageAsType(image, Camera2ImageUtils.YUV420SP)


                    // 使用C库获取到I420格式，对应 COLOR_FormatYUV420Planar
                    var yuvFrame = yuvUtils.convertToI420(image)
                    // 与MediaFormat的编码格式宽高对应
                    yuvFrame = yuvUtils.rotate(yuvFrame, 90)

                    // 旋转90度之后的I420格式添加到同步队列
                    videoThread.addVideoData(yuvFrame.asArray())

                }
            }

            override fun initEncode() {
                setupMediaCodecEncode()
            }

            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
                this@VideoAudioRecoderUtils.surfaceTexture = surfaceTexture
            }
        })

        container.addView(textureView)
    }

    /**
     * 准备数据编码成H264文件
     */
    fun setupMediaCodecEncode() {

        if (mPreviewSize == null) return

        videoThread = VideoEncodeThread()
        audioThread = AudioEncodeThread()
    }

    /**
     * 开始录制
     */
    fun startRecord() {
        isFirstFrame = true
        isRecording = true

        audioThread.start()
        videoThread.start()
        muxerThread = MuxThread(file)

        muxerThread?.start()
    }


    /**
     * 停止录制
     */
    fun stopRecord(block: ((path: String) -> Unit)? = null) {
        isRecording = false
        endBlock = block

        thread {
            audioExit = true
            audioThread.join()

            videoExit = true
            videoThread.join()

            MuxThread.muxExit = true
            muxerThread?.join()
        }

    }


    fun getOutputPath(): String {
        return file.absolutePath
    }

    /**
     * 内部包含Camera2Provider对象，释放资源会全部释放
     */
    fun destoryAll() {
        stopRecord()
        mCamera2Provider?.closeCamera()

        videoThread.interrupt()
        audioThread.interrupt()
        muxerThread?.interrupt()

    }


    // =======================  Video 线程  begin ↓  =========================

    private fun initVideoFormat() {
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

        //配置MediaFormat信息(指定H264格式)
        val videoMediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, videoWidth, videoHeight)
        //添加编码需要的颜色类型
        videoMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar)
        //设置帧率
        videoMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30)
        //设置比特率
        videoMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, mPreviewSize!!.width * mPreviewSize!!.height * 5)
        //设置关键帧I帧间隔
        videoMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 2)

        videoCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC)
        videoCodec!!.configure(videoMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        videoCodec!!.start()
    }

    /**
     * 视频流的编码处理线程
     */
    inner class VideoEncodeThread : Thread() {

        //由于摄像头数据的获取与编译不是在同一个线程，还是需要同步队列保存数据
        private val videoData = LinkedBlockingQueue<ByteArray>()

        // 用于Camera的回调中添加需要编译的原始数据，这里应该为YNV420
        fun addVideoData(byteArray: ByteArray?) {
            videoData.offer(byteArray)
        }

        override fun run() {
            super.run()

            initVideoFormat()

            while (!videoExit) {
                // 从同步队列中取出 YNV420格式，直接编码为H264格式
                val poll = videoData.poll()
                if (poll != null) {
                    encodeVideo(poll, false)
                }
            }

            //发送编码结束标志
            encodeVideo(ByteArray(0), true)

            // 当编码完成之后，释放视频编码器
            videoCodec?.release()
        }
    }

    //调用Codec硬编音频为AAC格式
    // dequeueInputBuffer 获取到索引，queueInputBuffer编码写入
    private fun encodeVideo(data: ByteArray, isFinish: Boolean) {

        val videoInputBuffers = videoCodec!!.inputBuffers
        var videoOutputBuffers = videoCodec!!.outputBuffers

        val index = videoCodec!!.dequeueInputBuffer(TIME_OUT_US)

        if (index >= 0) {

            val byteBuffer = videoInputBuffers[index]
            byteBuffer.clear()
            byteBuffer.put(data)

            if (!isFinish) {
                videoCodec!!.queueInputBuffer(index, 0, data.size, System.nanoTime() / 1000, 0)
            } else {
                videoCodec!!.queueInputBuffer(
                    index,
                    0,
                    0,
                    System.nanoTime() / 1000,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )

            }
            val bufferInfo = MediaCodec.BufferInfo()
            Log.i("camera2", "编码video  $index 写入buffer ${data.size}")

            var dequeueIndex = videoCodec!!.dequeueOutputBuffer(bufferInfo, TIME_OUT_US)

            if (dequeueIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                if (MuxThread.videoMediaFormat == null)
                    MuxThread.videoMediaFormat = videoCodec!!.outputFormat
            }

            if (dequeueIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                videoOutputBuffers = videoCodec!!.outputBuffers
            }

            while (dequeueIndex >= 0) {
                val outputBuffer = videoOutputBuffers[dequeueIndex]
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                    bufferInfo.size = 0
                }
                if (bufferInfo.size != 0) {
                    muxerThread?.addVideoData(outputBuffer, bufferInfo)
                }

                Log.i(
                    "camera2",
                    "编码后video $dequeueIndex buffer.size ${bufferInfo.size} buff.position ${outputBuffer.position()}"
                )

                videoCodec!!.releaseOutputBuffer(dequeueIndex, false)

                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    dequeueIndex = videoCodec!!.dequeueOutputBuffer(bufferInfo, TIME_OUT_US)
                } else {
                    break
                }
            }
        }
    }

    /**
     * 在开始录制前调用，强制插入一个I帧
     */
    private fun forceKeyFrame() {
        val flags = MediaCodec.BUFFER_FLAG_KEY_FRAME
        val dummyData = ByteArray(0)
        val dummyIndex = videoCodec!!.dequeueInputBuffer(TIME_OUT_US)
        if (dummyIndex >= 0) {
            val byteBuffer = videoCodec!!.inputBuffers[dummyIndex]
            byteBuffer.clear()
            videoCodec!!.queueInputBuffer(dummyIndex, 0, dummyData.size, System.nanoTime() / 1000, flags)
        }
    }

    // =======================   Video 线程 end ↑ =========================


    // =======================   Audio 线程 begin ↓ =========================

    inner class AudioEncodeThread : Thread() {

        //由于音频使用同步的方式编译，且在同一个线程内，所以不需要额外使用同步队列来处理数据
//        private val audioData = LinkedBlockingQueue<ByteArray>()


        override fun run() {
            super.run()
            prepareAudioRecord()
        }
    }

    /**
     * 准备初始化AudioRecord
     */
    private fun prepareAudioRecord() {
        initAudioFormat()

        // 初始化音频录制器
        audioRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC, AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG, AudioConfig.AUDIO_FORMAT, minSize
        )

        if (audioRecorder!!.state == AudioRecord.STATE_INITIALIZED) {

            audioRecorder?.run {
                //启动音频录制器开启录音
                startRecording()

                //读取音频录制器内的数据
                val byteArray = ByteArray(SAMPLES_PER_FRAME)
                var read = read(byteArray, 0, SAMPLES_PER_FRAME)

                //已经在录制了，并且读取到有效数据
                while (read > 0 && isRecording) {
                    //拿到音频原始数据去编译为音频AAC文件
                    encodeAudio(byteArray, read, getPTSUs())
                    //继续读取音频原始数据，循环执行
                    read = read(byteArray, 0, SAMPLES_PER_FRAME)
                }

                // 当录制完成之后，释放录音器
                audioRecorder?.release()

                //发送EOS编码结束信息
                encodeAudio(ByteArray(0), 0, getPTSUs())

                // 当编码完成之后，释放音频编码器
                audioCodec?.release()
            }
        }
    }

    //调用Codec硬编音频为AAC格式
    // dequeueInputBuffer 获取到索引，queueInputBuffer编码写入
    private fun encodeAudio(audioArray: ByteArray?, read: Int, timeStamp: Long) {
        val index = audioCodec!!.dequeueInputBuffer(TIME_OUT_US)
        val audioInputBuffers = audioCodec!!.inputBuffers

        if (index >= 0) {
            val byteBuffer = audioInputBuffers[index]
            byteBuffer.clear()
            byteBuffer.put(audioArray, 0, read)
            if (read != 0) {
                audioCodec!!.queueInputBuffer(index, 0, read, timeStamp, 0)
            } else {
                audioCodec!!.queueInputBuffer(
                    index,
                    0,
                    read,
                    timeStamp,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )

            }

            val bufferInfo = MediaCodec.BufferInfo()
            Log.i("camera2", "编码audio  $index 写入buffer ${audioArray?.size}")
            var dequeueIndex = audioCodec!!.dequeueOutputBuffer(bufferInfo, TIME_OUT_US)
            if (dequeueIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
                if (MuxThread.audioMediaFormat == null) {
                    MuxThread.audioMediaFormat = audioCodec!!.outputFormat
                }
            }
            var audioOutputBuffers = audioCodec!!.outputBuffers
            if (dequeueIndex == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
                audioOutputBuffers = audioCodec!!.outputBuffers
            }
            while (dequeueIndex >= 0) {
                val outputBuffer = audioOutputBuffers[dequeueIndex]
                Log.i(
                    "camera2",
                    "编码后audio $dequeueIndex buffer.size ${bufferInfo.size} buff.position ${outputBuffer.position()}"
                )
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_CODEC_CONFIG != 0) {
                    bufferInfo.size = 0
                }
                if (bufferInfo.size != 0) {
//                    Log.i("camera2", "音频时间戳  ${bufferInfo.presentationTimeUs / 1000}")

//                    bufferInfo.presentationTimeUs = getPTSUs()

                    val byteArray = ByteArray(bufferInfo.size + 7)
                    outputBuffer.get(byteArray, 7, bufferInfo.size)
                    addADTStoPacket(0x04, byteArray, bufferInfo.size + 7)
                    outputBuffer.clear()
                    val headBuffer = ByteBuffer.allocate(byteArray.size)
                    headBuffer.put(byteArray)
                    muxerThread?.addAudioData(outputBuffer, bufferInfo)  //直接加入到封装线程了

//                    prevOutputPTSUs = bufferInfo.presentationTimeUs

                }

                audioCodec!!.releaseOutputBuffer(dequeueIndex, false)
                if (bufferInfo.flags and MediaCodec.BUFFER_FLAG_END_OF_STREAM != 0) {
                    dequeueIndex = audioCodec!!.dequeueOutputBuffer(bufferInfo, TIME_OUT_US)
                } else {
                    break
                }
            }
        }

    }

    private fun initAudioFormat() {
        audioMediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, AudioConfig.SAMPLE_RATE, 1)
        audioMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000)
        audioMediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)
        audioMediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, minSize * 2)

        audioCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
        audioCodec!!.configure(audioMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
        audioCodec!!.start()
    }

    private fun getPTSUs(): Long {

        var result = System.nanoTime() / 1000L

        if (result < prevOutputPTSUs)
            result += prevOutputPTSUs - result
        return result
    }

    /**
     * 添加ADTS头
     */
    private fun addADTStoPacket(sampleRateType: Int, packet: ByteArray, packetLen: Int) {
        val profile = 2 // AAC LC
        val chanCfg = 1 // CPE

        packet[0] = 0xFF.toByte()
        packet[1] = 0xF9.toByte()
        packet[2] = ((profile - 1 shl 6) + (sampleRateType shl 2) + (chanCfg shr 2)).toByte()
        packet[3] = ((chanCfg and 3 shl 6) + (packetLen shr 11)).toByte()
        packet[4] = (packetLen and 0x7FF shr 3).toByte()
        packet[5] = ((packetLen and 7 shl 5) + 0x1F).toByte()
        packet[6] = 0xFC.toByte()
    }

    // =======================  Audio 线程 end ↑ =========================


    // =======================  MuxThread begin ↓ =========================


    class MuxThread(val file: File) : Thread() {

        private val audioData = LinkedBlockingQueue<EncodeData>()
        private val videoData = LinkedBlockingQueue<EncodeData>()

        companion object {
            var muxIsReady = false
            var videoMediaFormat: MediaFormat? = null
            var audioMediaFormat: MediaFormat? = null
            var muxExit = false
        }

        private lateinit var mediaMuxer: MediaMuxer

        /**
         * 需要先初始化Audio线程与资源，然后添加数据源到封装类中
         */
        fun addAudioData(byteBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
            audioData.offer(EncodeData(byteBuffer, bufferInfo))
        }

        /**
         * 需要先初始化Video线程与资源，然后添加数据源到封装类中
         */
        fun addVideoData(byteBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
            videoData.offer(EncodeData(byteBuffer, bufferInfo))
        }


        private fun initMuxer() {

            mediaMuxer = MediaMuxer(file.path, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

            videoAddTrack = mediaMuxer.addTrack(videoMediaFormat!!)
            audioAddTrack = mediaMuxer.addTrack(audioMediaFormat!!)

            mediaMuxer.start()
            muxIsReady = true

        }

        private fun muxerParamtersIsReady() = audioMediaFormat != null && videoMediaFormat != null

        override fun run() {
            super.run()

            //校验音频编码与视频编码不为空
            while (!muxerParamtersIsReady()) {
            }

            initMuxer()

            while (!muxExit) {
                if (audioAddTrack != -1) {
                    if (audioData.isNotEmpty()) {
                        val poll = audioData.poll()
                        Log.i("camera2", "混合写入audio音频 ${poll.bufferInfo.size} ")
                        mediaMuxer.writeSampleData(audioAddTrack, poll.buffer, poll.bufferInfo)

                    }
                }
                if (videoAddTrack != -1) {
                    if (videoData.isNotEmpty()) {
                        val poll = videoData.poll()
                        Log.i("camera2", "混合写入video视频 ${poll.bufferInfo.size} ")
                        mediaMuxer.writeSampleData(videoAddTrack, poll.buffer, poll.bufferInfo)

                    }
                }
            }

            mediaMuxer.stop()
            mediaMuxer.release()

            Log.i("camera2", "合成器释放")
            Log.i("camera2", "未写入audio音频 ${audioData.size}")
            Log.i("camera2", "未写入video视频 ${videoData.size}")

        }
    }

    // =======================  MuxThread end ↑ =========================
}