package com.newki.glrecord.camera1;

import android.graphics.Point;
import android.graphics.SurfaceTexture;

/**
 * 使用 Camera 的动作接口封装
 */
public interface ICamera {

    void open(int cameraId);

    void setPreviewTexture(SurfaceTexture texture);

    void setConfig(Config config);

    void setOnPreviewFrameCallback(PreviewFrameCallback callback);

    void preview();

    Point getPreviewSize();

    Point getPictureSize();

    boolean close();

    class Config {
        public float rate = 1.778f;
        public int minPreviewWidth;
        public int minPictureWidth;
    }

    interface PreviewFrameCallback {
        void onPreviewFrame(byte[] bytes, int width, int height);
    }
}
