package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import android.os.Handler
import android.os.Looper
import androidx.recyclerview.widget.AdapterListUpdateCallback
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.Executor


class MyAsyncDiffer<T>(
    private val adapter: RecyclerView.Adapter<*>,
    private val config: MyAsyncDifferConfig<T>
) {

    private val mUpdateCallback: ListUpdateCallback = AdapterListUpdateCallback(adapter)
    private var mMainThreadExecutor: Executor = config.mainThreadExecutor ?: MainThreadExecutor()
    private val mListeners: MutableList<ListChangeListener<T>> = CopyOnWriteArrayList()
    private var mMaxScheduledGeneration = 0

    private var mList: List<T>? = null
    private var mReadOnlyList = emptyList<T>()

    private class MainThreadExecutor internal constructor() : Executor {
        val mHandler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            mHandler.post(command)
        }
    }

    fun getCurrentList(): List<T> {
        return mReadOnlyList
    }

    @JvmOverloads
    fun submitList(newList: MutableList<T>?, commitCallback: Runnable? = null) {

        val runGeneration: Int = ++mMaxScheduledGeneration
        if (newList == mList) {
            commitCallback?.run()
            return
        }

        val previousList = mReadOnlyList

        if (newList == null) {
            val countRemoved = mList?.size ?: 0
            mList = null
            mReadOnlyList = emptyList()

            mUpdateCallback.onRemoved(0, countRemoved)
            onCurrentListChanged(previousList, commitCallback)
            return
        }

        if (mList == null) {
            mList = newList
            mReadOnlyList = Collections.unmodifiableList(newList)
            mUpdateCallback.onInserted(0, newList.size)
            onCurrentListChanged(previousList, commitCallback)
            return
        }

        val oldList: List<T> = mList as List<T>
        config.backgroundThreadExecutor.execute {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return oldList.size
                }

                override fun getNewListSize(): Int {
                    return newList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    return if (oldItem != null && newItem != null) {
                        config.diffCallback.areItemsTheSame(oldItem, newItem)
                    } else oldItem == null && newItem == null
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.areContentsTheSame(oldItem, newItem)
                    }
                    if (oldItem == null && newItem == null) {
                        return true
                    }
                    throw AssertionError()
                }

                override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
                    val oldItem: T? = oldList[oldItemPosition]
                    val newItem: T? = newList[newItemPosition]
                    if (oldItem != null && newItem != null) {
                        return config.diffCallback.getChangePayload(oldItem, newItem)
                    }
                    throw AssertionError()
                }
            })
            mMainThreadExecutor.execute {
                if (mMaxScheduledGeneration == runGeneration) {
                    latchList(newList, result, commitCallback)
                }
            }
        }
    }

    private fun latchList(
        newList: MutableList<T>,
        diffResult: DiffUtil.DiffResult,
        commitCallback: Runnable?
    ) {


        val previousList = mReadOnlyList
        mList = newList
        mReadOnlyList = Collections.unmodifiableList(newList)
        diffResult.dispatchUpdatesTo(mUpdateCallback)
        onCurrentListChanged(previousList, commitCallback)
    }

    private fun onCurrentListChanged(
        previousList: List<T>,
        commitCallback: Runnable?
    ) {

        for (listener in mListeners) {
            listener.onCurrentListChanged(previousList, mReadOnlyList)
        }
        commitCallback?.run()
    }

    //定义接口
    interface ListChangeListener<T> {
        fun onCurrentListChanged(previousList: List<T>, currentList: List<T>)
    }

    fun addListListener(listener: ListChangeListener<T>) {
        mListeners.add(listener)
    }

    fun removeListListener(listener: ListChangeListener<T>) {
        mListeners.remove(listener)
    }

    fun clearAllListListener() {
        mListeners.clear()
    }

}