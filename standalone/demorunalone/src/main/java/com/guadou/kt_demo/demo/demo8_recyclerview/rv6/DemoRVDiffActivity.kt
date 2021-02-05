package com.guadou.kt_demo.demo.demo8_recyclerview.rv6

import android.content.Intent
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvDiffBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.vertical
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


/**
 * 差分刷新
 */
@AndroidEntryPoint
class DemoRVDiffActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoRvDiffBinding>() {

    private val mDatas = mutableListOf<DemoDiffBean>()

    //        private val mAdapter by lazy { BaseDataBindingAdapter(R.layout.item_diff_jobs, BR.item, mDatas) }
    @Inject
    lateinit var mAdapter: DemoDiffAdapter

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVDiffActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_rv_diff)
            .addBindingParams(BR.click, ClickProxy())
    }

    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initData()
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
        mBinding.recyclerView.vertical().adapter = mAdapter


        //使用DataBinding的方式
//        mAdapter.setDiffCallback(DiffDemoCallback())
//        recyclerView.vertical().apply {
//            adapter = mAdapter
//            divider(Color.BLACK)
//        }
    }


    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        //差分刷新效果点击
        //一定要注意 设置差分数据的时候 不要是一个内存地址的，你copy一份集合出来再设置都行
        fun handleDiff1() {

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

        fun handleDiff2() {
            //局部替换
            //如果不替换内存地址 直接删减 直接notifyDataSetChanged得了
            mAdapter.data[0].content = "Diff 2 content:0"
            mAdapter.data.removeAt(1)

            mAdapter.notifyDataSetChanged()
        }

        fun handleDiff3() {

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