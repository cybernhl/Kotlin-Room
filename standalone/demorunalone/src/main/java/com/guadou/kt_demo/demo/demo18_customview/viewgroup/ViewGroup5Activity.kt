package com.guadou.kt_demo.demo.demo18_customview.viewgroup


import androidx.recyclerview.widget.RecyclerView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.*
import com.lxj.easyadapter.ViewHolder

class ViewGroup5Activity : BaseVMActivity<EmptyViewModel>() {

    val list = arrayListOf<String>()

    companion object {
        fun startInstance() {
            commContext().gotoActivity<ViewGroup5Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_viewgroup5

    override fun startObserve() {

    }

    override fun init() {

        for (i in 1..10) {
            list.add(i.toString())
        }

        val group5 = findViewById<ViewGroup5>(R.id.viewgroup5)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)

        recyclerView.horizontal().bindData(list, R.layout.item_scroll_card) { holder: ViewHolder, t: String, position: Int ->

            holder.setText(R.id.tv_name, "测试数据 $t")
        }

        group5.setOnShowMoreListener {
            toast("进入更多的页面")
        }


        findViewById<Xianyu5>(R.id.xianyu5).setOnShowMoreListener {
            toast("进入详情页面")
        }

    }

}