package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import androidx.recyclerview.widget.DiffUtil

class DiffDemoCallback : DiffUtil.ItemCallback<DemoDiffBean>() {
    /**
     * 判断是否是同一个item
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    override fun areItemsTheSame(oldItem: DemoDiffBean, newItem: DemoDiffBean): Boolean {
        return oldItem.id == newItem.id
    }

    /**
     * 当是同一个item时，再判断内容是否发生改变
     *
     * @param oldItem New data
     * @param newItem old Data
     * @return
     */
    override fun areContentsTheSame(oldItem: DemoDiffBean, newItem: DemoDiffBean): Boolean {
        return oldItem.content == newItem.content

        //                && oldItem.getContent().equals(newItem.getContent())
//                && oldItem.getDate().equals(newItem.getDate());
    }

    /**
     * 可选实现
     * 如果需要精确修改某一个view中的内容，请实现此方法。
     * 如果不实现此方法，或者返回null，将会直接刷新整个item。
     *
     * @param oldItem Old data
     * @param newItem New data
     * @return Payload info. if return null, the entire item will be refreshed.
     */
    override fun getChangePayload(oldItem: DemoDiffBean, newItem: DemoDiffBean): Any? {
        return null
    }
}