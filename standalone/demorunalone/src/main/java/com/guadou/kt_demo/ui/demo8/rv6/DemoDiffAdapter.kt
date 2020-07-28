package com.guadou.kt_demo.ui.demo8.rv6

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guadou.kt_demo.R

class DemoDiffAdapter(list: MutableList<DemoDiffBean>) :

    BaseQuickAdapter<DemoDiffBean, BaseViewHolder>(R.layout.item_demo_jobs, list), LoadMoreModule {


    //差分刷新的精准刷新调用到这里
    override fun convert(holder: BaseViewHolder, item: DemoDiffBean, payloads: List<Any>) {

        holder.setText(R.id.tv_job_text, item.content)
    }

    //默认数据赋值到这里
    override fun convert(holder: BaseViewHolder, item: DemoDiffBean) {

        holder.setText(R.id.tv_job_text, item.content)
    }

}