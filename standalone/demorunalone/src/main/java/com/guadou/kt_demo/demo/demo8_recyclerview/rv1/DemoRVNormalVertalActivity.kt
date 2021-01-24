package com.guadou.kt_demo.demo.demo8_recyclerview.rv1

import android.content.Intent
import android.graphics.Color
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import kotlinx.android.synthetic.main.activity_demo_rv_normal.*

/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
class DemoRVNormalVertalActivity : BaseVMActivity<EmptyViewModel>() {

    private val mAdapter by lazy { BaseDataBindingAdapter<String>(R.layout.item_vertal_text, BR.text) }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVNormalVertalActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_rv_normal


    override fun startObserve() {

    }

    override fun init() {

        val datas = listOf("关羽", "刘备", "张飞", "吕布", "刘邦", "鲁班", "赵云", "韩信", "孙策")

        //使用RecyclerView的扩展方法
//        recyclerView.vertical()
//            .bindData(datas, R.layout.item_vertal_text) { holder, t, _ ->
//                holder.setText(R.id.tv_vertal_text, t)
//            }
//            .divider(Color.BLACK)

        //使用DataBinding的方式
        recyclerView.vertical().apply {
            adapter = mAdapter
            divider(Color.BLACK)
        }
        mAdapter.addData(datas)

        //测试局部刷新
        easy_title.addRightText("Refresh") {
            mAdapter.data[0] = "关平"
            mAdapter.notifyItemChanged(0)
        }

    }
}