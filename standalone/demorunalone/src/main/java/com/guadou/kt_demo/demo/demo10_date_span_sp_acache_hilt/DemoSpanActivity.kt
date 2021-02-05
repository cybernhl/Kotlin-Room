package com.guadou.kt_demo.demo.demo10_date_span_sp_acache_hilt

import android.content.Intent
import android.graphics.Color
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoSpanBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.font_text_view.TypefaceUtil
import com.guadou.lib_baselib.utils.Log.YYLogUtils

import dagger.hilt.android.AndroidEntryPoint

import javax.inject.Inject

/**
 * Span的展示的逻辑
 */
@AndroidEntryPoint
class DemoSpanActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoSpanBinding>() {

    @Inject
    lateinit var userServer: UserServer

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoSpanActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_span)
    }

    override fun startObserve() {

    }

    override fun init() {
        //测试Hilt
        YYLogUtils.w("server:" + userServer.toString() + "Dao:"+userServer.getDaoContent())


        //可以直接操作TextView,如果没有文本可以直接添加带Span的文本
        mBinding.tvTextSpan1.text = "演示一下appendXX方法的用法\n"
        mBinding.tvTextSpan1.appendSizeSpan("变大变大", 1.5f)
            .appendColorSpan("我要变色", color = Color.parseColor("#f0aafc"))
            .appendBackgroundColorSpan("我是有底色的", color = Color.parseColor("#cacee0"))
            .appendStrikeThrougthSpan("添加删除线哦哦哦哦")
            .appendClickSpan("来点我一下试试啊", isUnderlineText = true, clickAction = {
                toast("哎呀，您点到我了呢，嘿嘿")
            })
            .appendImageSpan(R.mipmap.ic_launcher)  //默认的大图什么都不加
            .appendStyleSpan("我是粗体的")
            .appendImageSpan(R.mipmap.ic_launcher_round, 4, width = dp2px(35f), height = dp2px(35f))  //居中的,限制Drawable
            .appendCustomTypeFaceSpan("Xiao mi Hua wei", TypefaceUtil.getSFFlower(mActivity))
            .appendImageSpan(
                R.mipmap.iv_me_red_packet,
                maginLeft = dp2px(10f),
                marginRight = dp2px(10f)
            )  //默认底部对齐，加margin
            .appendStrikeThrougthSpan("添加删除线哦哦哦哦添加删除线哦哦哦哦")


        //如果已经有了文本，可以对现有的文本进行区间内部的修改
        mBinding.tvTextSpan2.text = "我是文本的测试，这是固定的，可以变大，可以点击，可以通过区间设置不同的效果,One Plus,我是图片，看看效果"
        mBinding.tvTextSpan2.colorSpan("", 0..7, Color.BLUE)   //0-6变蓝色
        mBinding.tvTextSpan2.backgroundColorSpan("", 5..14, Color.GRAY) //变色背景
        mBinding.tvTextSpan2.sizeSpan("", 14..18, 1.3f)
        mBinding.tvTextSpan2.clickSpan("", 18..23, isUnderlineText = true, clickAction = { toast("tusi") })
        mBinding.tvTextSpan2.styleSpan("", 23..38)
        mBinding.tvTextSpan2.customTypeFaceSpan("", 38..46, TypefaceUtil.getSFFlower(mActivity))
        //已有的一个文本变为图片
        mBinding.tvTextSpan2.imagepan(R.mipmap.ic_launcher_round, 4, range = 47..51, width = dp2px(35f), height = dp2px(35f))


        //直接改已存在的String
        val str = "I want change custom type face 1"
        var strSpan = str.toCustomTypeFaceSpan(TypefaceUtil.getSFFlower(mActivity), 14..30)
        strSpan = strSpan.toSizeSpan(0..20, 1.4f)
        //指定的文本换成图片
        strSpan = strSpan.toImageSpan(R.mipmap.iv_me_red_packet, 31..32)

        mBinding.tvTextSpan3.text = strSpan
    }

}