package com.guadou.kt_demo.ui.demo8.rv4.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.guadou.kt_demo.ui.demo8.rv4.adapter.DemoJobAdapter
import com.guadou.kt_demo.ui.demo8.rv4.bean.FullJobsPage
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.checkNet
import com.guadou.lib_baselib.ext.toastError
import com.guadou.lib_baselib.utils.CheckUtil


/**
 * Repository一定要通过默认的构造方法传入进来
 * 在DI中注册之后会自动加载
 */
class DemoJobViewModel(private val mRepository: DemoJobRepository) : BaseViewModel() {

    var mCurPage = 1
    private var isNeedPlaceHolder = true
    var isNeedCleanAllData = true
    var mDatas = mutableListOf<FullJobsPage.FullJobs>()
    var mAdapter = DemoJobAdapter(mDatas)

    fun getAllJobs(): LiveData<Boolean> {

        val liveData = MutableLiveData<Boolean>()

        //检查网络的状态，可选用
        checkNet({

            //默认执行在主线程的协程-必须用（可选择默认执行在IO线程的协程）
            launchOnUI {

                //开始Loading
                if (isNeedPlaceHolder) loadStartProgress()

                val jobs = mRepository.getAllJobs(mCurPage)

                //返回的数据是封装过的，检查是否成功
                jobs.checkResult({
                    //成功
                    handleData(it.list)
                    liveData.postValue(true)
                }, {
                    //失败
                    toastError(it)
                    liveData.postValue(false)
                })

                //完成Loading
                loadHideProgress()
            }

        })

        return liveData
    }

    //处理数据-添加或刷新
    private fun handleData(list: List<FullJobsPage.FullJobs>) {
        if (!CheckUtil.isEmpty(list)) {
            //有数据，判断是刷新还是加载更多的数据
            if (isNeedCleanAllData) {
                //刷新的方式
                mDatas.clear()
                mDatas.addAll(list)
                mAdapter.notifyDataSetChanged()
            } else {
                //加载更多
                mDatas.addAll(list)
                mAdapter.notifyItemRangeInserted(mDatas.size - list.size, list.size)
                mAdapter.loadMoreModule.loadMoreComplete()
            }
        } else {
            //展示无数据
            if (isNeedCleanAllData && mCurPage == 1) {
                mDatas.clear()
                mAdapter.notifyDataSetChanged()
                //展示无数据的布局
//                mAdapter.setEmptyView(R.layout.layout_empty_fulltime_interview)
            } else {
                //如果是加载更多,展示加载完成，没有更多数据了
                mAdapter.loadMoreModule.loadMoreEnd()
            }
        }
        isNeedPlaceHolder = false
        isNeedCleanAllData = false
    }

}