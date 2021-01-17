package com.guadou.cs_cptservices.binding

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder

/**
 * 基类的BRVAH的DataBinding封装
 */
open class BaseDataBindingAdapter<T>(layoutResId: Int, br: Int, list: MutableList<T> = mutableListOf()) :
    BaseQuickAdapter<T, BaseViewHolder>(layoutResId, list), LoadMoreModule {

    private val _br: Int = br

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        // 绑定databinding
        DataBindingUtil.bind<ViewDataBinding>(viewHolder.itemView)
    }

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