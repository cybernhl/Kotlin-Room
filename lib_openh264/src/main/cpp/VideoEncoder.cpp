
#include "VideoEncoder.h"

VideoEncoder::VideoEncoder(JNIEnv *jniEnv, jstring path) {
    Init(jniEnv, path);
}


void VideoEncoder::Encode(uint8_t *yuv_data, int width, int height) {
    if (yuv_data == NULL || *yuv_data == 0 || width == 0 || height == 0) {
        LOGE(TAG, "data err!")
        return;
    }
    LOGI(TAG, "Encode data .....yuv_data width=%d,height=%d", width, height)

    sPicture.iColorFormat = videoFormatI420;
    sPicture.iPicWidth = width;
    sPicture.iPicHeight = height;
    sPicture.iStride[0] = sPicture.iPicWidth;
    sPicture.iStride[1] = sPicture.iStride[2] = sPicture.iPicWidth / 2;
    sPicture.uiTimeStamp = timestamp_++;

    sPicture.pData[0] = yuv_data;
    sPicture.pData[1] = yuv_data + width * height;
    sPicture.pData[2] = yuv_data + width * height * 5 / 4;
    int err = ppEncoder->EncodeFrame(&sPicture, &encoded_frame_info);
    if (err) {
        LOGE(TAG, "Encode err err=%d", err);
        return;
    }
    unsigned char *outBuf = encoded_frame_info.sLayerInfo[0].pBsBuf;
    LOGI(TAG, "frame type:%d", encoded_frame_info.sLayerInfo[0].eFrameType)
    if (out264.is_open()) {
        out264.write(reinterpret_cast<const char *>(outBuf), encoded_frame_info.iFrameSizeInBytes);
    }
}


void VideoEncoder::Init(JNIEnv *jniEnv, jstring path) {
    LOGI(TAG, "VideoDecoder Init start")
    jobject m_path_ref = jniEnv->NewGlobalRef(path);
    m_path = jniEnv->GetStringUTFChars(path, NULL);
    jniEnv->GetJavaVM(&m_jvm_for_thread);
    int result = WelsCreateSVCEncoder(&ppEncoder);
    if (result != cmResultSuccess) {
        LOGE(TAG, "WelsCreateSVCEncoder fail ! result=%d", result)
        return;
    }
    //获取默认参数
    ppEncoder->GetDefaultParams(&paramExt);
    ECOMPLEXITY_MODE complexityMode = HIGH_COMPLEXITY;
    RC_MODES rc_mode = RC_BITRATE_MODE;
    bool bEnableAdaptiveQuant = false;

    //TODO 这里暂时写死,需要外面对应预览分辨率也设置成一样
    paramExt.iUsageType = CAMERA_VIDEO_REAL_TIME;
    paramExt.iPicWidth = 720;
    paramExt.iPicHeight = 1280;
    paramExt.iTargetBitrate = 720 * 1280 * 4;
    paramExt.iMaxBitrate = 720 * 1280 * 5;
    paramExt.iRCMode = rc_mode;
    paramExt.fMaxFrameRate = 30;
    paramExt.iTemporalLayerNum = 1;
    paramExt.iSpatialLayerNum = 1;
    paramExt.bEnableDenoise = false;
    paramExt.bEnableBackgroundDetection = true;
    paramExt.bEnableAdaptiveQuant = false;
    paramExt.bEnableFrameSkip = false;
    paramExt.bEnableLongTermReference = false;
    paramExt.bEnableAdaptiveQuant = bEnableAdaptiveQuant;
    paramExt.bEnableSSEI = true;
    paramExt.bEnableSceneChangeDetect = true;
    paramExt.uiIntraPeriod = 15u;
    paramExt.eSpsPpsIdStrategy = CONSTANT_ID;
    paramExt.bPrefixNalAddingCtrl = false;
    paramExt.iComplexityMode = complexityMode;
    paramExt.bEnableFrameSkip = false;

    paramExt.sSpatialLayers[0].iVideoWidth = paramExt.iPicWidth;
    paramExt.sSpatialLayers[0].iVideoHeight = paramExt.iPicHeight;
    paramExt.sSpatialLayers[0].fFrameRate = paramExt.fMaxFrameRate;
    paramExt.sSpatialLayers[0].iSpatialBitrate = paramExt.iTargetBitrate;
    paramExt.sSpatialLayers[0].iMaxSpatialBitrate = paramExt.iMaxBitrate;

    ppEncoder->InitializeExt(&paramExt);
    out264.open(m_path, std::ios::ate | std::ios::binary);

    LOGI(TAG, "VideoDecoder Init end")
}


VideoEncoder::~VideoEncoder() {
    //TODO
    if (ppEncoder) {
        ppEncoder->Uninitialize();
        WelsDestroySVCEncoder(ppEncoder);
    }
}

