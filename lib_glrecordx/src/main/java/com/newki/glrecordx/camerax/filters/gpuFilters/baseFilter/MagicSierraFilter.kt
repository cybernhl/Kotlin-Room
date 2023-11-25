package com.newki.glrecordx.camerax.filters.gpuFilters.baseFilter

import com.newki.glrecordx.camerax.filters.gpuFilters.utils.OpenGlUtils.readShaderFromRawResource
import com.newki.glrecordx.camerax.filters.gpuFilters.utils.OpenGlUtils.loadTexture
import com.newki.glrecordx.camerax.utils.GLCameraxUtils.Companion.getApplicationContext
import android.opengl.GLES20
import com.newki.glrecordx.camerax.R
import com.newki.glrecordx.camerax.filters.gpuFilters.utils.OpenGlUtils
import java.util.*

class MagicSierraFilter :
    GPUImageFilter(NO_FILTER_VERTEX_SHADER, readShaderFromRawResource(R.raw.sierra)) {
    private val inputTextureHandles = intArrayOf(-1, -1, -1)
    private val inputTextureUniformLocations = intArrayOf(-1, -1, -1)
    private var mGLStrengthLocation = 0
    public override fun onDestroy() {
        super.onDestroy()
        GLES20.glDeleteTextures(inputTextureHandles.size, inputTextureHandles, 0)
        Arrays.fill(inputTextureHandles, -1)
    }

    override fun onDrawArraysAfter() {
        var i = 0
        while (i < inputTextureHandles.size
            && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE
        ) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3))
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
            i++
        }
    }

    override fun onDrawArraysPre() {
        var i = 0
        while (i < inputTextureHandles.size
            && inputTextureHandles[i] != OpenGlUtils.NO_TEXTURE
        ) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + (i + 3))
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureHandles[i])
            GLES20.glUniform1i(inputTextureUniformLocations[i], i + 3)
            i++
        }
    }

    public override fun onInit() {
        super.onInit()
        for (i in inputTextureUniformLocations.indices) inputTextureUniformLocations[i] =
            GLES20.glGetUniformLocation(program, "inputImageTexture" + (2 + i))
        mGLStrengthLocation = GLES20.glGetUniformLocation(
            program,
            "strength"
        )
    }

    public override fun onInitialized() {
        super.onInitialized()
        setFloat(mGLStrengthLocation, 1.0f)
        runOnDraw {
            inputTextureHandles[0] =
                loadTexture(getApplicationContext(), "filter/sierravignette.png")
            inputTextureHandles[1] = loadTexture(getApplicationContext(), "filter/overlaymap.png")
            inputTextureHandles[2] = loadTexture(getApplicationContext(), "filter/sierramap.png")
        }
    }
}