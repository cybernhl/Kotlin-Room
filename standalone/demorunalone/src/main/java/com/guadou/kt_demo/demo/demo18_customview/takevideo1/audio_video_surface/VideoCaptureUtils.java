package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.location.Location;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.CamcorderProfile;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import androidx.annotation.GuardedBy;
import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.UiThread;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraXThreads;
import androidx.camera.core.impl.utils.executor.CameraXExecutors;
import androidx.camera.core.internal.utils.VideoUtil;
import androidx.core.util.Preconditions;

import com.google.common.util.concurrent.ListenableFuture;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint({"RestrictedApi", "MissingPermission"})
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public final class VideoCaptureUtils {

    //未知错误
    public static final int ERROR_UNKNOWN = 0;

    //编码错误
    public static final int ERROR_ENCODER = 1;

    //封装格式错误
    public static final int ERROR_MUXER = 2;

    //真正录制中，不能重复录制的错误
    public static final int ERROR_RECORDING_IN_PROGRESS = 3;

    //文件操作错误
    public static final int ERROR_FILE_IO = 4;

    //开启相机的错误
    public static final int ERROR_INVALID_CAMERA = 5;

    private static final String TAG = "VideoCaptureUtils";

    //等待从视频编码器中取出缓冲区的时间量.
    private static final int DEQUE_TIMEOUT_USEC = 10000;

    //视频流编码格式默认为AVC(H264)
    private static final String VIDEO_MIME_TYPE = "video/avc";

    //音频流的编码格式默认为audio/mp4a-latm（没有用AAC）
    private static final String AUDIO_MIME_TYPE = "audio/mp4a-latm";

    //视频的拍摄质量，对应的去设置音频的推荐参数
    private static final int[] CamcorderQuality = {
            CamcorderProfile.QUALITY_2160P,
            CamcorderProfile.QUALITY_1080P,
            CamcorderProfile.QUALITY_720P,
            CamcorderProfile.QUALITY_480P
    };

    //音频的编码格式，其他两个质量不好，PCM_16BIT为默认选项
    private static final short[] sAudioEncoding = {
            AudioFormat.ENCODING_PCM_16BIT,
            AudioFormat.ENCODING_PCM_8BIT,
            AudioFormat.ENCODING_PCM_FLOAT
    };

    private final MediaCodec.BufferInfo mVideoBufferInfo = new MediaCodec.BufferInfo();
    private final MediaCodec.BufferInfo mAudioBufferInfo = new MediaCodec.BufferInfo();
    //封装格式的锁
    private final Object mMediaMuxerLock = new Object();

    // 启动录制与结束录制的线程锁
    private final AtomicBoolean mEndOfVideoStreamSignal = new AtomicBoolean(true);
    private final AtomicBoolean mEndOfAudioStreamSignal = new AtomicBoolean(true);
    private final AtomicBoolean mEndOfAudioVideoSignal = new AtomicBoolean(true);
    private final AtomicBoolean mIsFirstVideoSampleWrite = new AtomicBoolean(false);
    private final AtomicBoolean mIsFirstAudioSampleWrite = new AtomicBoolean(false);

    // 视频录制的配置信息
    private final RecordConfig mRecordConfig;
    // 预览与录制的宽高
    private final Size mResolutionSize;

    // 视频流的编码处理线程
    private HandlerThread mVideoHandlerThread;
    private Handler mVideoHandler;
    // 音频流的编码处理线程
    private HandlerThread mAudioHandlerThread;
    private Handler mAudioHandler;

    @NonNull
    public MediaCodec mVideoEncoder;
    @NonNull
    private MediaCodec mAudioEncoder;
    @Nullable
    private ListenableFuture<Void> mRecordingFuture = null;

    /**
     * 封装格式对象 MediaMuxer
     */
    @GuardedBy("mMuxerLock")
    private MediaMuxer mMediaMuxer;
    private boolean mMuxerStarted = false;

    //用于添加到MediaMuxer中的视频轨道
    private int mVideoTrackIndex;

    //用于添加到MediaMuxer中的音频轨道
    private int mAudioTrackIndex;

    // 用于输入给视频编码的输入源
    public Surface mCameraSurface;


    /**
     * 通过AudioRecord获取到音频源数据RAW
     */
    @NonNull
    private AudioRecord mAudioRecorder;
    private int mAudioBufferSize;
    //标记当前的录制状态，是否正在录制
    private boolean mIsRecording = false;

    //最终的MP4文件输出到的文件地址
    @SuppressWarnings("WeakerAccess")
    Uri mSavedVideoUri;
    private ParcelFileDescriptor mParcelFileDescriptor;


    public VideoCaptureUtils(@NonNull RecordConfig config, Size size) {
        this.mRecordConfig = config;
        this.mResolutionSize = size;

        // 初始化音视频编码线程
        mVideoHandlerThread = new HandlerThread(CameraXThreads.TAG + "video encoding thread");
        mAudioHandlerThread = new HandlerThread(CameraXThreads.TAG + "audio encoding thread");

        // 启动视频线程
        mVideoHandlerThread.start();
        mVideoHandler = new Handler(mVideoHandlerThread.getLooper());

        // 启动音频线程
        mAudioHandlerThread.start();
        mAudioHandler = new Handler(mAudioHandlerThread.getLooper());

        if (mCameraSurface != null) {
            mVideoEncoder.stop();
            mVideoEncoder.release();
            mAudioEncoder.stop();
            mAudioEncoder.release();
            releaseCameraSurface(false);
        }

        try {
            mVideoEncoder = MediaCodec.createEncoderByType(VIDEO_MIME_TYPE);
            mAudioEncoder = MediaCodec.createEncoderByType(AUDIO_MIME_TYPE);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to create MediaCodec due to: " + e.getCause());
        }

        //设置音视频编码器与音频录制器
        setupEncoder();
    }


    /**
     * 开始录制，启动各种编码器与录制器
     */
    @SuppressLint("WrongConstant")
    public void startRecording(
            @NonNull OutputFileOptions outputFileOptions,
            @NonNull Executor executor,
            @NonNull OnVideoSavedCallback callback) {

        if (Looper.getMainLooper() != Looper.myLooper()) {
            CameraXExecutors.mainThreadExecutor().execute(() -> startRecording(outputFileOptions, executor, callback));
            return;
        }

        Log.d(TAG, "startRecording");
        mIsFirstVideoSampleWrite.set(false);
        mIsFirstAudioSampleWrite.set(false);

        VideoSavedListenerWrapper postListener = new VideoSavedListenerWrapper(executor, callback);

        //重复录制的错误
        if (!mEndOfAudioVideoSignal.get()) {
            postListener.onError(ERROR_RECORDING_IN_PROGRESS, "It is still in video recording!", null);
            return;
        }

        try {
            // 启动音频录制器
            mAudioRecorder.startRecording();
        } catch (IllegalStateException e) {
            postListener.onError(ERROR_ENCODER, "AudioRecorder start fail", e);
            return;
        }

        try {
            // 音视频编码器启动
            Log.d(TAG, "audioEncoder and videoEncoder all start");
            mVideoEncoder.start();
            mAudioEncoder.start();

        } catch (IllegalStateException e) {

            postListener.onError(ERROR_ENCODER, "Audio/Video encoder start fail", e);
            return;
        }

        //启动封装器
        try {
            synchronized (mMediaMuxerLock) {
                mMediaMuxer = initMediaMuxer(outputFileOptions);
                Preconditions.checkNotNull(mMediaMuxer);
                mMediaMuxer.setOrientationHint(90); //设置视频文件的方向，参数表示视频文件应该被旋转的角度

                Metadata metadata = outputFileOptions.getMetadata();
                if (metadata != null && metadata.location != null) {
                    mMediaMuxer.setLocation(
                            (float) metadata.location.getLatitude(),
                            (float) metadata.location.getLongitude());
                }
            }
        } catch (IOException e) {

            postListener.onError(ERROR_MUXER, "MediaMuxer creation failed!", e);

            return;
        }

        //设置开始录制的Flag变量
        mEndOfVideoStreamSignal.set(false);
        mEndOfAudioStreamSignal.set(false);
        mEndOfAudioVideoSignal.set(false);
        mIsRecording = true;

        //子线程开启编码音频
        mAudioHandler.post(() -> audioEncode(postListener));

        //子线程开启编码视频
        mVideoHandler.post(() -> {
            boolean errorOccurred = videoEncode(postListener);
            if (!errorOccurred) {
                postListener.onVideoSaved(new OutputFileResults(mSavedVideoUri));
                mSavedVideoUri = null;
            }
        });
    }

    /**
     * 停止录制
     */
    public void stopRecording() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            CameraXExecutors.mainThreadExecutor().execute(() -> stopRecording());
            return;
        }

        Log.d(TAG, "stopRecording");

        if (!mEndOfAudioVideoSignal.get() && mIsRecording) {
            // 停止音频编码器线程并等待视频编码器与封装器停止
            mEndOfAudioStreamSignal.set(true);
        }
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public void release() {
        stopRecording();

        if (mRecordingFuture != null) {
            mRecordingFuture.addListener(() -> releaseResources(),
                    CameraXExecutors.mainThreadExecutor());
        } else {
            releaseResources();
        }
    }

    private void releaseResources() {
        mVideoHandlerThread.quitSafely();
        mAudioHandlerThread.quitSafely();

        if (mAudioEncoder != null) {
            mAudioEncoder.release();
            mAudioEncoder = null;
        }

        if (mAudioRecorder != null) {
            mAudioRecorder.release();
            mAudioRecorder = null;
        }

        if (mCameraSurface != null) {
            releaseCameraSurface(true);
        }
    }


    @UiThread
    private void releaseCameraSurface(final boolean releaseVideoEncoder) {

        final MediaCodec videoEncoder = mVideoEncoder;

        if (releaseVideoEncoder && videoEncoder != null) {
            videoEncoder.release();
        }

        if (releaseVideoEncoder) {
            mVideoEncoder = null;
        }

        mCameraSurface = null;
    }


    /**
     * 设置音视频编码格式与音频录制器
     */
    @UiThread
    @SuppressWarnings("WeakerAccess")
    void setupEncoder() {

        // 初始化视频编码器
        mVideoEncoder.reset();
        mVideoEncoder.configure(createVideoMediaFormat(), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        if (mCameraSurface != null) {
            releaseCameraSurface(false);
        }

        //用于输入的Surface
        mCameraSurface = mVideoEncoder.createInputSurface();

        //初始化音频编码器
        mAudioEncoder.reset();
        mAudioEncoder.configure(createAudioMediaFormat(), null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);

        //初始化音频录制器
        if (mAudioRecorder != null) {
            mAudioRecorder.release();
        }

        mAudioRecorder = autoConfigAudioRecordSource();
        if (mAudioRecorder == null) {
            Log.e(TAG, "AudioRecord object cannot initialized correctly!");
        }

        //重置音视频轨道，设置未开始录制
        mVideoTrackIndex = -1;
        mAudioTrackIndex = -1;
        mIsRecording = false;
    }

    /**
     * 将已编码的《视频流》写入缓冲区
     */
    private boolean writeVideoEncodedBuffer(int bufferIndex) {
        if (bufferIndex < 0) {
            Log.e(TAG, "Output buffer should not have negative index: " + bufferIndex);
            return false;
        }
        // Get data from buffer
        ByteBuffer outputBuffer = mVideoEncoder.getOutputBuffer(bufferIndex);

        // Check if buffer is valid, if not then return
        if (outputBuffer == null) {
            Log.d(TAG, "OutputBuffer was null.");
            return false;
        }

        // Write data to mMuxer if available
        if (mAudioTrackIndex >= 0 && mVideoTrackIndex >= 0 && mVideoBufferInfo.size > 0) {
            outputBuffer.position(mVideoBufferInfo.offset);
            outputBuffer.limit(mVideoBufferInfo.offset + mVideoBufferInfo.size);
            mVideoBufferInfo.presentationTimeUs = (System.nanoTime() / 1000);

            synchronized (mMediaMuxerLock) {
                if (!mIsFirstVideoSampleWrite.get()) {
                    Log.d(TAG, "First video sample written.");
                    mIsFirstVideoSampleWrite.set(true);
                }
                Log.d(TAG, "write video Data");
                mMediaMuxer.writeSampleData(mVideoTrackIndex, outputBuffer, mVideoBufferInfo);
            }
        }

        // Release data
        mVideoEncoder.releaseOutputBuffer(bufferIndex, false);

        // Return true if EOS is set
        return (mVideoBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
    }

    /**
     * 将已编码《音频流》写入缓冲区
     */
    private boolean writeAudioEncodedBuffer(int bufferIndex) {
        ByteBuffer buffer = getOutputBuffer(mAudioEncoder, bufferIndex);
        buffer.position(mAudioBufferInfo.offset);
        if (mAudioTrackIndex >= 0 && mVideoTrackIndex >= 0
                && mAudioBufferInfo.size > 0
                && mAudioBufferInfo.presentationTimeUs > 0) {
            try {
                synchronized (mMediaMuxerLock) {
                    if (!mIsFirstAudioSampleWrite.get()) {
                        Log.d(TAG, "First audio sample written.");
                        mIsFirstAudioSampleWrite.set(true);
                    }

                    mMediaMuxer.writeSampleData(mAudioTrackIndex, buffer, mAudioBufferInfo);
                }
            } catch (Exception e) {
                Log.e(TAG, "audio error:size="
                        + mAudioBufferInfo.size
                        + "/offset="
                        + mAudioBufferInfo.offset
                        + "/timeUs="
                        + mAudioBufferInfo.presentationTimeUs);
                e.printStackTrace();
            }
        }
        mAudioEncoder.releaseOutputBuffer(bufferIndex, false);
        return (mAudioBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0;
    }

    /**
     * 具体的视频编码方法，子线程中执行编码逻辑，无限执行知道录制结束。
     * 当编码完成之后写入到缓冲区
     */
    boolean videoEncode(@NonNull OnVideoSavedCallback videoSavedCallback) {
        // Main encoding loop. Exits on end of stream.
        boolean errorOccurred = false;
        boolean videoEos = false;

        while (!videoEos && !errorOccurred && mVideoEncoder != null) {
            // Check for end of stream from main thread
            if (mEndOfVideoStreamSignal.get()) {
                mVideoEncoder.signalEndOfInputStream();
                mEndOfVideoStreamSignal.set(false);
            }

            // Deque buffer to check for processing step
            int outputBufferId = mVideoEncoder.dequeueOutputBuffer(mVideoBufferInfo, DEQUE_TIMEOUT_USEC);
            switch (outputBufferId) {
                case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                    if (mMuxerStarted) {
                        videoSavedCallback.onError(ERROR_ENCODER, "Unexpected change in video encoding format.", null);
                        errorOccurred = true;
                    }

                    synchronized (mMediaMuxerLock) {
                        mVideoTrackIndex = mMediaMuxer.addTrack(mVideoEncoder.getOutputFormat());
                        Log.d(TAG, "mAudioTrackIndex：" + mAudioTrackIndex + "mVideoTrackIndex:" + mVideoTrackIndex);
                        if (mAudioTrackIndex >= 0 && mVideoTrackIndex >= 0) {
                            mMuxerStarted = true;
                            Log.i(TAG, "media mMuxer start by video");
                            mMediaMuxer.start();
                        }
                    }
                    break;
                case MediaCodec.INFO_TRY_AGAIN_LATER:
                    // Timed out. Just wait until next attempt to deque.
                    break;
                default:
                    videoEos = writeVideoEncodedBuffer(outputBufferId);
            }
        }

        //如果循环结束，说明录制完成，停止视频编码器，释放资源
        try {
            Log.i(TAG, "videoEncoder stop");
            mVideoEncoder.stop();
        } catch (IllegalStateException e) {
            videoSavedCallback.onError(ERROR_ENCODER, "Video encoder stop failed!", e);
            errorOccurred = true;
        }

        //因为视频编码会更耗时，所以在此停止封装器的执行
        try {
            synchronized (mMediaMuxerLock) {
                if (mMediaMuxer != null) {
                    if (mMuxerStarted) {
                        mMediaMuxer.stop();
                    }
                    mMediaMuxer.release();
                    mMediaMuxer = null;
                }
            }
        } catch (IllegalStateException e) {
            videoSavedCallback.onError(ERROR_MUXER, "Muxer stop failed!", e);
            errorOccurred = true;
        }

        if (mParcelFileDescriptor != null) {
            try {
                mParcelFileDescriptor.close();
                mParcelFileDescriptor = null;
            } catch (IOException e) {
                videoSavedCallback.onError(ERROR_MUXER, "File descriptor close failed!", e);
                errorOccurred = true;
            }
        }

        //设置一些Flag为停止状态
        mMuxerStarted = false;
        mEndOfAudioVideoSignal.set(true);

        Log.d(TAG, "Video encode thread end.");

        return errorOccurred;
    }

    /**
     * 具体的音频编码方法，子线程中执行编码逻辑，无限执行知道录制结束。
     * 当编码完成之后写入到缓冲区
     */
    boolean audioEncode(OnVideoSavedCallback videoSavedCallback) {
        // Audio encoding loop. Exits on end of stream.
        boolean audioEos = false;
        int outIndex;

        while (!audioEos && mIsRecording && mAudioEncoder != null) {
            // Check for end of stream from main thread
            if (mEndOfAudioStreamSignal.get()) {
                mEndOfAudioStreamSignal.set(false);
                mIsRecording = false;
            }

            // get audio deque input buffer
            if (mAudioEncoder != null) {
                int index = mAudioEncoder.dequeueInputBuffer(-1);
                if (index >= 0) {
                    final ByteBuffer buffer = getInputBuffer(mAudioEncoder, index);
                    buffer.clear();
                    int length = mAudioRecorder.read(buffer, mAudioBufferSize);
                    if (length > 0) {
                        mAudioEncoder.queueInputBuffer(
                                index,
                                0,
                                length,
                                (System.nanoTime() / 1000),
                                mIsRecording ? 0 : MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                    }
                }


                // start to dequeue audio output buffer
                do {
                    outIndex = mAudioEncoder.dequeueOutputBuffer(mAudioBufferInfo, 0);
                    switch (outIndex) {
                        case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                            synchronized (mMediaMuxerLock) {
                                mAudioTrackIndex = mMediaMuxer.addTrack(mAudioEncoder.getOutputFormat());
                                Log.d(TAG, "mAudioTrackIndex：" + mAudioTrackIndex + "mVideoTrackIndex:" + mVideoTrackIndex);
                                if (mAudioTrackIndex >= 0 && mVideoTrackIndex >= 0) {
                                    mMuxerStarted = true;
                                    Log.d(TAG, "media mMuxer start by audio");
                                    mMediaMuxer.start();
                                }
                            }
                            break;
                        case MediaCodec.INFO_TRY_AGAIN_LATER:
                            break;
                        default:
                            audioEos = writeAudioEncodedBuffer(outIndex);
                    }
                } while (outIndex >= 0 && !audioEos);
            }
        }

        //当循环结束，说明停止录制了，停止音频录制器
        try {
            Log.d(TAG, "audioRecorder stop");
            mAudioRecorder.stop();
        } catch (IllegalStateException e) {
            videoSavedCallback.onError(ERROR_ENCODER, "Audio recorder stop failed!", e);
        }

        //停止音频编码器
        try {
            mAudioEncoder.stop();
        } catch (IllegalStateException e) {
            videoSavedCallback.onError(ERROR_ENCODER, "Audio encoder stop failed!", e);
        }

        Log.d(TAG, "Audio encode thread end");

        mEndOfVideoStreamSignal.set(true);

        return false;
    }

    private ByteBuffer getInputBuffer(MediaCodec codec, int index) {
        return codec.getInputBuffer(index);
    }

    private ByteBuffer getOutputBuffer(MediaCodec codec, int index) {
        return codec.getOutputBuffer(index);
    }

    /**
     * 创建视频编码格式
     */
    private MediaFormat createVideoMediaFormat() {
        MediaFormat format = MediaFormat.createVideoFormat(VIDEO_MIME_TYPE, mResolutionSize.getWidth(), mResolutionSize.getHeight());
        format.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
        format.setInteger(MediaFormat.KEY_BIT_RATE, mRecordConfig.getBitRate());
        format.setInteger(MediaFormat.KEY_FRAME_RATE, mRecordConfig.getVideoFrameRate());
        format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, mRecordConfig.getIntraFrameInterval());
        return format;
    }

    /**
     * 创建音频的编码格式
     */
    private MediaFormat createAudioMediaFormat() {
        MediaFormat format = MediaFormat.createAudioFormat(AUDIO_MIME_TYPE, mRecordConfig.getAudioSampleRate(), mRecordConfig.getAudioChannelCount());
        format.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        format.setInteger(MediaFormat.KEY_BIT_RATE, mRecordConfig.getAudioBitRate());

        return format;
    }

    /**
     * 创建AudioRecord对象以获取原始数据
     */
    private AudioRecord autoConfigAudioRecordSource() {

        for (short audioFormat : sAudioEncoding) {

            int channelConfig = mRecordConfig.getAudioChannelCount() == 1
                    ? AudioFormat.CHANNEL_IN_MONO
                    : AudioFormat.CHANNEL_IN_STEREO;

            int source = mRecordConfig.getAudioRecordSource();

            try {
                int bufferSize = AudioRecord.getMinBufferSize(mRecordConfig.getAudioSampleRate(), channelConfig, audioFormat);

                if (bufferSize <= 0) {
                    bufferSize = mRecordConfig.getAudioMinBufferSize();
                }

                AudioRecord recorder =
                        new AudioRecord(source,
                                mRecordConfig.getAudioSampleRate(),
                                channelConfig,
                                audioFormat,
                                bufferSize * 2);

                if (recorder.getState() == AudioRecord.STATE_INITIALIZED) {
                    mAudioBufferSize = bufferSize;
                    Log.d(TAG, "source: "
                            + source
                            + " audioSampleRate: "
                            + mRecordConfig.getAudioSampleRate()
                            + " channelConfig: "
                            + channelConfig
                            + " audioFormat: "
                            + audioFormat
                            + " bufferSize: "
                            + bufferSize);
                    return recorder;
                }
            } catch (Exception e) {
                Log.e(TAG, "Exception, keep trying.", e);
            }
        }

        return null;
    }


    /**
     * 获取MediaMuxer实例，兼容做了处理
     */
    @SuppressLint("UnsafeNewApiCall")
    @NonNull
    private MediaMuxer initMediaMuxer(@NonNull OutputFileOptions outputFileOptions) throws IOException {

        MediaMuxer mediaMuxer;

        if (outputFileOptions.isSavingToFile()) {
            File savedVideoFile = outputFileOptions.getFile();
            mSavedVideoUri = Uri.fromFile(outputFileOptions.getFile());

            mediaMuxer = new MediaMuxer(savedVideoFile.getAbsolutePath(),
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } else if (outputFileOptions.isSavingToFileDescriptor()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                throw new IllegalArgumentException("Using a FileDescriptor to record a video is "
                        + "only supported for Android 8.0 or above.");
            }

            mediaMuxer = new MediaMuxer(outputFileOptions.getFileDescriptor(),
                    MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        } else if (outputFileOptions.isSavingToMediaStore()) {
            ContentValues values = outputFileOptions.getContentValues() != null
                    ? new ContentValues(outputFileOptions.getContentValues())
                    : new ContentValues();

            mSavedVideoUri = outputFileOptions.getContentResolver().insert(outputFileOptions.getSaveCollection(), values);

            if (mSavedVideoUri == null) {
                throw new IOException("Invalid Uri!");
            }

            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
                    String savedLocationPath = VideoUtil.getAbsolutePathFromUri(
                            outputFileOptions.getContentResolver(), mSavedVideoUri);

                    Log.i(TAG, "Saved Location Path: " + savedLocationPath);
                    mediaMuxer = new MediaMuxer(savedLocationPath,
                            MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                } else {
                    mParcelFileDescriptor = outputFileOptions.getContentResolver().openFileDescriptor(mSavedVideoUri, "rw");
                    mediaMuxer = new MediaMuxer(mParcelFileDescriptor.getFileDescriptor(), MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                }
            } catch (IOException e) {
                mSavedVideoUri = null;
                throw e;
            }
        } else {
            throw new IllegalArgumentException("The OutputFileOptions should assign before recording");
        }

        return mediaMuxer;
    }


    @IntDef({ERROR_UNKNOWN, ERROR_ENCODER, ERROR_MUXER, ERROR_RECORDING_IN_PROGRESS,
            ERROR_FILE_IO, ERROR_INVALID_CAMERA})
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public @interface VideoCaptureError {
    }

    /**
     * 保存到视频本地的回调监听
     */
    public interface OnVideoSavedCallback {
        /**
         * 保存本地成功
         */
        void onVideoSaved(@NonNull OutputFileResults outputFileResults);

        /**
         * 保存出错了
         */
        void onError(@VideoCaptureError int videoCaptureError, @NonNull String message, @Nullable Throwable cause);
    }


    /**
     * 录制音视频的一些编码器和其他配置信息
     */
    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static final class RecordConfig {

        private static final int DEFAULT_VIDEO_FRAME_RATE = 30;

        private static final int DEFAULT_BIT_RATE = 8 * 1024 * 1024;

        private static final int DEFAULT_INTRA_FRAME_INTERVAL = 1;

        private static final int DEFAULT_AUDIO_BIT_RATE = 64000;

        private static final int DEFAULT_AUDIO_SAMPLE_RATE = 8000;

        private static final int DEFAULT_AUDIO_CHANNEL_COUNT = 1;

        private static final int DEFAULT_AUDIO_RECORD_SOURCE = MediaRecorder.AudioSource.MIC;

        private static final int DEFAULT_AUDIO_MIN_BUFFER_SIZE = 1024;

        private static final Size DEFAULT_MAX_RESOLUTION = new Size(1920, 1080);

        private static final int DEFAULT_SURFACE_OCCUPANCY_PRIORITY = 3;
        private static final int DEFAULT_ASPECT_RATIO = AspectRatio.RATIO_16_9;

        private int videoFrameRate;
        private int bitRate;
        private int intraFrameInterval;
        private int audioBitRate;
        private int audioSampleRate;
        private int audioChannelCount;
        private int audioRecordSource;
        private int audioMinBufferSize;
        private Size maxResolution;
        private int surfaceOccupancyPriority;
        private int targetAspectRatio;

        public int getVideoFrameRate() {
            return videoFrameRate;
        }

        public int getBitRate() {
            return bitRate;
        }

        public int getIntraFrameInterval() {
            return intraFrameInterval;
        }

        public int getAudioBitRate() {
            return audioBitRate;
        }

        public int getAudioSampleRate() {
            return audioSampleRate;
        }

        public int getAudioChannelCount() {
            return audioChannelCount;
        }

        public int getAudioRecordSource() {
            return audioRecordSource;
        }

        public int getAudioMinBufferSize() {
            return audioMinBufferSize;
        }

        public Size getMaxResolution() {
            return maxResolution;
        }

        public int getSurfaceOccupancyPriority() {
            return surfaceOccupancyPriority;
        }

        public int getTargetAspectRatio() {
            return targetAspectRatio;
        }

        public static class Builder {

            private int videoFrameRate = DEFAULT_VIDEO_FRAME_RATE;
            private int bitRate = DEFAULT_BIT_RATE;
            private int intraFrameInterval = DEFAULT_INTRA_FRAME_INTERVAL;
            private int audioBitRate = DEFAULT_AUDIO_BIT_RATE;
            private int audioSampleRate = DEFAULT_AUDIO_SAMPLE_RATE;
            private int audioChannelCount = DEFAULT_AUDIO_CHANNEL_COUNT;
            private int audioRecordSource = DEFAULT_AUDIO_RECORD_SOURCE;
            private int audioMinBufferSize = DEFAULT_AUDIO_MIN_BUFFER_SIZE;
            private Size maxResolution = DEFAULT_MAX_RESOLUTION;
            private int surfaceOccupancyPriority = DEFAULT_SURFACE_OCCUPANCY_PRIORITY;
            private int targetAspectRatio = DEFAULT_ASPECT_RATIO;

            public Builder setVideoFrameRate(int videoFrameRate) {
                this.videoFrameRate = videoFrameRate;
                return this;
            }

            public Builder setBitRate(int bitRate) {
                this.bitRate = bitRate;
                return this;
            }

            public Builder setIFrameInterval(int intraFrameInterval) {
                this.intraFrameInterval = intraFrameInterval;
                return this;
            }

            public Builder setAudioBitRate(int audioBitRate) {
                this.audioBitRate = audioBitRate;
                return this;
            }

            public Builder setAudioSampleRate(int audioSampleRate) {
                this.audioSampleRate = audioSampleRate;
                return this;
            }

            public Builder setAudioChannelCount(int audioChannelCount) {
                this.audioChannelCount = audioChannelCount;
                return this;
            }

            public Builder setAudioRecordSource(int audioRecordSource) {
                this.audioRecordSource = audioRecordSource;
                return this;
            }

            public Builder setAudioMinBufferSize(int audioMinBufferSize) {
                this.audioMinBufferSize = audioMinBufferSize;
                return this;
            }

            public Builder setMaxResolution(Size maxResolution) {
                this.maxResolution = maxResolution;
                return this;
            }

            public Builder setSurfaceOccupancyPriority(int surfaceOccupancyPriority) {
                this.surfaceOccupancyPriority = surfaceOccupancyPriority;
                return this;
            }

            public Builder setTargetAspectRatio(int targetAspectRatio) {
                this.targetAspectRatio = targetAspectRatio;
                return this;
            }

            public RecordConfig build() {
                RecordConfig defaults = new RecordConfig();
                defaults.videoFrameRate = this.videoFrameRate;
                defaults.bitRate = this.bitRate;
                defaults.intraFrameInterval = this.intraFrameInterval;
                defaults.audioBitRate = this.audioBitRate;
                defaults.audioSampleRate = this.audioSampleRate;
                defaults.audioChannelCount = this.audioChannelCount;
                defaults.audioRecordSource = this.audioRecordSource;
                defaults.audioMinBufferSize = this.audioMinBufferSize;
                defaults.maxResolution = this.maxResolution;
                defaults.surfaceOccupancyPriority = this.surfaceOccupancyPriority;
                defaults.targetAspectRatio = this.targetAspectRatio;

                return defaults;
            }
        }

    }

    public static final class Metadata {

        @Nullable
        public Location location;
    }

    /**
     * 保存视频的回调保存
     */
    private static final class VideoSavedListenerWrapper implements OnVideoSavedCallback {

        @NonNull
        Executor mExecutor;
        @NonNull
        OnVideoSavedCallback mOnVideoSavedCallback;

        VideoSavedListenerWrapper(@NonNull Executor executor,
                                  @NonNull OnVideoSavedCallback onVideoSavedCallback) {
            mExecutor = executor;
            mOnVideoSavedCallback = onVideoSavedCallback;
        }

        @Override
        public void onVideoSaved(@NonNull OutputFileResults outputFileResults) {
            try {
                mExecutor.execute(() -> mOnVideoSavedCallback.onVideoSaved(outputFileResults));
            } catch (RejectedExecutionException e) {
                Log.e(TAG, "Unable to post to the supplied executor.");
            }
        }

        @Override
        public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {

            try {
                mExecutor.execute(() -> mOnVideoSavedCallback.onError(videoCaptureError, message, cause));

            } catch (RejectedExecutionException e) {
                Log.e(TAG, "Unable to post to the supplied executor.");
            }
        }

    }

    /**
     * 输入文件的结果
     */
    public static class OutputFileResults {
        @Nullable
        private Uri mSavedUri;

        OutputFileResults(@Nullable Uri savedUri) {
            mSavedUri = savedUri;
        }

        @Nullable
        public Uri getSavedUri() {
            return mSavedUri;
        }
    }


    /**
     * 输出的文件选项封装
     */
    public static final class OutputFileOptions {

        private static final Metadata EMPTY_METADATA = new Metadata();

        @Nullable
        private final File mFile;
        @Nullable
        private final FileDescriptor mFileDescriptor;
        @Nullable
        private final ContentResolver mContentResolver;
        @Nullable
        private final Uri mSaveCollection;
        @Nullable
        private final ContentValues mContentValues;
        @Nullable
        private final Metadata mMetadata;

        OutputFileOptions(@Nullable File file,
                          @Nullable FileDescriptor fileDescriptor,
                          @Nullable ContentResolver contentResolver,
                          @Nullable Uri saveCollection,
                          @Nullable ContentValues contentValues,
                          @Nullable Metadata metadata) {
            mFile = file;
            mFileDescriptor = fileDescriptor;
            mContentResolver = contentResolver;
            mSaveCollection = saveCollection;
            mContentValues = contentValues;
            mMetadata = metadata == null ? EMPTY_METADATA : metadata;
        }

        @Nullable
        File getFile() {
            return mFile;
        }

        @Nullable
        FileDescriptor getFileDescriptor() {
            return mFileDescriptor;
        }


        @Nullable
        ContentResolver getContentResolver() {
            return mContentResolver;
        }


        @Nullable
        Uri getSaveCollection() {
            return mSaveCollection;
        }


        @Nullable
        ContentValues getContentValues() {
            return mContentValues;
        }


        @Nullable
        Metadata getMetadata() {
            return mMetadata;
        }


        boolean isSavingToMediaStore() {
            return getSaveCollection() != null && getContentResolver() != null
                    && getContentValues() != null;
        }


        boolean isSavingToFile() {
            return getFile() != null;
        }


        boolean isSavingToFileDescriptor() {
            return getFileDescriptor() != null;
        }


        public static final class Builder {
            @Nullable
            private File mFile;
            @Nullable
            private FileDescriptor mFileDescriptor;
            @Nullable
            private ContentResolver mContentResolver;
            @Nullable
            private Uri mSaveCollection;
            @Nullable
            private ContentValues mContentValues;
            @Nullable
            private Metadata mMetadata;


            public Builder(@NonNull File file) {
                mFile = file;
            }


            public Builder(@NonNull FileDescriptor fileDescriptor) {
                Preconditions.checkArgument(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O,
                        "Using a FileDescriptor to record a video is only supported for Android 8"
                                + ".0 or above.");

                mFileDescriptor = fileDescriptor;
            }

            public Builder(@NonNull ContentResolver contentResolver,
                           @NonNull Uri saveCollection,
                           @NonNull ContentValues contentValues) {
                mContentResolver = contentResolver;
                mSaveCollection = saveCollection;
                mContentValues = contentValues;
            }


            @NonNull
            public OutputFileOptions.Builder setMetadata(@NonNull Metadata metadata) {
                mMetadata = metadata;
                return this;
            }

            @NonNull
            public OutputFileOptions build() {
                return new OutputFileOptions(mFile, mFileDescriptor, mContentResolver,
                        mSaveCollection, mContentValues, mMetadata);
            }
        }
    }
}

