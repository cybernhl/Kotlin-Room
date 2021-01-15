package com.guadou.kt_demo.demo.demo8_recyclerview.rv2

import android.content.Intent
import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.EmptyViewModel
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.ext.bindData
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.dp2px
import com.guadou.lib_baselib.ext.vertical
import com.luck.picture.lib.decoration.GridSpacingItemDecoration
import kotlinx.android.synthetic.main.activity_demo_rv_normal.*


/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
class DemoRVNormalGridActivity : BaseActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVNormalGridActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun inflateLayoutById(): Int = R.layout.activity_demo_rv_normal


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

        //默认的Grid
        recyclerView.vertical(3)
            .bindData(datas, R.layout.item_local_image) { holder, t, _ ->
                holder.getView<ImageView>(R.id.iv_img).extLoad(t, R.mipmap.home_list_plachholder, isCenterCrop = true)
            }
            .addItemDecoration(GridSpacingItemDecoration(3, dp2px(10f), true))

    }
}