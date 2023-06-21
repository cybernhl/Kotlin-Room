package com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl2

/**
 * 自定义滤镜的回调
 */
interface Filter {
    fun onDrawFrame(textureId: Int): Int
    fun setTransformMatrix(mtx: FloatArray)
    fun onReady(width: Int, height: Int)
}