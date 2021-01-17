package com.guadou.kt_demo.demo.demo8_recyclerview.rv5

import android.util.SparseArray
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.cs_cptservices.binding.BaseMultiDataBindingAdapter
import com.guadou.kt_demo.R

class DemoMuliteAdapter(br: Int, list: MutableList<MuliteTestBean>) :
    BaseMultiDataBindingAdapter<MuliteTestBean>(br, list) {

    init {
        // 绑定 layout 对应的 type
        addItemType(MuliteTestBean.TYPE_TEXT, R.layout.item_multi_jobs)
        addItemType(MuliteTestBean.TYPE_IMAGE, R.layout.item_demo_image)
    }

    override fun convert(holder: BaseViewHolder, item: MuliteTestBean) {
        super.convert(holder, item)

        when (holder.itemViewType) {
            MuliteTestBean.TYPE_TEXT -> {

            }
            MuliteTestBean.TYPE_IMAGE -> {

            }

        }
    }

}