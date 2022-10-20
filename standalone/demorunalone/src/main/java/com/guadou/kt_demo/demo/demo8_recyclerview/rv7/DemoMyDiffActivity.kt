package com.guadou.kt_demo.demo.demo8_recyclerview.rv7

import android.content.Intent
import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.diff.BrvahAsyncDifferConfig
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvDiffBinding
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.DemoDiffAdapter
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.DemoDiffBean
import com.guadou.kt_demo.demo.demo8_recyclerview.rv6.DiffDemoCallback
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.divider
import com.guadou.lib_baselib.ext.vertical
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors.newCachedThreadPool
import javax.inject.Inject


/**
 * 差分刷新
 */
class DemoMyDiffActivity : BaseVMActivity<EmptyViewModel>() {

    val mDatas = arrayListOf<DemoDiffBean>()
    lateinit var mAdapter: MyDiffAdapter

    override fun getLayoutIdRes(): Int = R.layout.activity_demo_my_diff

    override fun startObserve() {

    }

    override fun init() {

        initRV()
        initData()
        initListener()
    }

    private fun initListener() {

        findViewById<View>(R.id.diff_1).click {
            val list = mutableListOf<DemoDiffBean>()
            for (i in 1..10) {
                list.add(DemoDiffBean(i, "Diff1 conetnt:$i"))
            }

            mAdapter.setDiffNewData(list)
        }

        findViewById<View>(R.id.diff_2).click {

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

    private fun initData() {
        mDatas.clear()
        for (i in 1..10) {
            mDatas.add(DemoDiffBean(i, "conetnt:$i"))
        }

        mAdapter.setDiffNewData(mDatas)
    }

    private fun initRV() {

        mAdapter = MyDiffAdapter()

        findViewById<RecyclerView>(R.id.recyclerView).vertical().apply {
            adapter = mAdapter
            divider(Color.BLACK)
        }

    }


}