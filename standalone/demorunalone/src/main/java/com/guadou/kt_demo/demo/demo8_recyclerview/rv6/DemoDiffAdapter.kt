package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.utils.CheckUtil
import com.guadou.lib_baselib.utils.Log.YYLogUtils
import javax.inject.Inject

/**
 * 原生的BRVAH
 */
class DemoDiffAdapter @Inject constructor() :
    BaseQuickAdapter<DemoDiffBean, BaseViewHolder>(R.layout.item_diff_jobs), LoadMoreModule {

    //差分刷新的精准刷新调用到这里
    override fun convert(holder: BaseViewHolder, item: DemoDiffBean, payloads: List<Any>) {
        if (!CheckUtil.isEmpty(payloads) && (payloads[0] as String) == "text") {
            YYLogUtils.w("差分刷新 -------- 文本更新")
            holder.setText(R.id.tv_job_text, item.content)
        }
    }

    //默认数据赋值到这里
    override fun convert(holder: BaseViewHolder, item: DemoDiffBean) {
        YYLogUtils.w("默认数据赋值 --------")
        holder.setText(R.id.tv_job_text, item.content)
    }

}