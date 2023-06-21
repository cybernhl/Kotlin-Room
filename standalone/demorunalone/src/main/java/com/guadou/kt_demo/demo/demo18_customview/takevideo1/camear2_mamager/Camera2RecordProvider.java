package com.guadou.kt_demo.demo.demo18_customview.takevideo1.camear2_mamager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.ImageFormat;
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
import android.util.Log;
import android.util.Size;
import android.view.Surface;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectInterface;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils.Mp4Writer;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * 在All Size的基础上提供《视频录制》的逻辑
 */
public class Camera2RecordProvider extends BaseCommonCameraProvider {

    private CaptureRequest.Builder mPreviewBuilder;
    protected ImageReader mImageReader;
    private Size outputSize;

    private Mp4Writer mp4Writer;
    private String outMp4Path;

    public Camera2RecordProvider(Activity mContext, String outMp4Path) {
        super(mContext);
        Point displaySize = new Point();
        mContext.getWindowManager().getDefaultDisplay().getSize(displaySize);
        screenSize = new Size(displaySize.x, displaySize.y);

        this.outMp4Path = outMp4Path;
    }

    private void initCamera() {
        mCameraId = getCameraId(false);//默认使用后置相机
        //获取指定相机的输出尺寸列表，降序排序
        outputSize = getCameraBestOutputSizes(mCameraId, SurfaceTexture.class);
        //初始化预览尺寸
        previewSize = outputSize;

        YYLogUtils.w("previewSize,width:" + previewSize.getWidth() + "height:" + previewSize.getHeight());

        if (mCameraInfoListener != null) {
            mCameraInfoListener.getBestSize(outputSize);
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

    //初始化编码格式
    public void initEncord() {
        if (mCameraInfoListener != null) {
            mCameraInfoListener.initEncode();
        }
    }

    public void startPreviewSession(Size size) {

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
                Surface previewSurface = new Surface(surfaceTexture);
                mPreviewBuilder.addTarget(previewSurface);
                outputs.add(previewSurface);
            }

            mImageReader = ImageReader.newInstance(size.getWidth(), size.getHeight(), ImageFormat.YUV_420_888, 2);
            mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mCameraHandler);
            Surface readerSurface = mImageReader.getSurface();
            mPreviewBuilder.addTarget(readerSurface);
            outputs.add(readerSurface);

            mCameraDevice.createCaptureSession(outputs, mStateCallBack, mCameraHandler);

            //初始化MP4录制工具类
            mp4Writer = new Mp4Writer(mContext, previewSize.getWidth(), previewSize.getHeight(), 30, outMp4Path);

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


            image.close();
        }
    };

    public byte[] imageToYUV420(Image image) {
        Image.Plane[] planes = image.getPlanes();
        int pixelStride = planes[1].getPixelStride();
        int rowStride = planes[1].getRowStride();
        Size previewSize = new Size(image.getWidth(), image.getHeight());
        int bufferSize = rowStride * (previewSize.getHeight() + previewSize.getHeight() / 2);
        byte[] data = new byte[bufferSize];
        ByteBuffer bufferY = planes[0].getBuffer();
        ByteBuffer bufferU = planes[1].getBuffer();
        ByteBuffer bufferV = planes[2].getBuffer();
        int rowY = bufferY.remaining() / planes[0].getRowStride();
        if (pixelStride == 1 && rowStride == previewSize.getWidth()) { // YUV420P
            bufferY.get(data, 0, bufferY.remaining());
            bufferU.get(data, bufferY.remaining(), bufferU.remaining());
            bufferV.get(data, bufferY.remaining() + bufferU.remaining(), bufferV.remaining());
        } else { // YUV420SP
            int yOffset = 0;
            int uOffset = bufferSize / 4;
            int vOffset = bufferSize / 4 + bufferSize / 8;
            for (int i = 0; i < previewSize.getHeight(); i++) {
                bufferY.get(data, yOffset, previewSize.getWidth());
                if (i % 2 == 0) { // U、V分量数据交错存储
                    bufferV.get(data, uOffset, pixelStride);
                    bufferU.get(data, vOffset, pixelStride);
                    uOffset += pixelStride;
                    vOffset += pixelStride;
                }
                yOffset += rowStride;
            }
        }
        return data;
    }


    private final CameraCaptureSession.StateCallback mStateCallBack = new Camera2SimpleInterface.SimpleStateCallback() {
        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {

                Camera2RecordProvider.this.session = session;
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
        if (mImageReader != null) {
            mImageReader.close();
        }
        if (mp4Writer != null) {
            mp4Writer.endWrite();
        }
    }


    // 获取视频录制对象
    public Mp4Writer getMp4Writer() {
        return mp4Writer;
    }

    public void startRecord() {
        if (mp4Writer != null) {
            mp4Writer.startWrite();
        }
    }

    public void stopRecord() {
        if (mp4Writer != null) {
            mp4Writer.endWrite();
        }
    }
}
