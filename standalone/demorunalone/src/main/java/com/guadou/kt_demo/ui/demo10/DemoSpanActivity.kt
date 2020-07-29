package com.guadou.kt_demo.ui.demo10

import android.content.Intent
import android.graphics.Color
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.font_text_view.TypefaceUtil
import kotlinx.android.synthetic.main.activity_demo_span.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Span的展示的逻辑
 */
class DemoSpanActivity : BaseActivity<BaseViewModel>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoSpanActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo_span

    override fun startObserve() {

    }

    override fun init() {
        //可以直接操作TextView,如果没有文本可以直接添加带Span的文本
        tv_text_span1.text = "演示一下appendXX方法的用法\n"
        tv_text_span1.appendSizeSpan("变大变大", 1.5f)
            .appendColorSpan("我要变色", color = Color.parseColor("#f0aafc"))
            .appendBackgroundColorSpan("我是有底色的", color = Color.parseColor("#cacee0"))
            .appendStrikeThrougthSpan("添加删除线哦哦哦哦")
            .appendClickSpan("来点我一下试试啊", isUnderlineText = true, clickAction = {
                toast("哎呀，您点到我了呢，嘿嘿")
            })
            .appendStyleSpan("我是粗体的")
            .appendCustomTypeFaceSpan("Xiao mi Hua wei", TypefaceUtil.getSFFlower(mActivity))


        //如果已经有了文本，可以对现有的文本进行区间内部的修改
        tv_text_span2.text = "我是文本的测试，这是固定的，可以变大，可以点击，可以通过区间设置不同的效果,One Plus"
        tv_text_span2.colorSpan("", 0..7, Color.BLUE)   //0-6变蓝色
        tv_text_span2.backgroundColorSpan("", 5..14, Color.GRAY) //变色背景
        tv_text_span2.sizeSpan("", 14..18, 1.3f)
        tv_text_span2.clickSpan("", 18..23, isUnderlineText = true, clickAction = { toast("tusi") })
        tv_text_span2.styleSpan("", 23..38)
        tv_text_span2.customTypeFaceSpan("", 38..46, TypefaceUtil.getSFFlower(mActivity))


        //直接改S已存在的String
        val str = "i want change custom type face"
        var strSpan = str.toCustomTypeFaceSpan(TypefaceUtil.getSFFlower(mActivity), 14..30)
        strSpan = strSpan.toSizeSpan(0..20, 1.4f)
        tv_text_span3.text = strSpan
    }
}