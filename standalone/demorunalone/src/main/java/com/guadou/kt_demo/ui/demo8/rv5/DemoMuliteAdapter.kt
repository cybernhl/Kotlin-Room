package com.guadou.kt_demo.ui.demo8.rv5

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guadou.kt_demo.R

class DemoMuliteAdapter(list: MutableList<MuliteTestBean>) : BaseMultiItemQuickAdapter<MuliteTestBean, BaseViewHolder>(list) {

    init {
        // 绑定 layout 对应的 type
        addItemType(MuliteTestBean.TYPE_TEXT, R.layout.item_demo_jobs)
        addItemType(MuliteTestBean.TYPE_IMAGE, R.layout.item_demo_image)
    }

    override fun convert(holder: BaseViewHolder, item: MuliteTestBean) {

        when (holder.itemViewType) {
            MuliteTestBean.TYPE_TEXT -> {

            }
            MuliteTestBean.TYPE_IMAGE -> {

            }

        }
    }
}