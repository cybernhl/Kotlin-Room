package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio_video_surface;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.os.Build;
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.BaseCommonCameraProvider;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager.Camera2SimpleInterface;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectInterface;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 在预览的基础上加入ImageReader的帧回调，可以用于编码H264视频流
 */
public class Camera2SurfaceProvider extends BaseCommonCameraProvider {

    private CaptureRequest.Builder mPreviewBuilder;
    private Size outputSize;
    public VideoCaptureUtils videoCaptureUtils;

    public Camera2SurfaceProvider(Activity mContext) {
        super(mContext);
        Point displaySize = new Point();
        mContext.getWindowManager().getDefaultDisplay().getSize(displaySize);
        screenSize = new Size(displaySize.x, displaySize.y);
        YYLogUtils.w("screenSize,width:" + screenSize.getWidth() + "height:" + screenSize.getHeight(), "Camera2ImageReaderProvider");
    }

    private void initCamera() {
        mCameraId = getCameraId(false); //默认使用后置相机
        //获取指定相机的输出尺寸列表，降序排序
        outputSize = getCameraBestOutputSizes(mCameraId, SurfaceTexture.class);
        //初始化预览尺寸
        previewSize = outputSize;
        YYLogUtils.w("previewSize,width:" + previewSize.getWidth() + "height:" + previewSize.getHeight(), "Camera2ImageReaderProvider");

        if (mCameraInfoListener != null) {
            mCameraInfoListener.getBestSize(outputSize);

            //初始化录制工具类
            VideoCaptureUtils.RecordConfig recordConfig = new VideoCaptureUtils.RecordConfig.Builder().build();

            //Surface 录制工具类
            videoCaptureUtils = new VideoCaptureUtils(recordConfig, outputSize);
//            videoCaptureUtils = new VideoCaptureUtils(recordConfig, new Size(outputSize.getHeight(), outputSize.getWidth()));
        }
    }

    int index = 0;

    /**
     * 关联并初始化TextTure
     */
    public void initTexture(AspectTextureView... textureViews) {

        mTextureViews = textureViews;
        int size = textureViews.length;
        for (AspectTextureView aspectTextureView : textureViews) {
            aspectTextureView.setSurfaceTextureListener(new Camera2SimpleInterface.SimpleSurfaceTextureListener() {
                @Override
                public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height) {
                    textureViewSize = new Size(width, height);
                    YYLogUtils.w("textureViewSize,width:" + textureViewSize.getWidth() + "height:" + textureViewSize.getHeight(), "Camera2ImageReaderProvider");

                    if (mCameraInfoListener != null) {
                        mCameraInfoListener.onSurfaceTextureAvailable(surfaceTexture, width, height);
                    }

                    if (++index == size) {
                        initCamera();
                        openCamera();
                    }
                }
            });
        }
    }

    //初始化编码格式
    public void initEncord() {
        if (mCameraInfoListener != null) {
            mCameraInfoListener.initEncode();
        }
    }

    @SuppressLint("MissingPermission")
    private void openCamera() {
        try {

            cameraManager.openCamera(mCameraId, mStateCallback, mCameraHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private final CameraDevice.StateCallback mStateCallback = new Camera2SimpleInterface.SimpleCameraDeviceStateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            mCameraDevice = camera;
            initEncord();

            MediaCodecList allMediaCodecLists = new MediaCodecList(-1);
            MediaCodecInfo avcCodecInfo = null;
            for (MediaCodecInfo mediaCodecInfo : allMediaCodecLists.getCodecInfos()) {
                if (mediaCodecInfo.isEncoder()) {
                    String[] supportTypes = mediaCodecInfo.getSupportedTypes();
                    for (String supportType : supportTypes) {
                        if (supportType.equals(MediaFormat.MIMETYPE_VIDEO_AVC)) {
                            avcCodecInfo = mediaCodecInfo;
                            Log.d("TAG", "编码器名称:" + mediaCodecInfo.getName() + "  " + supportType);
                            MediaCodecInfo.CodecCapabilities codecCapabilities = avcCodecInfo.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_AVC);
                            int[] colorFormats = codecCapabilities.colorFormats;
                            for (int colorFormat : colorFormats) {
                                switch (colorFormat) {
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV411Planar:
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV411PackedPlanar:
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                                    case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
                                        Log.d("TAG", "支持的格式::" + colorFormat);
                                        break;
                                }
                            }
                        }
                    }
                }
            }

            //根据什么Size来展示PreView
            startPreviewSession(previewSize);
        }
    };

    public void startPreviewSession(Size size) {
        YYLogUtils.w("真正的预览Size,width:" + size.getWidth() + " height:" + size.getHeight(), "Camera2ImageReaderProvider");

        try {

            releaseCameraSession(session);
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            List<Surface> outputs = new ArrayList<>();
            for (AspectTextureView aspectTextureView : mTextureViews) {
                //设置预览大小与展示的裁剪模式
                aspectTextureView.setScaleType(AspectInterface.ScaleType.CENTER_CROP);
                aspectTextureView.setSize(size.getHeight(), size.getWidth());

                SurfaceTexture surfaceTexture = aspectTextureView.getSurfaceTexture();
                surfaceTexture.setDefaultBufferSize(size.getWidth(), size.getHeight());

                //添加预览的TextureView
                Surface previewSurface = new Surface(surfaceTexture);
                mPreviewBuilder.addTarget(previewSurface);
                outputs.add(previewSurface);

            }

            //这里设置输入Surface编码的数据源
            //使用 mVideoEncoder.createInputSurface() 的方式创建的Surface，就是预览与录制各玩各的效果不相关，
            Surface inputSurface = videoCaptureUtils.mCameraSurface;
            mPreviewBuilder.addTarget(inputSurface);
            outputs.add(inputSurface);

            mCameraDevice.createCaptureSession(outputs, mStateCallBack, mCameraHandler);

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

    }

    private ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            Image image = reader.acquireLatestImage();
            if (image == null) {
                return;
            }

            if (mCameraInfoListener != null) {
                mCameraInfoListener.onFrameCannback(image);
            }

            image.close();
        }
    };

    private final CameraCaptureSession.StateCallback mStateCallBack = new Camera2SimpleInterface.SimpleStateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {

                Camera2SurfaceProvider.this.session = session;

                //设置拍照前持续自动对焦
                mPreviewBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);

                CaptureRequest request = mPreviewBuilder.build();
                session.setRepeatingRequest(request, null, mCameraHandler);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }
    };

    public void closeCamera() {
        releaseCamera();

        if (videoCaptureUtils != null) {
            videoCaptureUtils.release();
        }
        if (mCameraDevice != null) {
            mCameraDevice.close();
        }
    }

}
