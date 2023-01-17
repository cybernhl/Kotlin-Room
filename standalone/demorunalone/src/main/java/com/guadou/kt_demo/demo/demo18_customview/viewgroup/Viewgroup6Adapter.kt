package com.guadou.kt_demo.demo.demo18_customview.viewgroup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo18_customview.viewgroup.viewgroup6.adapter.CurtainAdapter
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.toast

/**
 * 测试的数据适配器
 */
class Viewgroup6Adapter(private val data: List<String>) : CurtainAdapter() {

    override fun getItemCount(): Int = data.size

    override fun getItemViewType(position: Int): Int {
        return if (position == 1 || position == 8) 5 else 6
    }

    override fun onCreateItemView(context: Context, parent: ViewGroup, itemType: Int): View {
        return if (itemType == 5) {
            LayoutInflater.from(context).inflate(R.layout.item_img_group6_2, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_img_group6, parent, false)
        }
    }

    override fun onBindItemView(itemView: View, itemType: Int, position: Int) {

        val imgageView = itemView.findViewById<ImageView>(R.id.iv_img)
        imgageView.extLoad(data[position])

        imgageView.click {
            toast("当前点击的是第 $position 个索引")
        }

    }


}