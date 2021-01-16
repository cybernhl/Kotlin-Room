package com.guadou.kt_demo.demo.demo8_recyclerview.rv3

import android.content.Intent
import android.graphics.Color
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.CommUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo_rv_normal.*


/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
@AndroidEntryPoint
class DemoRVHeadFoodVertalActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVHeadFoodVertalActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }


    override fun inflateLayoutById(): Int = R.layout.activity_demo_rv_normal

    override fun startObserve() {

    }

    override fun init() {

        val datas = listOf("关羽", "刘备", "张飞", "吕布", "刘邦", "鲁班", "赵云", "韩信", "孙策")

        recyclerView.vertical()
            .bindData(datas, R.layout.item_vertal_text) { holder, t, _ ->
                holder.setText(R.id.tv_vertal_text, t)
            }
            .divider(Color.BLACK)
            .addHeader(CommUtils.inflate(R.layout.item_vertal_header))
            .addFooter(CommUtils.inflate(R.layout.item_vertal_fooder))
            .addFooter(CommUtils.inflate(R.layout.item_vertal_fooder))

    }
}