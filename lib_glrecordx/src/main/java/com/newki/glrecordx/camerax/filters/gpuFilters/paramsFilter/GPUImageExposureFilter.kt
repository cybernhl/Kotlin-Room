
package com.newki.glrecordx.camerax.filters.gpuFilters.paramsFilter

import android.opengl.GLES20
import com.newki.glrecordx.camerax.filters.gpuFilters.baseFilter.GPUImageFilter

/**
 * exposure: The adjusted exposure (-10.0 - 10.0, with 0.0 as the default)
 */
class GPUImageExposureFilter constructor(private var mExposure: Float = 0.8f) :
    GPUImageFilter(
        NO_FILTER_VERTEX_SHADER, EXPOSURE_FRAGMENT_SHADER
    ) {
    private var mExposureLocation = 0
    public override fun onInit() {
        super.onInit()
        mExposureLocation = GLES20.glGetUniformLocation(program, "exposure")
    }

    public override fun onInitialized() {
        super.onInitialized()
        setExposure(mExposure)
    }

    fun setExposure(exposure: Float) {
        mExposure = exposure
        setFloat(mExposureLocation, mExposure)
    }

    companion object {
        const val EXPOSURE_FRAGMENT_SHADER = "" +
                " varying highp vec2 textureCoordinate;\n" +
                " \n" +
                " uniform sampler2D inputImageTexture;\n" +
                " uniform highp float exposure;\n" +
                " \n" +
                " void main()\n" +
                " {\n" +
                "     highp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n" +
                "     \n" +
                "     gl_FragColor = vec4(textureColor.rgb * pow(2.0, exposure), textureColor.w);\n" +
                " } "
    }
}