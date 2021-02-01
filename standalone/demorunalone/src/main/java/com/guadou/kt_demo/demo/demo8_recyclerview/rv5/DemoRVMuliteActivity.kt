package com.guadou.kt_demo.demo.demo8_recyclerview.rv5

import android.content.Intent
import android.graphics.Color
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvNormalBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.divider
import com.guadou.lib_baselib.ext.vertical


/**
 * 多布局
 */
class DemoRVMuliteActivity : BaseVDBActivity<EmptyViewModel,ActivityDemoRvNormalBinding >() {

    val datas = mutableListOf(
        MuliteTestBean("MuliteTestBean1", "", 0),
        MuliteTestBean("MuliteTestBean2", "http://pic73.nipic.com/file/20150723/455997_210818004000_2.jpg", 1),
        MuliteTestBean("MuliteTestBean3", "", 0),
        MuliteTestBean("MuliteTestBean4", "", 0),
        MuliteTestBean("MuliteTestBean5", "http://i02piccdn.sogoucdn.com/438750e61adc26dc", 1),
        MuliteTestBean("MuliteTestBean6", "1.6", 0),
        MuliteTestBean("MuliteTestBean7", "1.7", 0),
        MuliteTestBean("MuliteTestBean8", "http://i03piccdn.sogoucdn.com/965cc2a9ea62fc08", 1),
        MuliteTestBean("MuliteTestBean9", "1.9", 0),
        MuliteTestBean("MuliteTestBean10", "1.1", 0),
        MuliteTestBean("MuliteTestBean11", "http://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0", 1),
        MuliteTestBean("MuliteTestBean12", "1.3", 0)
    )
    private val mAdapter by lazy { DemoMuliteAdapter(BR.item, datas) }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVMuliteActivity::class.java).apply {
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

//        recyclerView.vertical().adapter = DemoMuliteAdapter(datas)

        //使用DataBinding的方式
        mBinding.recyclerView.vertical().apply {
            adapter = mAdapter
            divider(Color.BLACK)
        }
    }
}