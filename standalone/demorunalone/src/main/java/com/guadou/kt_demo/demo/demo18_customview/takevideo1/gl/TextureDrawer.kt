package com.guadou.kt_demo.demo.demo18_customview.takevideo1.gl

import android.content.Context
import android.opengl.GLES11Ext
import android.opengl.GLES20
import com.guadou.kt_demo.R
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class TextureDrawer(context: Context, OESTextureId: Int) {

    private val mBuffer: FloatBuffer?

    private var mOESTextureId = -1
    private var mShaderProgram = -1

    private var aPositionLocation = -1
    private var aTextureCoordLocation = -1
    private var uTextureMatrixLocation = -1
    private var uTextureSamplerLocation = -1
    private var uTimestampLocation = -1;
    private var uEffectIndexLocation = 0

    init {
        mOESTextureId = OESTextureId
        mBuffer = ByteBuffer.allocateDirect(vertexData.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        mBuffer.put(vertexData, 0, vertexData.size).position(0)

        val vertexShader = TextResourceReader.readTextFileFromResource(context, R.raw.vertex)
        val fragmentShader = TextResourceReader.readTextFileFromResource(context, R.raw.fragment)
        mShaderProgram = ShaderHelper.buildProgram(vertexShader, fragmentShader)

        aPositionLocation = GLES20.glGetAttribLocation(mShaderProgram, POSITION_ATTRIBUTE)
        aTextureCoordLocation = GLES20.glGetAttribLocation(mShaderProgram, TEXTURE_COORD_ATTRIBUTE)

        uTextureMatrixLocation = GLES20.glGetUniformLocation(mShaderProgram, TEXTURE_MATRIX_UNIFORM)
        uTextureSamplerLocation = GLES20.glGetUniformLocation(mShaderProgram, TEXTURE_SAMPLER_UNIFORM)
        uEffectIndexLocation = GLES20.glGetUniformLocation(mShaderProgram, EFFECT_INDEX)
        uTimestampLocation = GLES20.glGetUniformLocation(mShaderProgram, TIME_STAMP)
    }

    fun drawTexture(transformMatrix: FloatArray, effectIndex: Int, timestamp:Float) {
        GLES20.glUseProgram(mShaderProgram)
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, mOESTextureId)
        GLES20.glUniform1i(uTextureSamplerLocation, 0)
        GLES20.glUniformMatrix4fv(uTextureMatrixLocation, 1, false, transformMatrix, 0)
        GLES20.glUniform1i(uEffectIndexLocation, effectIndex)
        GLES20.glUniform1f(uTimestampLocation, timestamp)

        if (mBuffer != null) {
            mBuffer.position(0)
            GLES20.glEnableVertexAttribArray(aPositionLocation)
            GLES20.glVertexAttribPointer(aPositionLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                16,
                mBuffer)

            mBuffer.position(2)
            GLES20.glEnableVertexAttribArray(aTextureCoordLocation)
            GLES20.glVertexAttribPointer(
                aTextureCoordLocation,
                2,
                GLES20.GL_FLOAT,
                false,
                16,
                mBuffer
            )

            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4)

            GLES20.glDisableVertexAttribArray(aPositionLocation)
            GLES20.glDisableVertexAttribArray(aTextureCoordLocation)
        }
    }

    fun drawTexture(transformMatrix: FloatArray, timestamp:Float) {
        drawTexture(transformMatrix, -1, timestamp)
    }


    companion object {
        private val vertexData = floatArrayOf(
            -1f, 1f, 0f, 1f,
            -1f, -1f, 0f, 0f,
            1f, 1f, 1f, 1f,
            1f, -1f, 1f, 0f
        )

        private const val POSITION_ATTRIBUTE = "aPosition"
        private const val TEXTURE_COORD_ATTRIBUTE = "aTextureCoordinate"
        private const val EFFECT_INDEX = "uEffectIndex"
        private const val TIME_STAMP = "uTimestamp"
        private const val TEXTURE_MATRIX_UNIFORM = "uTextureMatrix"
        private const val TEXTURE_SAMPLER_UNIFORM = "uTextureSampler"
    }
}