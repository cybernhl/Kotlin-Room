package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import androidx.recyclerview.widget.RecyclerView

//设置别名简化
typealias IDiffer<T> = IMyDifferController<T>

fun <T> differ(): MyDifferController<T> = MyDifferController()

interface IMyDifferController<T> {

    fun RecyclerView.Adapter<*>.initDiffer(config: MyAsyncDifferConfig<T>): MyAsyncDiffer<T>

    fun getDiffer(): MyAsyncDiffer<T>?

    fun getCurrentList(): List<T>

    fun setDiffNewData(list: MutableList<T>, commitCallback: Runnable? = null)

    fun addDiffNewData(list: MutableList<T>, commitCallback: Runnable? = null)

    fun addDiffNewData(t: T, commitCallback: Runnable? = null)

    fun removeDiffData(index: Int)

    fun clearDiffData()

    fun RecyclerView.Adapter<*>.onCurrentListChanged(previousList: List<T>, currentList: List<T>)
}