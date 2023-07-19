package com.newki.glrecord.utils

import android.app.Application
import java.util.concurrent.TimeUnit


class GLCamera1Utils private constructor(private val application: Application){

    companion object {
        private var instance: GLCamera1Utils? = null

        fun init(application: Application){
            if(null == instance) {
                instance = GLCamera1Utils(application)
            }
        }

        @JvmStatic
        fun getApplicationContext(): Application {
            return checkNotNull(instance?.application){
                "GLCameraxUtils instance == null!"
            }
        }

        /**
         * 获取格式化之后的时间值：mm:ss:sss
         */
        fun getTimeInfoFromNanoTime(time: Long): String{
            val second = TimeUnit.NANOSECONDS.toSeconds(time)
            val minutes = TimeUnit.NANOSECONDS.toMinutes(time)
            val nanosString = TimeUnit.NANOSECONDS.toNanos(time).toString()
            val nanosResult = nanosString.getOrNull(nanosString.length - 9)
            return getTimeString(minutes).plus(":")
                .plus(getTimeString(second)).plus(".").plus(nanosResult?:"0")
        }
        private fun getTimeString(time:Long) = if(time < 10) "0".plus(time) else time.toString()
    }

}