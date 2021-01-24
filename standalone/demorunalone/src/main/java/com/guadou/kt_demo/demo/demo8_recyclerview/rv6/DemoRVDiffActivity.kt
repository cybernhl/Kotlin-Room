package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import android.content.Intent
import android.graphics.Color
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.divider
import com.guadou.lib_baselib.ext.vertical
import kotlinx.android.synthetic.main.activity_demo_rv_diff.*
import kotlinx.android.synthetic.main.activity_demo_rv_diff.recyclerView
import kotlinx.android.synthetic.main.activity_demo_rv_normal.*

/**
 * 差分刷新
 */
class DemoRVDiffActivity : BaseVMActivity<EmptyViewModel>() {

    private val mDatas = mutableListOf<DemoDiffBean>()

    //        private val mAdapter by lazy { BaseDataBindingAdapter(R.layout.item_diff_jobs, BR.item, mDatas) }
    private val mAdapter: DemoDiffAdapter by lazy { DemoDiffAdapter(mDatas) }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVDiffActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_rv_diff

    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initData()
        initListener()
    }

    private fun initData() {
        for (i in 1..10) {
            mDatas.add(DemoDiffBean(i, "conetnt:$i"))
        }

        mAdapter.setDiffNewData(mDatas)
    }

    private fun initRV() {
        //设置BRVAH
        mAdapter.setDiffCallback(DiffDemoCallback())
        recyclerView.vertical().adapter = mAdapter


        //使用DataBinding的方式
//        mAdapter.setDiffCallback(DiffDemoCallback())
//        recyclerView.vertical().apply {
//            adapter = mAdapter
//            divider(Color.BLACK)
//        }
    }

    //差分刷新效果点击
    //一定要注意 设置差分数据的时候 不要是一个内存地址的，你copy一份集合出来再设置都行
    private fun initListener() {

        btn_diff_1.click {

            //全部替换数据-数据地址变换了
            val list = mutableListOf<DemoDiffBean>()
            for (i in 1..10) {
                list.add(DemoDiffBean(i, "Diff1 conetnt:$i"))
            }

            mAdapter.setDiffNewData(list)


            //错误示范 - 不替换内存地址
            //全部替换数据-数据地址不变这样不行的
//            mDatas.clear()
//            for (i in 1..10) {
//                mDatas.add(DemoDiffBean(i, "Diff2 conetnt:$i"))
//            }
//
//            mAdapter.setDiffNewData(mDatas)
        }

        btn_diff_2.click {
            //局部替换
            //如果不替换内存地址 直接删减 直接notifyDataSetChanged得了
            mAdapter.data[0].content = "Diff 2 content:0"
            mAdapter.data.removeAt(1)

            mAdapter.notifyDataSetChanged()
        }

        btn_diff_3.click {

            val list = mutableListOf<DemoDiffBean>()
            for (i in 1..10) {
                list.add(DemoDiffBean(i, "Diff3 conetnt:$i"))
            }
            list.removeAt(0)
            list.removeAt(1)
            list.removeAt(2)
            list[3].content = "自定义乱改的数据"

            mAdapter.setDiffNewData(list)

        }
    }


}