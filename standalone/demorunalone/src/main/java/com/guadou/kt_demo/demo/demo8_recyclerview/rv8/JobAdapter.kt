package com.guadou.kt_demo.demo.demo8_recyclerview.rv8

import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo8_recyclerview.base.BaseRVAdapter
import com.guadou.kt_demo.demo.demo8_recyclerview.base.BaseViewHolder
import com.guadou.lib_baselib.utils.CommUtils


class JobAdapter(datas: MutableList<String>) : BaseRVAdapter<String>(CommUtils.getContext(), datas, R.layout.item_custom_jobs) {

    override fun bindData(holder: BaseViewHolder, t: String?, position: Int) {

        holder.setText(R.id.tv_job_text, t)
    }
}