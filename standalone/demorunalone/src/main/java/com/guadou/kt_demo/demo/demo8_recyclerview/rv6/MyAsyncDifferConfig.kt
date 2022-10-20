package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import android.annotation.SuppressLint
import androidx.annotation.RestrictTo
import androidx.recyclerview.widget.DiffUtil
import java.util.concurrent.Executor
import java.util.concurrent.Executors


class MyAsyncDifferConfig <T>(
    @SuppressLint("SupportAnnotationUsage")
    @RestrictTo(RestrictTo.Scope.LIBRARY)
    val mainThreadExecutor: Executor?,
    val backgroundThreadExecutor: Executor,
    val diffCallback: DiffUtil.ItemCallback<T>) {

    class Builder<T>(private val mDiffCallback: DiffUtil.ItemCallback<T>) {

        companion object {
            private val sExecutorLock = Any()
            private var sDiffExecutor: Executor? = null
        }

        private var mMainThreadExecutor: Executor? = null
        private var mBackgroundThreadExecutor: Executor? = null

        fun setMainThreadExecutor(executor: Executor?): Builder<T> {
            mMainThreadExecutor = executor
            return this
        }

        fun setBackgroundThreadExecutor(executor: Executor?): Builder<T> {
            mBackgroundThreadExecutor = executor
            return this
        }


        fun build(): MyAsyncDifferConfig<T> {
            if (mBackgroundThreadExecutor == null) {
                synchronized(sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newSingleThreadExecutor()
                    }
                }
                mBackgroundThreadExecutor = sDiffExecutor
            }
            return MyAsyncDifferConfig(
                mMainThreadExecutor,
                mBackgroundThreadExecutor!!,
                mDiffCallback)
        }
    }

}