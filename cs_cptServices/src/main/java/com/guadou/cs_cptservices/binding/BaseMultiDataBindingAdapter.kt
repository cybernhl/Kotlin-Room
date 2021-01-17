package com.guadou.cs_cptservices.binding

import android.util.SparseArray
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 基类的BRVAH的DataBinding封装(多布局)
 * 需要再子类实现多布局逻辑，这里只是实现了Item的DataBinding
 */
open class BaseMultiDataBindingAdapter<T : MultiItemEntity>(
    br: Int,
    list: MutableList<T> = mutableListOf()
) : BaseMultiItemQuickAdapter<T, BaseViewHolder>(list), LoadMoreModule {

    private val _br: Int = br

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        // 绑定databinding
        DataBindingUtil.bind<ViewDataBinding>(viewHolder.itemView)
    }


    @Suppress("SENSELESS_COMPARISON")
    override fun convert(holder: BaseViewHolder, item: T) {
        if (item == null) {
            return
        }

        holder.getBinding<ViewDataBinding>()?.run {
            setVariable(_br, item)
            executePendingBindings()
        }

    }


}