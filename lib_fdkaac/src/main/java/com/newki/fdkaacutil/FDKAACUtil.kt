package com.newki.fdkaacutil

import android.util.Log


class FDKAACUtil {

    init {
        try {
            System.loadLibrary("fdkcodec")
        } catch (e: Exception) {
            Log.e("FDKAACUtil", "Couldn't load fdkcodec library: $e")
        }
    }

    external fun init(sampleRate: Int, channelCount: Int, bitRate: Int)

    external fun encode(input: ShortArray?): ByteArray?

    external fun release()

    external fun initDecoder()

    external fun decode(input: ByteArray?): ByteArray?

    external fun releaseDecoder()
}