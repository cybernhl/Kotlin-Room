package com.guadou.kt_demo.demo.demo8_recyclerview.rv4

import android.content.Intent
import android.graphics.Color
import androidx.lifecycle.Observer
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo8_recyclerview.rv4.mvvm.DemoJobViewModel
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.divider
import com.guadou.lib_baselib.ext.vertical
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_demo_rv_muban.*

/**
 * 网络请求模板
 */
@AndroidEntryPoint
class DemoRVMobanActivity : BaseVMActivity<DemoJobViewModel>(), OnRefreshListener {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVMobanActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_rv_muban

    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initData()
        initListener()

    }

    private fun initRV() {
        //使用RecyclerView的扩展方法
//        recycler_view.vertical().adapter = mViewModel.mAdapter

        //使用DataBinding的方式
        recycler_view.vertical().apply {
            adapter = mViewModel.mAdapter
            divider(Color.BLACK)
        }
    }

    private fun initData() {

        mViewModel.getAllJobs().observe(this, Observer {
            refresh_layout.finishRefresh()
            mViewModel.mAdapter.loadMoreModule.isEnableLoadMore = true
        })

    }

    private fun initListener() {

        //Adapter的滑动监听，监听加载更多
        mViewModel.mAdapter.loadMoreModule.isEnableLoadMore = false
        mViewModel.mAdapter.loadMoreModule.preLoadNumber = 4
        mViewModel.mAdapter.loadMoreModule.setOnLoadMoreListener {
            mViewModel.isNeedCleanAllData = false
            mViewModel.mCurPage++
            initData()
        }

        //刷新控件初始化
        refresh_layout.setEnableNestedScroll(true)
        refresh_layout.setEnableLoadMore(false)
        refresh_layout.setEnableRefresh(true)
        refresh_layout.setOnRefreshListener(this)
    }


    override fun onRefresh(refreshLayout: RefreshLayout) {
        mViewModel.isNeedCleanAllData = true
        mViewModel.mAdapter.loadMoreModule.loadMoreComplete()
        mViewModel.mAdapter.loadMoreModule.isEnableLoadMore = false
        //直接调用，参数从成员变量中获取
        mViewModel.mCurPage = 1
        initData()
    }

}