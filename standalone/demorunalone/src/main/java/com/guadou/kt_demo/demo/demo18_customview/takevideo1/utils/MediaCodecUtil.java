package com.guadou.kt_demo.demo.demo18_customview.takevideo1.utils;

import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;

import com.guadou.lib_baselib.utils.log.YYLogUtils;

public class MediaCodecUtil {
    public static final String TAG = "MediaCodec";

    public static void echoCodecList() {
        MediaCodecList allMediaCodecLists = new MediaCodecList(-1);
        MediaCodecList regularMediaCodecLists = new MediaCodecList(MediaCodecList.REGULAR_CODECS);
//        echoMediaLCodecList("all - ", allMediaCodecLists);
        echoMediaLCodecList("regular - ", regularMediaCodecLists);
    }

    private static void echoMediaLCodecList(String tag, MediaCodecList codecList) {
        StringBuilder sb = new StringBuilder(tag);
        for (MediaCodecInfo mediaCodecInfo : codecList.getCodecInfos()) {
            sb.append(mediaCodecInfo.getName()).append(":");
            for (String supportType : mediaCodecInfo.getSupportedTypes()) {
                sb.append("| ").append(supportType);
            }
            sb.append(" \n");
        }
        YYLogUtils.d(TAG, sb.toString());
    }

    public static void getSupportTypes() {
        MediaCodecList allMediaCodecLists = new MediaCodecList(-1);
        for (MediaCodecInfo mediaCodecInfo : allMediaCodecLists.getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                String[] supportTypes = mediaCodecInfo.getSupportedTypes();
                for (String supportType : supportTypes) {
                    if (supportType.equals(MediaFormat.MIMETYPE_VIDEO_AVC)) {
                        YYLogUtils.d(TAG, "编码器名称:" + mediaCodecInfo.getName() + "  " + supportType);
                        MediaCodecInfo.CodecCapabilities codecCapabilities = mediaCodecInfo.getCapabilitiesForType(MediaFormat.MIMETYPE_VIDEO_AVC);
                        int[] colorFormats = codecCapabilities.colorFormats;
                        for (int colorFormat : colorFormats) {
                            switch (colorFormat) {
                                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
                                case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
                                    YYLogUtils.d(MediaCodecUtil.TAG, "支持的格式::" + colorFormat);
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    // OMX.google开头的是软解码
    // OMX.开头的是硬解码
    public static String getExpectedEncodeCodec(String expectedMimeType, int expectedColorFormat) {
        MediaCodecList allMediaCodecLists = new MediaCodecList(-1);
        for (MediaCodecInfo mediaCodecInfo : allMediaCodecLists.getCodecInfos()) {
            if (mediaCodecInfo.isEncoder()) {
                String[] supportTypes = mediaCodecInfo.getSupportedTypes();
                for (String supportType : supportTypes) {
                    if (supportType.equals(expectedMimeType)) {
                        YYLogUtils.d(TAG, "编码器名称:" + mediaCodecInfo.getName() + "  " + supportType);
                        MediaCodecInfo.CodecCapabilities codecCapabilities = mediaCodecInfo.getCapabilitiesForType(expectedMimeType);
                        int[] colorFormats = codecCapabilities.colorFormats;
                        for (int colorFormat : colorFormats) {
                            if (colorFormat == expectedColorFormat) {
                                return mediaCodecInfo.getName();
                            }
                        }
                    }
                }
            }
        }
        return "";
    }

}