package com.guadou.kt_demo.demo.demo8_recyclerview.rv8

import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo8_recyclerview.base.BaseRVAdapter
import com.guadou.kt_demo.demo.demo8_recyclerview.base.BaseViewHolder
import com.guadou.kt_demo.demo.demo8_recyclerview.base.IHasMoreType
import com.guadou.lib_baselib.utils.CommUtils


class Job2Adapter(datas: MutableList<MoreJob>) : BaseRVAdapter<MoreJob>(CommUtils.getContext(), datas,
    IHasMoreType<MoreJob> {
        if (it.type == 1) return@IHasMoreType R.layout.item_custom_jobs
        else return@IHasMoreType R.layout.item_custom_image
    }) {

    override fun bindData(holder: BaseViewHolder, bean: MoreJob, position: Int) {
        if (bean.type == 1) {
            holder.setText(R.id.tv_job_text, bean.name)
        } else {
            holder.setText(R.id.tv_name, bean.name)
        }
    }

}