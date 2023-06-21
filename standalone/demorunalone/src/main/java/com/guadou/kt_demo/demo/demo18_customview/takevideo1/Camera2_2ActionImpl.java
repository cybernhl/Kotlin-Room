package com.guadou.kt_demo.demo.demo18_customview.takevideo1;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl3.Camera2RecordProvider;
import com.guadou.kt_demo.demo.demo18_customview.takevideo1.helper.AspectTextureView;


import java.io.File;

/**
 * 使用原生Camera2
 */
public class Camera2_2ActionImpl implements ICameraAction {

    private File mVecordFile = null;  // 输出的文件

    private Context mContext;
    private AspectTextureView mTextureView;
    private Camera2RecordProvider mCamera2Provider;

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
        mTextureView = new AspectTextureView(context);
        mContext = context;

        mTextureView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mCamera2Provider = new Camera2RecordProvider((Activity) mContext, mVecordFile.getAbsolutePath());
        mCamera2Provider.initTexture(mTextureView);

        return mTextureView;

    }

    @Override
    public void initCameraRecord() {
    }

    @Override
    public void startCameraRecord() {
        mCamera2Provider.startRecord();
    }

    @Override
    public void stopCameraRecord(ICameraCallback cameraCallback) {
        mCamera2Provider.stopRecord();
    }

    @Override
    public void releaseCameraRecord() {
    }

    @Override
    public void releaseAllCamera() {
        mCamera2Provider.closeCamera();
    }

    @Override
    public void clearWindow() {
    }

    @Override
    public void isShowCameraView(boolean isVisible) {
        mTextureView.setVisibility(isVisible ? View.VISIBLE : View.GONE);
    }

}
