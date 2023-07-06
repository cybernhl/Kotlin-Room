package com.guadou.kt_demo.demo.demo18_customview.takevideo1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureResult;
import android.media.Image;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.renderscript.Allocation;
import android.renderscript.Matrix4f;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicColorMatrix;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.util.Size;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraInfoUnavailableException;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.core.VideoCapture;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.common.util.concurrent.ListenableFuture;
import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.ScreenUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.ByteBuffer;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 手撕 CameraX
 */
@SuppressLint("RestrictedApi")
public class CameraX1ActionImpl implements ICameraAction {

    private File mVecordFile = null;  // 输出的文件
    private Context mContext;
    private PreviewView mPreviewView;
    private VideoCapture mVideoCapture;  //录制视频类型
    private ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    private CameraSelector mCameraSelector;   //镜头选择

    private static final double RATIO_4_3_VALUE = 4.0 / 3.0;
    private static final double RATIO_16_9_VALUE = 16.0 / 9.0;
    private ICameraCallback mCameraCallback;
    private ProcessCameraProvider mCameraProvider;
    private int mLensFacing;

    @Override
    public void setOutFile(File file) {
        mVecordFile = file;
    }

    @Override
    public File getOutFile() {
        return mVecordFile;
    }

    @Override
    public View initCamera(Context context) {
        mPreviewView = new PreviewView(context);
        mContext = context;
        mPreviewView.setScaleType(PreviewView.ScaleType.FIT_CENTER);
        mPreviewView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mPreviewView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (mPreviewView.isShown()) {
                    startCamera();
                }
                mPreviewView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        return mPreviewView;
    }

    private void startCamera() {

        //获取屏幕的分辨率
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mPreviewView.getDisplay().getRealMetrics(displayMetrics);
        //获取宽高比
        int screenAspectRatio = aspectRatio(displayMetrics.widthPixels, displayMetrics.heightPixels);

        int rotation = mPreviewView.getDisplay().getRotation();

        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(mContext);
        cameraProviderFuture.addListener(() -> {
            try {
                //获取相机信息
                mCameraProvider = cameraProviderFuture.get();

                //镜头选择
                mLensFacing = getLensFacing();
                mCameraSelector = new CameraSelector.Builder().requireLensFacing(mLensFacing).build();

                //预览对象
                Preview.Builder previewBuilder = new Preview.Builder()
                        .setTargetAspectRatio(screenAspectRatio)
                        .setTargetRotation(rotation);
                Preview preview = previewBuilder.build();

                preview.setSurfaceProvider(mPreviewView.getSurfaceProvider());

                //录制视频对象
                mVideoCapture = new VideoCapture.Builder()
                        .setTargetAspectRatio(screenAspectRatio)  //设置高宽比
                        .setAudioRecordSource(MediaRecorder.AudioSource.MIC) //设置音频源麦克风
                        .setTargetRotation(rotation)
                        //视频帧率
                        .setVideoFrameRate(30)
                        //bit率
                        .setBitRate(3 * 1024 * 1024)
                        .build();

//                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
//                        .setTargetAspectRatio(screenAspectRatio)
//                        .setTargetRotation(rotation)
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build();
//
//                // 在每一帧上应用颜色矩阵
//                imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), new MyAnalyzer(mContext));

                //开启CameraX
                mCameraProvider.unbindAll();

                if (mContext instanceof FragmentActivity) {
                    FragmentActivity fragmentActivity = (FragmentActivity) mContext;
                    mCameraProvider.bindToLifecycle(fragmentActivity, mCameraSelector, preview, mVideoCapture/*,imageAnalysis*/);
                }


            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        }, ContextCompat.getMainExecutor(mContext));

    }

    private int aspectRatio(int widthPixels, int heightPixels) {
        double previewRatio = (double) Math.max(widthPixels, heightPixels) / (double) Math.min(widthPixels, heightPixels);
        if (Math.abs(previewRatio - RATIO_4_3_VALUE) <= Math.abs(previewRatio - RATIO_16_9_VALUE)) {
            return AspectRatio.RATIO_4_3;
        }
        return AspectRatio.RATIO_16_9;
    }

    @Override
    public void initCameraRecord() {
    }

    @Override
    public void startCameraRecord() {
        if (mVideoCapture == null) return;

        VideoCapture.OutputFileOptions outputFileOptions = new VideoCapture.OutputFileOptions.Builder(getOutFile()).build();

        mVideoCapture.startRecording(outputFileOptions, mExecutorService, new VideoCapture.OnVideoSavedCallback() {
            @Override
            public void onVideoSaved(@NonNull VideoCapture.OutputFileResults outputFileResults) {

                YYLogUtils.w("视频保存成功,outputFileResults:" + outputFileResults.getSavedUri());

                if (mCameraCallback != null) mCameraCallback.takeSuccess();
            }

            @Override
            public void onError(int videoCaptureError, @NonNull String message, @Nullable Throwable cause) {
                YYLogUtils.e(message);
            }
        });
    }

    @Override
    public void stopCameraRecord(ICameraCallback cameraCallback) {
        mCameraCallback = cameraCallback;
        if (mVideoCapture != null) {
            mVideoCapture.stopRecording();
        }
    }

    @Override
    public void releaseCameraRecord() {
//        mExecutorService.shutdown();
    }

    @Override
    public void releaseAllCamera() {

    }

    @Override
    public void clearWindow() {
    }

    @Override
    public void isShowCameraView(boolean isVisible) {
        mPreviewView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    //是否有后摄像头
    private boolean hasBackCamera() {
        if (mCameraProvider == null) {
            return false;
        }
        try {
            return mCameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }
        return false;
    }

    //是否有前摄像头
    private boolean hasFrontCamera() {
        if (mCameraProvider == null) {
            return false;
        }
        try {
            return mCameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA);
        } catch (CameraInfoUnavailableException e) {
            e.printStackTrace();
        }
        return false;
    }

    //选择镜头
    private int getLensFacing() {
        if (hasBackCamera()) {
            return CameraSelector.LENS_FACING_BACK;
        }
        if (hasFrontCamera()) {
            return CameraSelector.LENS_FACING_FRONT;
        }
        return -1;
    }

}
