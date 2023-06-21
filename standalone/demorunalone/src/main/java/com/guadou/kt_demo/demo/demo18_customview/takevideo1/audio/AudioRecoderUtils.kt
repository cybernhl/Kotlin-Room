package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.media.*
import android.util.Log
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.utils.log.YYLogUtils
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.LinkedBlockingDeque
import kotlin.concurrent.thread


class AudioRecoderUtils {

    private var mAudioMediaCodec: MediaCodec? = null
    private lateinit var mAudioRecorder: AudioRecord

    //子线程中使用同步队列保存数据
    private var mAudioList: LinkedBlockingDeque<ByteArray>? = LinkedBlockingDeque()

    //标记当前是否正在录制中
    private var isRecording: Boolean = false

    // 输入的时候标记是否是结束标记
    private var isEndTip = false

    private var minBufferSize: Int = 0

    private lateinit var file: File
    private lateinit var bufferedOutputStream: BufferedOutputStream
    private lateinit var outputStream: FileOutputStream

    private var endBlock: ((path: String) -> Unit)? = null

    init {
        initAudioRecorder()
    }

    /**
     * 初始化音频采集
     */
    private fun initAudioRecorder() {
        //根据系统提供的方法计算最小缓冲区大小
        minBufferSize = AudioRecord.getMinBufferSize(
            AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG,
            AudioConfig.AUDIO_FORMAT
        )

        //创建音频录制器对象
        mAudioRecorder = AudioRecord(
            MediaRecorder.AudioSource.MIC,
            AudioConfig.SAMPLE_RATE,
            AudioConfig.CHANNEL_CONFIG,
            AudioConfig.AUDIO_FORMAT,
            minBufferSize
        )


        file = File(CommUtils.getContext().externalCacheDir, "${System.currentTimeMillis()}-record.aac")
        if (!file.exists()) {
            file.createNewFile()
        }
        if (!file.isDirectory) {
            outputStream = FileOutputStream(file, true)
            bufferedOutputStream = BufferedOutputStream(outputStream, 4096)
        }

        YYLogUtils.w("最终输入的File文件为：" + file.absolutePath)
    }

    /**
     * 启动音频录制
     */
    fun startAudioRecord() {

        //开启线程启动录音
        thread(priority = android.os.Process.THREAD_PRIORITY_URGENT_AUDIO) {
            isRecording = true  //标记是否在录制中

            try {
                //判断AudioRecord是否初始化成功
                if (AudioRecord.STATE_INITIALIZED == mAudioRecorder.state) {

                    mAudioRecorder.startRecording()  //音频录制器开始启动录制
                    val outputArray = ByteArray(minBufferSize)

                    while (isRecording) {

                        var readCode = mAudioRecorder.read(outputArray, 0, minBufferSize)

                        //这个readCode还有很多小于0的数字，表示某种错误，
                        if (readCode > 0) {
                            val realArray = ByteArray(readCode)
                            System.arraycopy(outputArray, 0, realArray, 0, readCode)
                            //将读取的数据保存到同步队列
                            mAudioList?.offer(realArray)

                        } else {
                            Log.d("AudioRecoderUtils", "获取音频原始数据的时候出现了某些错误")
                        }
                    }

                    //自定义一个结束标记符，便于让编码器识别是录制结束
                    val stopArray = byteArrayOf((-666).toByte(), (-999).toByte())
                    //把自定义的标记符保存到同步队列
                    mAudioList?.offer(stopArray)

                }
            } catch (e: IOException) {
                e.printStackTrace()

            } finally {
                //释放资源
                mAudioRecorder.release()
            }

        }

        //测试编码
        thread {
            mediaCodecEncodeToAAC()
        }

    }


    private fun mediaCodecEncodeToAAC() {

        try {

            //创建音频MediaFormat
            val encodeFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, AudioConfig.SAMPLE_RATE, 1)

            //配置比特率
            encodeFormat.setInteger(MediaFormat.KEY_BIT_RATE, 96000)
            encodeFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC)

            //配置最大输入大小
            encodeFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, minBufferSize * 2)

            //初始化编码器
            mAudioMediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC)
            //这里采取的是异步回调的方式，与dequeueInputBuffer，queueInputBuffer 这样的方式获取数据有区别的
            // 一种是异步方式，一种是同步的方式。
            mAudioMediaCodec?.setCallback(callback)

            mAudioMediaCodec?.configure(encodeFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE)
            mAudioMediaCodec?.start()

        } catch (e: IOException) {
            Log.e("mistake", e.message ?: "Error")

        } finally {

        }

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
        override fun onOutputBufferAvailable(
            codec: MediaCodec,
            index: Int,
            info: MediaCodec.BufferInfo
        ) {

            //通过bufferinfo获取Buffer的数据，这些数据就是编码后的数据
            val outBitsSize = info.size

            //为AAC文件添加头部，头部占7字节
            //AAC有 ADIF和ADTS两种  ADIF只有一个头部剩下都是音频文件
            //ADTS是每一段编码都有一个头部
            //outpacketSize是最后头部加上返回数据后的总大小
            val outPacketSize = outBitsSize + 7 // 7 is ADTS size

            //根据index获取buffer
            val outputBuffer = codec.getOutputBuffer(index)

            // 防止buffer有offset导致自己从0开始获取，
            // 取出数据(但是我实验的offset都为0，可能有些不为0的情况)
            outputBuffer?.position(info.offset)

            //设置buffer的操作上限位置，不清楚的可以查下ByteBuffer(NIO知识),
            //了解limit ，position，clear(),filp()都是啥作用
            outputBuffer?.limit(info.offset + outBitsSize)

            //创建byte数组保存组合数据
            val outData = ByteArray(outPacketSize)

            //为数据添加头部，后面会贴出，就是在头部写入7个数据
            addADTStoPacket(AudioConfig.SAMPLE_RATE, outData, outPacketSize)

            //将buffer的数据存入数组中
            outputBuffer?.get(outData, 7, outBitsSize)

            outputBuffer?.position(info.offset)

            //将数据写到文件
            bufferedOutputStream.write(outData)
            bufferedOutputStream.flush()
            outputBuffer?.clear()

            //释放输出buffer,并释放Buffer
            codec.releaseOutputBuffer(index, false)
        }

        /**
         * 当系统有可用的输入buffer，读取同步队列中的数据
         */
        override fun onInputBufferAvailable(codec: MediaCodec, index: Int) {

            //根据index获取buffer
            val inputBuffer = codec.getInputBuffer(index)

            //从同步队列中获取还未编码的原始音频数据
            val pop = mAudioList?.poll()

            //判断是否到达音频数据的结尾，根据自定义的结束标记符判断
            if (pop != null && pop.size >= 2 && (pop[0] == (-666).toByte() && pop[1] == (-999).toByte())) {
                //如果是结束标记
                isEndTip = true
            }

            //如果数据不为空，而且不是结束标记，写入buffer，让MediaCodec去编码
            if (pop != null && !isEndTip) {

                //填入数据
                inputBuffer?.clear()
                inputBuffer?.limit(pop.size)
                inputBuffer?.put(pop, 0, pop.size)

                //将buffer还给MediaCodec，这个一定要还
                //第四个参数为时间戳，也就是，必须是递增的，系统根据这个计算
                //音频总时长和时间间隔
                codec.queueInputBuffer(
                    index,
                    0,
                    pop.size,
                    Date().time * 1000 - currentTime,
                    0
                )
            }

            // 由于2个线程谁先执行不确定，所以可能编码线程先启动，获取到队列的数据为null
            // 而且也不是结尾数据，这个时候也要调用queueInputBuffer，将buffer换回去，写入
            // 数据大小就写0

            // 如果为null就不调用queueInputBuffer  回调几次后就会导致无可用InputBuffer，
            // 从而导致MediaCodec任务结束 只能写个配置文件
            if (pop == null && !isEndTip) {

                codec.queueInputBuffer(
                    index,
                    0,
                    0,
                    Date().time * 1000 - currentTime,
                    0
                )
            }

            //发现结束标志，写入结束标志，
            //flag为MediaCodec.BUFFER_FLAG_END_OF_STREAM
            //通知编码结束
            if (isEndTip) {
                codec.queueInputBuffer(
                    index,
                    0,
                    0,
                    Date().time * 1000 - currentTime,
                    MediaCodec.BUFFER_FLAG_END_OF_STREAM
                )

                endBlock?.invoke(file.absolutePath)
            }
        }

    }

    /**
     * 停止音频录制
     */
    fun stopAudioRecord(block: ((path: String) -> Unit)? = null) {
        endBlock = block
        isRecording = false

        if (mAudioRecorder.state == AudioRecord.STATE_INITIALIZED) {
            mAudioRecorder.stop()
        }
    }

    /**
     * （网上抄的ADTS头）
     * 添加ADTS头，如果要与视频流合并就不用添加，
     * 如果是单独AAC文件就需要添加，否则无法正常播放
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

    //强制停止并释放资源
    fun release() {
        stopAudioRecord()
        mAudioRecorder.release()
        mAudioMediaCodec?.release()
        outputStream.close()
        bufferedOutputStream.close()
        isRecording = false
    }

}