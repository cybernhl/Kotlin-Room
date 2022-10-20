package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import androidx.recyclerview.widget.RecyclerView

class MyDifferController<T> : IMyDifferController<T> {

    private var mDiffer: MyAsyncDiffer<T>? = null

    override fun RecyclerView.Adapter<*>.initDiffer(config: MyAsyncDifferConfig<T>): MyAsyncDiffer<T> {
        mDiffer = MyAsyncDiffer(this, config)

        val mListener: MyAsyncDiffer.ListChangeListener<T> = object : MyAsyncDiffer.ListChangeListener<T> {
            override fun onCurrentListChanged(previousList: List<T>, currentList: List<T>) {
                this@initDiffer.onCurrentListChanged(previousList, currentList)
            }
        }

        mDiffer?.addListListener(mListener)

        return mDiffer!!
    }

    override fun getDiffer(): MyAsyncDiffer<T>? {
        return mDiffer
    }

    override fun getCurrentList(): List<T> {
        return mDiffer?.getCurrentList() ?: emptyList()
    }

    override fun setDiffNewData(list: MutableList<T>, commitCallback: Runnable?) {
        mDiffer?.submitList(list, commitCallback)
    }

    override fun addDiffNewData(list: MutableList<T>, commitCallback: Runnable?) {
        val newList = mutableListOf<T>()
        newList.addAll(mDiffer?.getCurrentList() ?: emptyList())
        newList.addAll(list)
        mDiffer?.submitList(newList, commitCallback)
    }

    override fun addDiffNewData(t: T, commitCallback: Runnable?) {
        val newList = mutableListOf<T>()
        newList.addAll(mDiffer?.getCurrentList() ?: emptyList())
        newList.add(t)
        mDiffer?.submitList(newList, commitCallback)
    }

    override fun removeDiffData(index: Int) {
        val newList = mutableListOf<T>()
        newList.addAll(mDiffer?.getCurrentList() ?: emptyList())
        newList.removeAt(index)
        mDiffer?.submitList(newList)
    }

    override fun clearDiffData() {
        mDiffer?.submitList(null)
    }

    override fun RecyclerView.Adapter<*>.onCurrentListChanged(previousList: List<T>, currentList: List<T>) {

    }

}