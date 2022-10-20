package com.guadou.kt_demo.demo.demo8_recyclerview.rv7

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo8_recyclerview.base.BaseViewHolder
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.*
import com.guadou.lib_baselib.utils.CheckUtil
import com.guadou.lib_baselib.utils.log.YYLogUtils


class MyDiffAdapter() : RecyclerView.Adapter<BaseViewHolder>(), IDiffer<DemoDiffBean> by differ() {

    init {
        initDiffer(MyAsyncDifferConfig.Builder(DiffDemoCallback()).build())
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {

        if (!CheckUtil.isEmpty(payloads) && (payloads[0] as String) == "text") {
            YYLogUtils.w("差分刷新 -------- 文本更新")

            holder.setText(R.id.tv_job_text, getCurrentList()[position].content)

        } else {
            onBindViewHolder(holder, position)
        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        YYLogUtils.w("默认数据赋值 --------")
        holder.setText(R.id.tv_job_text, getCurrentList()[position].content)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_diff_jobs, parent, false))
    }

    override fun getItemCount(): Int {
        return getCurrentList().size
    }

}