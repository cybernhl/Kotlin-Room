package com.guadou.kt_demo.demo.demo18_customview.takevideo1;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AutoFitTextureView;
import com.guadou.lib_baselib.ext.ToastUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * 使用原生Camera2
 */
public class Camera2_1ActionImpl implements ICameraAction {

    private File mVecordFile = null;  // 输出的文件

    private Context mContext;
    private TextureView mTextureView;

    private CameraDevice mCameraDevice;
    private ImageReader mImageReader;
    private CameraManager mCameraManager;
    private Handler mBgHandler;
    private String mRearCameraID, mFrontCameraID, mCurrentCameraId;
    private CameraCharacteristics mRearCameraCs, mFrontCameraCs;
    private Size previewSize;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CameraCaptureSession mPreviewSession;
    private CaptureRequest mPreviewRequest;

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
        mTextureView = new TextureView(context);
        mContext = context;
        mTextureView.setLayoutParams(new ViewGroup.LayoutParams(720, 1280));
        mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);

        return mTextureView;
    }

    @Override
    public void initCameraRecord() {
        // 录制的初始化
    }

    @Override
    public void startCameraRecord() {
    }

    @Override
    public void stopCameraRecord(ICameraCallback cameraCallback) {
    }

    @Override
    public void releaseCameraRecord() {
    }

    @Override
    public void releaseAllCamera() {
    }

    @Override
    public void clearWindow() {
    }

    @Override
    public void isShowCameraView(boolean isVisible) {
        mTextureView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

    private void initCameraAndRecord() {

    }

    //设置垂直方向
    private void setCameraParams() {

    }

    private TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            // 当TextureView可用时，打开摄像头
            YYLogUtils.w("当TextureView可用时,width:" + width + " height:" + height);

            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
            // 当TextureView尺寸改变时，更新预览尺寸
//            configureTransform(width, height);
        }


        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            // 当TextureView销毁时，释放资源
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
            // 监听纹理更新事件
        }
    };


    @SuppressLint("MissingPermission")
    private void openCamera(int width, int height) {
        // 获取相机管理器
        mCameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);

        // 设置自定义的线程处理
        HandlerThread handlerThread = new HandlerThread("Camera2Manager");
        handlerThread.start();
        mBgHandler = new Handler(handlerThread.getLooper());
        
        try {

            //获取到相机信息并赋值
            getCameraListCameraCharacteristics(width,height);


            YYLogUtils.w("打开的摄像头id:" + mCurrentCameraId);

            // 打开摄像头
            mCameraManager.openCamera(mCurrentCameraId, new CameraDevice.StateCallback() {
                @Override
                public void onOpened(CameraDevice cameraDevice) {
                    // 当摄像头打开时，创建预览会话
                    mCameraDevice = cameraDevice;
                    createCameraPreviewSession(mBgHandler, mTextureView.getWidth(), mTextureView.getHeight());
                }

                @Override
                public void onDisconnected(CameraDevice cameraDevice) {
                    if (mCameraDevice != null) {
                        mCameraDevice.close();
                        mCameraDevice = null;
                    }
                }

                @Override
                public void onError(CameraDevice cameraDevice, int error) {
                    if (mCameraDevice != null) {
                        mCameraDevice.close();
                        mCameraDevice = null;
                    }
                }

            }, mBgHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private void getCameraListCameraCharacteristics(int width, int height) {
        try {
            //获取当前手机的所有摄像头id
            String[] cameraList = this.mCameraManager.getCameraIdList();
            for (String id : cameraList) {
                //通过id获取对应相机信息结构
                CameraCharacteristics ccs = this.mCameraManager.getCameraCharacteristics(id);
                //获取前置/后置摄像头
                Integer facing = ccs.get(CameraCharacteristics.LENS_FACING);
                if (facing != null) {
                    if (facing == CameraCharacteristics.LENS_FACING_BACK) {
                        //后置
                        this.mRearCameraCs = ccs;
                        this.mRearCameraID = id;
                        //设置默认后置
                        this.mCurrentCameraId = this.mRearCameraID;
                    } else if (facing == CameraCharacteristics.LENS_FACING_FRONT) {
                        //前置
                        this.mFrontCameraCs = ccs;
                        this.mFrontCameraID = id;
                    }
                }
            }


//            //判断是前置or后置拿到对应的相机信息
//            CameraCharacteristics tmpCss = this.mCurrentCameraId.equals(this.mRearCameraID) ? this.mRearCameraCs : this.mFrontCameraCs;
//            //获取摄像头支持的所有输出格式和尺寸
//            StreamConfigurationMap map = tmpCss.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//            if (map != null) {
//                //根据SurfaceTexture获取输出大小尺寸列表
//                Size[] sizes = map.getOutputSizes(SurfaceTexture.class);
//                //寻找最佳尺寸
//                this.previewSize = findSuitablePreviewSize(width,height,Arrays.asList(sizes));
//                YYLogUtils.w("previewSize,width:" + previewSize.getWidth() + " height:" + previewSize.getHeight());
//            }


        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private Size findSuitablePreviewSize(int width, int height, List<Size> sizes) {
        // 获取当前设备的屏幕大小
        int screenWidth = width;
        int screenHeight = height;

        // 计算与屏幕宽高比最接近的相机预览尺寸
        float screenAspectRatio = (float) screenHeight / screenWidth;
        float closestAspectRatio = Float.MAX_VALUE;

        Size bestSize = null;
        for (Size size : sizes) {
            float aspectRatio = (float) size.getWidth() / size.getHeight();
            if (Math.abs(screenAspectRatio - aspectRatio) < Math.abs(closestAspectRatio - screenAspectRatio)) {
                closestAspectRatio = aspectRatio;
                bestSize = size;
            }
        }

        return bestSize;
    }

    /**
     * 创建相机预览会话
     */
    private void createCameraPreviewSession(Handler handler, int width, int height) {
        try {
            // 获取SurfaceTexture并设置默认缓冲区大小
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            texture.setDefaultBufferSize(mTextureView.getWidth(), mTextureView.getHeight());

            // 创建预览Surface
            Surface surface = new Surface(texture);

            // 创建CaptureRequest.Builder并设置预览Surface为目标
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);

            // 创建ImageReader并设置回调
            YYLogUtils.w("创建ImageReader并设置回调,width:" + width + " height:" + height);
//            mImageReader = ImageReader.newInstance(mTextureView.getWidth(), mTextureView.getHeight(), ImageFormat.JPEG, 1);
            mImageReader = ImageReader.newInstance(width, height, ImageFormat.YUV_420_888, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, handler);

            // 将ImageReader的Surface添加到CaptureRequest.Builder中
            Surface readerSurface = mImageReader.getSurface();
            mPreviewRequestBuilder.addTarget(readerSurface);

            // 创建预览会话
            mCameraDevice.createCaptureSession(Arrays.asList(surface, readerSurface), mSessionCallback, handler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    private CameraCaptureSession.StateCallback mSessionCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
            // 预览会话已创建成功，开始预览
            mPreviewSession = cameraCaptureSession;
            updatePreview();
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
            ToastUtils.INSTANCE.makeText(mContext, "Failed to create camera preview session");
        }
    };

    private void updatePreview() {
        try {
            // 设置自动对焦模式
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

            // 构建预览请求
            mPreviewRequest = mPreviewRequestBuilder.build();

            // 发送预览请求
            mPreviewSession.setRepeatingRequest(mPreviewRequest, null, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {
            // 当有图像可用时，保存图像并显示
            Image image = reader.acquireLatestImage();
//            ByteBuffer buffer = image.getPlanes()[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            saveImage(bytes);
            image.close();
        }
    };

    private void saveImage(byte[] bytes) {
        // TODO: 将图像保存到文件中
    }

//    private void configureTransform(int viewWidth, int viewHeight) {
//
//        if (null == mTextureView || null == mImageReader) {
//            return;
//        }
//
//        // 获取摄像头传感器方向
//        if (mContext instanceof Activity) {
//            Activity activity = (Activity) mContext;
//
//            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
//            CameraCharacteristics characteristics = null;
//
//            try {
//                CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
//                String cameraId = mCameraDevice.getId();
//                characteristics = manager.getCameraCharacteristics(cameraId);
//
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//
//            int sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
//            boolean swappedDimensions = false;
//            switch (rotation) {
//                case Surface.ROTATION_0:
//                case Surface.ROTATION_180:
//                    if (sensorOrientation == 90 || sensorOrientation == 270) {
//                        swappedDimensions = true;
//                    }
//                    break;
//                case Surface.ROTATION_90:
//                case Surface.ROTATION_270:
//                    if (sensorOrientation == 0 || sensorOrientation == 180) {
//                        swappedDimensions = true;
//                    }
//                    break;
//                default:
//                    YYLogUtils.e("Invalid rotation degree: " + rotation);
//            }
//
//            // 计算预览尺寸和TextureView尺寸的比例
//            Point displaySize = new Point();
//            activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
//            int previewWidth = viewWidth;
//            int previewHeight = viewHeight;
//            if (swappedDimensions) {
//                previewWidth = viewHeight;
//                previewHeight = viewWidth;
//            }
//            float ratio = Math.min((float) displaySize.x / previewWidth, (float) displaySize.y / previewHeight);
//
//            YYLogUtils.w("sensorOrientation:" + sensorOrientation);
//
//            // 计算缩放变换矩阵
//            Matrix matrix = new Matrix();
//            matrix.postRotate((float) (360 - sensorOrientation));
//            matrix.postScale(ratio, ratio, displaySize.x / 2, displaySize.y / 2);
//
//            // 应用变换矩阵
//            mTextureView.setTransform(matrix);
//        }
//    }

}
