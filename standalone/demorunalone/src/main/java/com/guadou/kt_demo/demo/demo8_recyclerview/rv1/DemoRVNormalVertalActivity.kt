package com.guadou.kt_demo.demo.demo8_recyclerview.rv1

import android.content.Intent
import android.graphics.Color
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvNormalBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.bindData
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.divider
import com.guadou.lib_baselib.ext.vertical


/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
class DemoRVNormalVertalActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoRvNormalBinding>() {

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

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_rv_normal)
    }

    override fun startObserve() {

    }

    override fun init() {

        val datas = listOf("关羽", "刘备", "张飞", "吕布", "刘邦", "鲁班", "赵云", "韩信", "孙策")

        //使用RecyclerView的扩展方法
        mBinding.recyclerView.vertical()
            .bindData(datas, R.layout.rl_03_layout) { holder, t, _ ->

            }
            .divider(Color.BLACK)

        //使用DataBinding的方式
//        mBinding.recyclerView.vertical().apply {
//            adapter = mAdapter
//            divider(Color.BLACK)
//        }
//        mAdapter.addData(datas)

        //测试局部刷新
        mBinding.easyTitle.addRightText("Refresh") {
            mAdapter.data[0] = "关平"
            mAdapter.notifyItemChanged(0)
        }

    }
}