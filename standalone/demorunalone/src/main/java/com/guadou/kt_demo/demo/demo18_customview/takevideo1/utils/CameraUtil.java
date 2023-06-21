package com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Log;
import android.util.Size;
import android.view.TextureView;
import android.view.ViewGroup;

import com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl3.BaseCameraProvider;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraUtil {

    //选择sizeMap中大于并且最接近width和height的size
    public static Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }

    public static void transTextureView(ViewGroup parentViewGroup, TextureView mPreviewView) {
        int parentHeight = parentViewGroup.getHeight();
        int parentWidth = parentViewGroup.getWidth();
        int tarHeight = mPreviewView.getHeight();
        int tarWidth = mPreviewView.getWidth();
        YYLogUtils.d("parentHeight::" + parentHeight + " parentWidth::" + parentWidth + " tarHeight::" + tarHeight + " tarWidth::" + tarWidth);
        if (parentWidth * 1.0f / parentHeight > tarWidth * 1.0f / tarHeight) {
            // parent的宽高比 比 预览的宽高比大, 也就是parent比较宽，预览比较细长，需要移动x轴
            int deltaX = (int) (parentWidth - parentHeight * 1.0f * tarWidth / tarHeight);
            YYLogUtils.d("deltaX::" + deltaX);
        } else {
            int deltaY = (int) (parentHeight - parentWidth * 1.0f * tarHeight / tarWidth);
            YYLogUtils.d("deltaY::" + deltaY);
        }
    }

    public static void transTextureView(TextureView mPreviewView) {
        int minus = BaseCameraProvider.textureViewSize.getWidth() - BaseCameraProvider.textureViewSize.getWidth();
        mPreviewView.setTranslationX(-minus / 2);
    }

    public static final int COLOR_FormatI420 = 1;
    public static final int COLOR_FormatNV21 = 2;

    private static boolean isImageFormatSupported(Image image) {
        int format = image.getFormat();
        switch (format) {
            case ImageFormat.YUV_420_888:
            case ImageFormat.NV21:
            case ImageFormat.YV12:
                return true;
            default:
        }
        return false;
    }


    public static byte[] getDataFromImage(Image image, int colorFormat) {
        if (colorFormat != COLOR_FormatI420 && colorFormat != COLOR_FormatNV21) {
            throw new IllegalArgumentException("only support COLOR_FormatI420 " + "and COLOR_FormatNV21");
        }
        if (!isImageFormatSupported(image)) {
            throw new RuntimeException("can't convert Image to byte array, format " + image.getFormat());
        }
        Rect crop = image.getCropRect();
        int format = image.getFormat();
        int width = crop.width();
        int height = crop.height();
        Image.Plane[] planes = image.getPlanes();
        byte[] data = new byte[width * height * ImageFormat.getBitsPerPixel(format) / 8];
        byte[] rowData = new byte[planes[0].getRowStride()];

        Log.d("TAG", planes.length + " planes");

        int channelOffset = 0;
        int outputStride = 1;
        for (int i = 0; i < planes.length; i++) {
            switch (i) {
                case 0:
                    channelOffset = 0;
                    outputStride = 1;
                    break;
                case 1:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = width * height;
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height + 1;
                        outputStride = 2;
                    }
                    break;
                case 2:
                    if (colorFormat == COLOR_FormatI420) {
                        channelOffset = (int) (width * height * 1.25);
                        outputStride = 1;
                    } else if (colorFormat == COLOR_FormatNV21) {
                        channelOffset = width * height;
                        outputStride = 2;
                    }
                    break;
                default:
            }
            ByteBuffer buffer = planes[i].getBuffer();
            buffer.rewind();
            int rowStride = planes[i].getRowStride();
            int pixelStride = planes[i].getPixelStride();

            Log.d("TAG", "pixelStride " + pixelStride);
            Log.d("TAG", "rowStride " + rowStride);
            Log.d("TAG", "width " + width);
            Log.d("TAG", "height " + height);
            Log.d("TAG", "buffer size " + buffer.remaining());

            int shift = (i == 0) ? 0 : 1;
            int w = width >> shift;
            int h = height >> shift;
            buffer.position(rowStride * (crop.top >> shift) + pixelStride * (crop.left >> shift));
            for (int row = 0; row < h; row++) {
                int length;
                if (pixelStride == 1 && outputStride == 1) {
                    length = w;
                    buffer.get(data, channelOffset, length);
                    channelOffset += length;
                } else {
                    length = (w - 1) * pixelStride + 1;
                    buffer.get(rowData, 0, length);
                    for (int col = 0; col < w; col++) {
                        data[channelOffset] = rowData[col * pixelStride];
                        channelOffset += outputStride;
                    }
                }
                if (row < h - 1) {
                    buffer.position(buffer.position() + rowStride - length);
                }
            }

            Log.d("TAG", "Finished reading data from plane " + i);

        }
        return data;
    }

}
