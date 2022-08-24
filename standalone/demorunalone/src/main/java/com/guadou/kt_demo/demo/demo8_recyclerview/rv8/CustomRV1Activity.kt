package com.guadou.kt_demo.demo.demo8_recyclerview.rv8

import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvNormalBinding
import com.guadou.kt_demo.demo.demo8_recyclerview.base.WarpLoadAdapter
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext

import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.vertical
import com.guadou.lib_baselib.utils.CommUtils

/**
 * 自定义适配器的测试
 */
class CustomRV1Activity : BaseVDBActivity<EmptyViewModel, ActivityDemoRvNormalBinding>() {


    private val mAdapter = Job2Adapter(mutableListOf<MoreJob>())

    private val mLoadAdapter = WarpLoadAdapter(mAdapter)

//    private val mLoadMoreAdapter = LoadMoreAdapter(mAdapter)
//    private val wrapAdapter = WrapRVAdapter(mAdapter)

    companion object {
        fun startInstance() {
            commContext().gotoActivity<CustomRV1Activity>()
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_rv_normal)
    }

    override fun startObserve() {

    }

    override fun init() {
//        wrapAdapter.addHeadView(CommUtils.inflate(R.layout.item_vertal_header))
//        wrapAdapter.addFootView(CommUtils.inflate(R.layout.item_vertal_fooder))

        mLoadAdapter.addHeadView(CommUtils.inflate(R.layout.item_vertal_header))
        mLoadAdapter.addFootView(CommUtils.inflate(R.layout.item_vertal_fooder))
        mLoadAdapter.isLoadMoreEnable = true
        mLoadAdapter.setPreLoadNumber(1)
        mLoadAdapter.setOnLoadMoreListener {
            loadMoreData()
        }

        mBinding.recyclerView.vertical().adapter = mLoadAdapter

        initData()
    }

    private fun loadMoreData() {
        //模拟网络请求
        CommUtils.getHandler().postDelayed({

            mAdapter.addDataList(
                listOf(
                    MoreJob(1, "孙权"),
                    MoreJob(1, "孙策"),
                    MoreJob(1, "孙尚香"),
                    MoreJob(1, "黄盖"),
                    MoreJob(1, "程普")
                )
            )

            if (mAdapter.dataList.size > 20) {
                mLoadAdapter.loadMoreEnd()
            } else {
                mLoadAdapter.loadMoreSuccess()
            }

        }, 500)

    }

    private fun initData() {

        val newList = listOf(
            MoreJob(1, "刘备"),
            MoreJob(1, "张飞"),
            MoreJob(2, "曹操"),
            MoreJob(1, "赵云"),
            MoreJob(2, "曹仁"),
            MoreJob(1, "马超"),
            MoreJob(1, "黄忠"),
            MoreJob(1, "诸葛亮"),
            MoreJob(2, "夏侯惇"),
            MoreJob(1, "姜维"),
            MoreJob(1, "王平"),
            MoreJob(2, "徐晃"),
            MoreJob(1, "魏延"),
            MoreJob(2, "张辽"),
            MoreJob(2, "曹真"),
            MoreJob(1, "刘禅"),
            MoreJob(1, "张苞"),
            MoreJob(2, "司马懿"),
        )

        mAdapter.addDataList(newList)

    }

}