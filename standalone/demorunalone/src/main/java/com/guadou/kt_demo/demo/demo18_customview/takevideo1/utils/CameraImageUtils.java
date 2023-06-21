package com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils;

/**
 * Camera 返回的Image对象工具类，用于转换输出格式
 */
public class CameraImageUtils {

    /**
     * 将YUV420格式的数据转换为I420格式的数据
     *
     * @param yuv420 YUV420格式的数据
     * @param width  图像宽度
     * @param height 图像高度
     * @return I420格式的数据
     */
    public static byte[] convertYUV420ToI420(byte[] yuv420, int width, int height) {
        int size = width * height;
        byte[] i420 = new byte[size * 3 / 2];
        System.arraycopy(yuv420, 0, i420, 0, size); // 复制Y分量数据
        System.arraycopy(yuv420, size + size / 4, i420, size, size / 4); // 复制U分量数据
        System.arraycopy(yuv420, size, i420, size + size / 4, size / 4); // 复制V分量数据
        return i420;
    }

    /**
     * 将YUV420格式的数据转换为NV21格式的数据
     *
     * @param yuv420 YUV420格式的数据
     * @param width  图像宽度
     * @param height 图像高度
     * @return NV21格式的数据
     */
    public static byte[] convertYUV420ToNV21(byte[] yuv420, int width, int height) {
        int size = width * height;
        byte[] nv21 = new byte[size * 3 / 2];
        System.arraycopy(yuv420, 0, nv21, 0, size); // 复制Y分量数据
        for (int i = 0; i < size / 4; i++) {
            nv21[size + 2 * i] = yuv420[size + i];    // 复制V分量数据
            nv21[size + 2 * i + 1] = yuv420[size + size / 4 + i];    // 复制U分量数据
        }
        return nv21;
    }

}
