package com.guadou.kt_demo.ui.demo8.rv4.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guadou.kt_demo.R
import com.guadou.kt_demo.ui.demo8.rv4.bean.FullJobsPage
import com.guadou.lib_baselib.ext.engine.extLoad

class DemoJobAdapter(list: MutableList<FullJobsPage.FullJobs>) :
    BaseQuickAdapter<FullJobsPage.FullJobs, BaseViewHolder>(R.layout.item_demo_jobs, list) ,LoadMoreModule{

    override fun convert(holder: BaseViewHolder, item: FullJobsPage.FullJobs) {

        holder.getView<ImageView>(R.id.iv_job_img).extLoad(item.employer_logo, R.mipmap.home_list_plachholder)
        holder.setText(R.id.tv_job_text, item.title)
    }
}