
package com.newki.glrecordx.camerax.filters.gpuFilters.paramsFilter

import android.opengl.GLES20
import com.newki.glrecordx.camerax.filters.gpuFilters.baseFilter.GPUImageFilter

/**
 * brightness value ranges from -1.0 to 1.0, with 0.0 as the normal level
 */
class GPUImageBrightnessFilter constructor(private var mBrightness: Float = 0.5f) :
    GPUImageFilter(
        NO_FILTER_VERTEX_SHADER, BRIGHTNESS_FRAGMENT_SHADER
    ) {
    private var mBrightnessLocation = 0
    public override fun onInit() {
        super.onInit()
        mBrightnessLocation = GLES20.glGetUniformLocation(program, "brightness")
    }

    public override fun onInitialized() {
        super.onInitialized()
        setBrightness(mBrightness)
    }

    fun setBrightness(brightness: Float) {
        mBrightness = brightness
        setFloat(mBrightnessLocation, mBrightness)
    }

    companion object {
        const val BRIGHTNESS_FRAGMENT_SHADER = "" +
                "varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform lowp float brightness;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     gl_FragColor = vec4((textureColor.rgb + vec3(brightness)), textureColor.w);\n" +
                " }"
    }
}