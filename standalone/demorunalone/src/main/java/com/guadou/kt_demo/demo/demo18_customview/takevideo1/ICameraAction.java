package com.guadou.kt_demo.demo.demo18_customview.takevideo1;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

import java.io.File;

/**
 * @auther Newki
 * @date 2023/5/18
 * @description XX
 */
interface ICameraAction {

    void setupCustomParams(int width ,int height  ,int recordMaxTime);

    void setOutFile(File file);

    File getOutFile();

    View initCamera(Context context);

    void initCameraRecord();

    void startCameraRecord();

    void stopCameraRecord();

    void releaseCameraRecord();

    void releaseCamera();

    void clearWindow();

    void isShowCameraView(boolean isVisible);
}
