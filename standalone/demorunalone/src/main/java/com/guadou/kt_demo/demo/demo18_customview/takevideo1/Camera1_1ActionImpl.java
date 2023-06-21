package com.guadou.kt_demo.demo.demo18_customview.takevideo1;


import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.DisplayMetrics;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 使用原生Camera手撕
 */
public class Camera1_1ActionImpl implements ICameraAction {

    private File mVecordFile = null;  // 输出的文件

    private int previewWidth = 0;
    private int previewHeight = 0;

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private Camera mCamera;
    private Camera.Size mBestPreviewSize;
    private MediaRecorder mMediaRecorder;
    private Context mContext;

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
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);  // 视频封装格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);  // 音频格式
        if (mBestPreviewSize != null) {
//            mMediaRecorder.setVideoSize(mBestPreviewSize.width, mBestPreviewSize.height);  // 设置分辨率
            mMediaRecorder.setVideoSize(640, 480);  // 设置分辨率
        }
//        mMediaRecorder.setVideoFrameRate(16); // 比特率
        mMediaRecorder.setVideoEncodingBitRate(1024 * 512);// 设置帧频率，
        mMediaRecorder.setOrientationHint(90);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频输出格式
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());

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
            releaseAllCamera();
        }

        //打开摄像头
        try {
            mCamera = Camera.open();

        } catch (Exception e) {
            e.printStackTrace();
            releaseAllCamera();
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

            DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

            //找到最接近屏幕宽高比的比例
            Camera.Parameters parameters = mCamera.getParameters();

            // 初始化最佳预览尺寸
            mBestPreviewSize = getBestPreviewSize(parameters, screenWidth, screenHeight);

            if (mBestPreviewSize != null) {
                YYLogUtils.w("bestSize- width" + mBestPreviewSize.width + " height:" + mBestPreviewSize.height);
                parameters.setPreviewSize(mBestPreviewSize.width, mBestPreviewSize.height);
            }

            parameters.set("orientation", "portrait");
            mCamera.setParameters(parameters);

        }
    }

    private Camera.Size getBestPreviewSize(Camera.Parameters parameters, int screenWidth, int screenHeight) {
        List<Camera.Size> supportedSizes = parameters.getSupportedPreviewSizes();
        // 假设 screenWidth 和 screenHeight 分别表示屏幕的宽度和高度
        double screenRatio = (double) screenWidth / screenHeight;
        YYLogUtils.w("屏幕的尺寸-screenWidth: " + screenWidth + " screenHeight:" + screenHeight + " screenRatio:" + screenRatio);

        // 初始化最小差值为一个足够大的数
        double minDiff = Double.MAX_VALUE;
        Camera.Size bestSize = null;

        // 遍历 supportedSizes 数组
        for (Camera.Size size : supportedSizes) {

            double ratio = (double) size.width / size.height;
            double diff = Math.abs(ratio - screenRatio);

            YYLogUtils.w("支持的预览宽高，width:" + size.width + " height:" + size.height + " ratio:" + ratio + " 差值：" + diff);

            // 如果差值比当前最小差值还要小，则更新最小差值和 bestSize
            if (diff < minDiff) {
                minDiff = diff;
                bestSize = size;
            }

            double ratio2 = (double) size.height / size.width;
            double diff2 = Math.abs(ratio2 - screenRatio);

            YYLogUtils.w("支持的预览宽高，width:" + size.height + " height:" + size.width + " ratio:" + ratio2 + " 差值：" + diff2);

            // 如果差值比当前最小差值还要小，则更新最小差值和 bestSize
            if (diff2 < minDiff) {
                minDiff = diff2;
                bestSize = size;
            }

        }

        return bestSize;

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
            YYLogUtils.w("surfaceChanged-width: " + width + " height:" + height);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            releaseAllCamera();
        }
    }

}
