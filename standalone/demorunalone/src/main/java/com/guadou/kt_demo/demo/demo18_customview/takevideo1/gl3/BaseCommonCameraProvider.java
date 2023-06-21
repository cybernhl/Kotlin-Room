package com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl3;

import android.app.Activity;
import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.util.Size;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils.BaseMessageLoop;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BaseCommonCameraProvider extends BaseCameraProvider {
    protected Activity mContext;
    protected String mCameraId;
    protected Handler mCameraHandler;
    private final BaseMessageLoop mThread;
    protected CameraDevice mCameraDevice;
    protected CameraCaptureSession session;
    protected AspectTextureView[] mTextureViews;
    protected CameraManager cameraManager;

    protected OnCameraInfoListener mCameraInfoListener;

    public interface OnCameraInfoListener {
        void getBestSize(Size outputSizes);

        void onFrameCannback(Image image);

        void initEncode();

        void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int width, int height);
    }

    public void setCameraInfoListener(OnCameraInfoListener cameraInfoListener) {
        this.mCameraInfoListener = cameraInfoListener;
    }

    protected BaseCommonCameraProvider(Activity mContext) {
        this.mContext = mContext;

        mThread = new BaseMessageLoop(mContext, "camera") {
            @Override
            protected boolean recvHandleMessage(Message msg) {
                return false;
            }
        };
        mThread.Run();

        mCameraHandler = mThread.getHandler();

        cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
    }

    protected String getCameraId(boolean useFront) {
        try {
            for (String cameraId : cameraManager.getCameraIdList()) {
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                int cameraFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (useFront) {
                    if (cameraFacing == CameraCharacteristics.LENS_FACING_FRONT) {
                        return cameraId;
                    }
                } else {
                    if (cameraFacing == CameraCharacteristics.LENS_FACING_BACK) {
                        return cameraId;
                    }
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected Size getCameraBestOutputSizes(String cameraId, Class clz) {
        try {
            //拿到支持的全部Size,并从大到小排序
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            List<Size> sizes = Arrays.asList(configs.getOutputSizes(clz));
            Collections.sort(sizes, new Comparator<Size>() {
                @Override
                public int compare(Size o1, Size o2) {
                    return o1.getWidth() * o1.getHeight() - o2.getWidth() * o2.getHeight();
                }
            });
            Collections.reverse(sizes);
            YYLogUtils.w("all_sizes:" + sizes);

            //去除一些不合适的预览尺寸
            List<Size> suitableSizes = new ArrayList();
            for (int i = 0; i < sizes.size(); i++) {
                Size option = sizes.get(i);
                if (textureViewSize.getWidth() > textureViewSize.getHeight()) {
                    if (option.getWidth() >= textureViewSize.getWidth() && option.getHeight() >= textureViewSize.getHeight()) {
                        suitableSizes.add(option);
                    }
                } else {
                    if (option.getWidth() >= textureViewSize.getHeight() && option.getHeight() >= textureViewSize.getWidth()) {
                        suitableSizes.add(option);
                    }
                }
            }

            YYLogUtils.w("suitableSizes:" + suitableSizes);

            //获取最小占用的Size
            if (!suitableSizes.isEmpty()) {
                return suitableSizes.get(suitableSizes.size() - 1);
            } else {
                //异常情况下只能找默认的了
                return sizes.get(0);
            }

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected List<Size> getCameraAllSizes(String cameraId, int format) {
        try {
            CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap configs = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            return Arrays.asList(configs.getOutputSizes(format));
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void releaseCameraDevice(CameraDevice cameraDevice) {
        if (cameraDevice != null) {
            cameraDevice.close();
            cameraDevice = null;
        }
    }

    protected void releaseCameraSession(CameraCaptureSession session) {
        if (session != null) {
            session.close();
            session = null;
        }
    }

    protected void releaseCamera() {
        releaseCameraDevice(mCameraDevice);
        releaseCameraSession(session);
    }
}