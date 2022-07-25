package com.guadou.cs_cptservices.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.guadou.cs_cptservices.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.BaseViewModel
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CheckUtil
import com.guadou.lib_baselib.utils.CommUtils
import com.guadou.lib_baselib.view.MyWebView
import kotlinx.android.synthetic.main.activity_global_web.*

class GlobalWebActivity : BaseVMActivity<BaseViewModel>() {

    private var mWebtitle: String? = null
    private var mWeburl: String? = null
    private var mWebView: MyWebView? = null

    companion object {
        fun startInstance(webTitle: String, webUrl: String) {
            CommUtils.getContext().let {
                it.startActivity(Intent(it, GlobalWebActivity::class.java).apply {
                    putExtra("webTitle", webTitle)
                    putExtra("webUrl", webUrl)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataFromIntent(intent: Intent) {
        super.getDataFromIntent(intent)
        mWebtitle = intent.getStringExtra("webTitle")
        mWeburl = intent.getStringExtra("webUrl")
    }

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_global_web
    }

    override fun startObserve() {
    }

    override fun init() {
        initTitles()
        initWeb()
    }

    private fun initTitles() {
        if (!CheckUtil.isEmpty(mWebtitle)) {
            easy_title.setTitle(mWebtitle)
        }
    }

    @SuppressLint("AddJavascriptInterface")
    private fun initWeb() {
        val params = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        mWebView = MyWebView(applicationContext)
        mWebView!!.layoutParams = params
        mWebView!!.setOnWebChangeListener(object : MyWebView.OnWebChangeListener {
            override fun titleChange(title: String) {
                if (CheckUtil.isEmpty(mWebtitle)) {
                    easy_title.setTitle(mWebtitle)
                }
            }

            override fun progressChange(progress: Int) {
                var newProgress = progress
                if (newProgress == 100) {
                    pb_web_view.setProgress(100)
                    CommUtils.getHandler()
                        .postDelayed({ pb_web_view.visibility = View.GONE }, 200)//0.2秒后隐藏进度条
                } else if (pb_web_view.visibility == View.GONE) {
                    pb_web_view.visibility = View.VISIBLE
                }
                //设置初始进度10，这样会显得效果真一点，总不能从1开始吧
                if (newProgress < 10) {
                    newProgress = 10
                }
                //不断更新进度
                pb_web_view.setProgress(newProgress)
            }

            override fun onInnerLinkChecked() {

            }

            override fun onWebLoadError() {
                toast("Load Error")
            }
        })

        if (!TextUtils.isEmpty(mWeburl))
            mWebView!!.loadUrl(mWeburl!!)

        fl_content.addView(mWebView)

    }


    /**
     * 集成了系统的回退和网页的回退
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView!!.canGoBack()) {
            mWebView!!.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onPause() {
        super.onPause()
        if (mWebView != null) {
            mWebView!!.onPause()
        }
    }

    override fun onResume() {
        super.onResume()
        if (mWebView != null) {
            mWebView!!.onResume()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mWebView != null) {
            mWebView!!.clearCache(true) //清空缓存
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {

                fl_content.removeView(mWebView)

                mWebView?.removeAllViews()
                mWebView?.destroy()
            } else {
                mWebView?.removeAllViews()
                mWebView?.destroy()

                fl_content.removeView(mWebView)

            }
            mWebView = null
        }

        //退出当前web进程
        //        System.exit(0);
    }


}
