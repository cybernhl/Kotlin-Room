package com.newki.openh264util

import android.util.Log


class OpenH264Util {

    init {
        try {
            System.loadLibrary("openh264util")
        } catch (e: Exception) {
            Log.e("OpenH264Util", "Couldn't load openh264util library: $e")
        }
    }

    external fun createEncoder(width: Int, height: Int, outputPath: String): Long

    external fun encode(pEncoder: Long, data: ByteArray?, width: Int, height: Int): Long

}