package com.guadou.kt_demo.demo.demo18_customview.takevideo1.audio

import android.media.MediaCodec
import java.nio.ByteBuffer

data class EncodeData(val buffer: ByteBuffer,val bufferInfo: MediaCodec.BufferInfo) {
}