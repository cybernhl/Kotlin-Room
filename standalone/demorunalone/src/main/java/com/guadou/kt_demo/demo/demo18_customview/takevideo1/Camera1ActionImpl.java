package com.guadou.kt_demo.demo.demo18_customview.takevideo1;


import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Camera1ActionImpl implements ICameraAction {

    private int mWidth; // 视频分辨率宽度
    private int mHeight; // 视频分辨率高度
    private int mRecordMaxTime;  // 一次拍摄最长时间
    private File mVecordFile = null;  // 输出的文件

    private int previewWidth = 0;
    private int previewHeight = 0;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private MediaRecorder mMediaRecorder;

    @Override
    public void setupCustomParams(int width, int height, int recordMaxTime) {

        this.mWidth = width;
        this.mHeight = height;
        this.mRecordMaxTime = recordMaxTime;
    }

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
        mSurfaceView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        return mSurfaceView;
    }


    @Override
    public void initCameraRecord() {
        // 录制的初始化
    }

    @Override
    public void startCameraRecord() {

//        if(mCamera == null) return;
//        Camera.Parameters params = mCamera.getParameters();
//        Camera.Size previewSize = params.getPreviewSize();
//        int preWidth = previewSize.width;
//        int preHeight = previewSize.height;
//        YYLogUtils.w("preWidth:"+preWidth +" preHeight:"+preHeight);

        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null) {
            mMediaRecorder.setCamera(mCamera);
        }
        mMediaRecorder.setOnErrorListener(new MediaRecorder.OnErrorListener() {
            @Override
            public void onError(MediaRecorder mr, int what, int extra) {
                if (mr != null) {
                    mr.reset();
                }
            }
        });
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA); // 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  // 音频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 视频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  // 音频格式
        mMediaRecorder.setVideoSize(mWidth, mHeight);  // 设置分辨率
//        mMediaRecorder.setVideoFrameRate(16); // 比特率
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 512);// 设置帧频率，
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());

        try {
            mMediaRecorder.prepare();
            mMediaRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void stopCameraRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
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
    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.lock();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void clearWindow() {
        mSurfaceView.clearFocus();
        initCameraAndRecord();
        mSurfaceView.setVisibility(View.VISIBLE);
    }

    @Override
    public void isShowCameraView(boolean isVisible) {
        mSurfaceView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void initCameraAndRecord() {
        if (mCamera != null) {
            releaseCamera();
        }

        //打开摄像头
        try {
            mCamera = Camera.open();

            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            for (int i = 0; i < sizes.size(); i++) {
                Camera.Size size = sizes.get(i);
                YYLogUtils.w("支持的Size-width:" + size.width + " height:" + size.height);
            }

        } catch (Exception e) {
            e.printStackTrace();
            releaseCamera();
        }
        if (mCamera == null)
            return;

        //设置摄像头参数
        setCameraParams();

        try {
            mCamera.setDisplayOrientation(90);   //设置拍摄方向为90度（竖屏）
            mCamera.setPreviewDisplay(mSurfaceHolder);
            mCamera.startPreview();
            mCamera.unlock();

            //摄像头参数设置完成之后，初始化录制API配置
            initCameraRecord();

        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //设置垂直方向
    private void setCameraParams() {
        if (mCamera != null) {
            Camera.Parameters params = mCamera.getParameters();
            params.set("orientation", "portrait");
            mCamera.setParameters(params);
        }
    }

    /* =========================== SurfaceView的Callback ===================================**/

    /**
     * SurfaceView的回调中创建surface的时候初始化摄像头操作
     * 销毁surface的时候释放资源
     */
    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            initCameraAndRecord();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseCamera();
        }
    }

}
