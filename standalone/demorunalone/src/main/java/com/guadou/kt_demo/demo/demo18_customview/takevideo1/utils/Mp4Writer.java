package com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;


import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Mp4Writer {

    private final BaseMessageLoop mThread;

    private static final int TYPE_BEGIN = 0;
    private static final int TYPE_WRITE = 1;
    private static final int TYPE_END = 2;

    private int encodeVideoTrackIndex;
    private volatile boolean isRunning = false;
    private MediaCodec encodeCodec;
    private MediaMuxer mMediaMuxer;
    private int encodeIndex = 0;
    private int encodeFrameRate = 0;

    public Mp4Writer(Context context, int width, int height, int fps, String saveMp4Path) {
        encodeFrameRate = fps;

        mThread = new BaseMessageLoop(context, "write") {
            @Override
            protected boolean recvHandleMessage(Message msg) {
                YYLogUtils.d("recvHandleMessage:" + msg.what);
                switch (msg.what) {
                    case TYPE_BEGIN:
                        try {
                            encodeIndex = 0;
                            init(width, height, saveMp4Path);
                            isRunning = true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            stopAndRelease();
                            Quit();
                        }
                        break;
                    case TYPE_WRITE:
                        if (isRunning) {
                            byte[] frameData = (byte[]) msg.obj;
                            encodeData(frameData, encodeIndex++, false);
                        }
                        break;
                    case TYPE_END:
                        if (isRunning) {
                            stopAndRelease();
                            isRunning = false;
                            Quit();
                        }
                        break;
                    default:
                }
                return false;
            }
        };
        mThread.Run();
    }

    private void init(int width, int height, String savePath) throws IOException {


        MediaFormat encodeMediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        encodeMediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        encodeMediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        encodeMediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 2500000);
        encodeMediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        encodeCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        encodeCodec.configure(encodeMediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        encodeCodec.start();

        mMediaMuxer = new MediaMuxer(savePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    }

    private void stopAndRelease() {
        mMediaMuxer.stop();
        mMediaMuxer.release();
        encodeCodec.stop();
        encodeCodec.release();
    }

    private void encodeData(byte[] yuvBytes, int encodeIndex, boolean isVideoEOS) {
        long presentationTimeUs = 1000 * 1000 / encodeFrameRate * encodeIndex;
        Log.d(MediaCodecUtil.TAG, "presentationTimeUs::" + presentationTimeUs);

        MediaCodec.BufferInfo encodeOutputBufferInfo = new MediaCodec.BufferInfo();
        MediaCodec.BufferInfo muxerOutputBufferInfo = new MediaCodec.BufferInfo();

        int inputBufferIndex = encodeCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = encodeCodec.getInputBuffer(inputBufferIndex);
            inputBuffer.put(yuvBytes);
            encodeCodec.queueInputBuffer(inputBufferIndex, 0, yuvBytes.length, presentationTimeUs, 0);
        }

        if ((encodeOutputBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
            YYLogUtils.d(" encode  buffer stream end");
        }

        int outputBufferIndex = encodeCodec.dequeueOutputBuffer(encodeOutputBufferInfo, -1);
        switch (outputBufferIndex) {
            case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                MediaFormat newFormat = encodeCodec.getOutputFormat();
                encodeVideoTrackIndex = mMediaMuxer.addTrack(newFormat);
                mMediaMuxer.start();
                break;
            case MediaCodec.INFO_TRY_AGAIN_LATER:
                break;
            default:
                ByteBuffer outputBuffer = encodeCodec.getOutputBuffer(outputBufferIndex);
                byte[] outData = new byte[encodeOutputBufferInfo.size];
                outputBuffer.get(outData);

                MediaFormat outputFormat = encodeCodec.getOutputFormat(outputBufferIndex);
                YYLogUtils.d("outputFormat::" + outputFormat.toString());

                muxerOutputBufferInfo.offset = 0;
                muxerOutputBufferInfo.size = encodeOutputBufferInfo.size;
                muxerOutputBufferInfo.flags = isVideoEOS ? MediaCodec.BUFFER_FLAG_END_OF_STREAM : MediaCodec.BUFFER_FLAG_KEY_FRAME;
                muxerOutputBufferInfo.presentationTimeUs = presentationTimeUs;
                YYLogUtils.d("presentationTimeUs::" + presentationTimeUs + " size::" + encodeOutputBufferInfo.size + "  isVideoEOS:" + isVideoEOS);
                mMediaMuxer.writeSampleData(encodeVideoTrackIndex, outputBuffer, encodeOutputBufferInfo);

                encodeCodec.releaseOutputBuffer(outputBufferIndex, false);
                break;
        }

    }

    public void startWrite() {
        mThread.sendEmptyMessage(TYPE_BEGIN);
    }

    public void write(byte[] frame) {
        mThread.sendMessage(TYPE_WRITE, 0, 0, frame);
    }

    public void endWrite() {
        if (isRunning) {
            encodeData(new byte[0], encodeIndex++, false);
        }
        mThread.removeMessages(TYPE_WRITE);
        mThread.sendEmptyMessage(TYPE_END);
    }

}
