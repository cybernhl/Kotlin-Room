package com.guadou.kt_demo.demo.demo8_recyclerview.rv2

import android.content.Intent
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvNormalBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.*
import com.luck.picture.lib.decoration.GridSpacingItemDecoration


/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
class DemoRVNormalGridActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoRvNormalBinding>() {

    private val mAdapter by lazy { BaseDataBindingAdapter<String>(R.layout.item_local_image, BR.text) }

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVNormalGridActivity::class.java).apply {
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

        val datas = listOf(
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/wwmlZgkElgETb4rslWRnEnPD9o6MzrkL8AWLjCgC.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/cXRkXxth9SZA9Zzsx7DcEFvWKQ0mfPkUkRlwHKSZ.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/7e4Te4PsxIBEGSPqQODEbgyVBihd4vzpOzGMGVom.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/LkVTsegs2wMSWiQ1PGAeo8iY1N47Yhx3vcX9JsEO.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/GJHJKd0H1a8fzBdxYrCyP0TZwX3C84gAYq09rsfv.png",
            "http://yyjobs-admin-dev.guabean.com/storage/202006/23/glRhS2exDmTMJR5Bgnf3ZGxPPXNxFMy5u1AUaav5.png",
            "https://i01piccdn.sogoucdn.com/5f7f3dcff67f89c0"
        )

        //使用RecyclerView的扩展方法
//        recyclerView.vertical(3)
//            .bindData(datas, R.layout.item_local_image) { holder, t, _ ->
//                holder.getView<ImageView>(R.id.iv_img).extLoad(t, R.drawable.home_list_plachholder, isCenterCrop = true)
//            }
//            .addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))

        //使用DataBinding的方式
        mBinding.recyclerView.vertical(3).apply {
            adapter = mAdapter
            addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))
        }
        mAdapter.addData(datas)

    }
}