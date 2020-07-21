package com.guadou.kt_demo.ui

import android.annotation.SuppressLint
import com.guadou.kt_demo.R
import com.guadou.kt_demo.ui.demo1.Demo1Activity
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.click
import kotlinx.android.synthetic.main.activity_demo_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 演示Demo的首页
 */
class DemoMainActivity : BaseActivity<BaseViewModel>() {

    override fun initVM(): BaseViewModel = getViewModel()

    override fun inflateLayoutById(): Int = R.layout.activity_demo_main

    override fun startObserve() {
    }

    @SuppressLint("SetTextI18n")
    override fun init() {

        btn_demo_1.text = "1.基本Activity/Fragment的使用（占位图1）"
        btn_demo_1.click {
            Demo1Activity.startInstance()
        }

        btn_demo_2.text = "2.ViewPager/LazyFragment（占位图2）"
        btn_demo_2.click {

        }

        btn_demo_3.text = "3.BottomTab/LazyFragment"
        btn_demo_3.click {

        }

        btn_demo_4.text = "4.吐司/弹窗/banner"
        btn_demo_4.click {

        }

        btn_demo_5.text = "5.网络请求（带去重策略）"
        btn_demo_5.click {

        }

        btn_demo_6.text = "6.权限/图库/相机"
        btn_demo_6.click {

        }

        btn_demo_7.text = "7.图片加载Glide封装"
        btn_demo_7.click {

        }

        btn_demo_8.text = "8.RecyclerView封装"
        btn_demo_8.click {

        }

        btn_demo_9.text = "9.Kotlin倒计时"
        btn_demo_9.click {

        }

        btn_demo_10.text = "10.无界面功能（SP，ACache, String, Span, 时间格式化等）"
        btn_demo_10.click {

        }
    }

}
