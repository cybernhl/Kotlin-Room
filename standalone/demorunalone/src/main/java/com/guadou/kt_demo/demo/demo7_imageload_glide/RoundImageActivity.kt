package com.guadou.kt_demo.demo.demo7_imageload_glide

import android.widget.ImageView
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.view.RoundCircleImageView
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.engine.extLoad
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.view.CircleImageView


class RoundImageActivity : BaseVMActivity<EmptyViewModel>() {

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RoundImageActivity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_round_image


    override fun startObserve() {

    }

    override fun init() {

        val imgUrl = "http://i01piccdn.sogoucdn.com/7f62e43d48cb78d9"
//        val imgUrl = "123"

        findViewById<CircleImageView>(R.id.iv_circle_img).background = drawable(R.drawable.shape_blue)
        findViewById<CircleImageView>(R.id.iv_circle_img).extLoad(imgUrl, R.drawable.test_img_placeholder)

        findViewById<ImageView>(R.id.iv_round_clip_path).background = drawable(R.drawable.shape_blue)   //不错
        findViewById<ImageView>(R.id.iv_round_clip_path).extLoad(imgUrl, R.drawable.test_img_placeholder)

        findViewById<ImageView>(R.id.iv_round_xfermode).background = drawable(R.drawable.shape_blue)   //完全就不能看
        findViewById<ImageView>(R.id.iv_round_xfermode).extLoad(imgUrl, R.drawable.test_img_placeholder)

        findViewById<ImageView>(R.id.iv_round_shared).background = drawable(R.drawable.shape_blue)  //丑，需要优化
        findViewById<ImageView>(R.id.iv_round_shared).extLoad(imgUrl, R.drawable.test_img_placeholder)

//        findViewById<RoundCircleImageView>(R.id.iv_custom_round).background = drawable(R.drawable.shape_blue)
//        findViewById<RoundCircleImageView>(R.id.iv_custom_round).extLoad(imgUrl, R.drawable.test_img_placeholder)

//        findViewById<RoundCircleImageView>(R.id.iv_custom_round2).background = drawable(R.drawable.chengxiao)
//        findViewById<RoundCircleImageView>(R.id.iv_custom_round2).extLoad(imgUrl, R.drawable.test_img_placeholder)

        findViewById<ImageView>(R.id.iv_radius).background = drawable(R.drawable.shape_blue)
        findViewById<ImageView>(R.id.iv_radius).extLoad(imgUrl, R.drawable.test_img_placeholder)

        findViewById<ImageView>(R.id.iv_glide).background = drawable(R.drawable.shape_blue)
        findViewById<ImageView>(R.id.iv_glide).extLoad(
            imgUrl, R.drawable.test_img_placeholder, isCenterCrop = true,
            topLeftRadius = dp2px(40f).toFloat(),
            topRightRadius = dp2px(20f).toFloat(),
            bottomLeftRadius = dp2px(20f).toFloat(),
            bottomRightRadius = dp2px(40f).toFloat()
        )

        findViewById<ImageView>(R.id.iv_glide2).background = drawable(R.drawable.shape_blue)
        findViewById<ImageView>(R.id.iv_glide2).extLoad(imgUrl, R.drawable.test_img_placeholder)

    }
}