package com.guadou.kt_demo.demo.demo18_customview.takevideo1;


import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.CameraHelper;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.CameraListener;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 使用工具类Hepler的方式
 */
public class Camera1_2ActionImpl implements ICameraAction {

    private File mVecordFile = null;  // 输出的文件
    private MediaRecorder mMediaRecorder;
    private Context mContext;
    private SurfaceView mSurfaceView;
    private CameraHelper cameraHelper;

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
        mSurfaceView = new SurfaceView(context);
        mContext = context;
        mSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


        mSurfaceView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mSurfaceView.post(() -> {

                    setupCameraHelper();
                });

                mSurfaceView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        return mSurfaceView;
    }

    private void setupCameraHelper() {
        cameraHelper = new CameraHelper.Builder()
                .previewViewSize(new Point(mSurfaceView.getMeasuredWidth(), mSurfaceView.getMeasuredHeight()))
                .rotation(((Activity) mContext).getWindowManager().getDefaultDisplay().getRotation())
                .specificCameraId(Camera.CameraInfo.CAMERA_FACING_BACK)
                .isMirror(false)
                .previewOn(mSurfaceView) //预览容器 推荐TextureView
                .cameraListener(mCameraListener) //设置自定义的监听器
                .build();

        cameraHelper.start();
    }

    //自定义监听
    private CameraListener mCameraListener = new CameraListener() {
        @Override
        public void onCameraOpened(Camera camera, int cameraId, int displayOrientation, boolean isMirror) {
            YYLogUtils.w("CameraListener - onCameraOpened");

            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.reset();
            if (camera != null) {
                mMediaRecorder.setCamera(camera);
            }
            mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
                @Override
                public void onError(MediaRecorder mr, int what, int extra) {
                    if (mr != null) {
                        mr.reset();
                    }
                }
            });
            mMediaRecorder.setPreviewDisplay(mSurfaceView.getHolder().getSurface());
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 音频源
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 视频封装格式
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  // 音频格式

//            mMediaRecorder.setVideoSize(mBestPreviewSize.width, mBestPreviewSize.height);  // 设置分辨率
            mMediaRecorder.setVideoSize(640, 480);  // 设置分辨率
//        mMediaRecorder.setVideoFrameRate(16); // 比特率
            mMediaRecorder.setVideoEncodingBitRate(1024 * 512);// 设置帧频率，
            mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频输出格式
            mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());

        }

        @Override
        public void onPreview(byte[] data, Camera camera) {

        }

        @Override
        public void onCameraClosed() {
            YYLogUtils.w("CameraListener - onCameraClosed");
        }

        @Override
        public void onCameraError(Exception e) {
            YYLogUtils.w("CameraListener - onCameraError");
        }

        @Override
        public void onCameraConfigurationChanged(int cameraID, int displayOrientation) {
            YYLogUtils.w("CameraListener - onCameraConfigurationChanged");
        }
    };

    @Override
    public void initCameraRecord() {
    }

    @Override
    public void startCameraRecord() {

        if (mMediaRecorder == null) return;

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stopCameraRecord(ICameraCallback cameraCallback) {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
                cameraCallback.takeSuccess();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void releaseCameraRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }

    @Override
    public void releaseAllCamera() {

    }

    @Override
    public void clearWindow() {
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void isShowCameraView(boolean isVisible) {
        mSurfaceView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
