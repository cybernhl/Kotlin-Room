package com.guadou.kt_demo.ui.demo1

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.BaseActivity
import com.guadou.lib_baselib.base.BaseViewModel
import com.guadou.lib_baselib.ext.addFragment
import com.guadou.lib_baselib.ext.commContext
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * 加载空的fragment
 */
class EmptyFragmentActivity : BaseActivity<BaseViewModel>() {

    private var mType: Int = 0

    companion object {
        fun startInstance(type: Int) {
            commContext().let {
                it.startActivity(Intent(it, EmptyFragmentActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    putExtra("type", type)
                })
            }
        }
    }

    override fun getDataFromIntent(intent: Intent) {
        mType = intent.getIntExtra("type", 0)
    }

    override fun initVM(): BaseViewModel = getViewModel()


    override fun inflateLayoutById(): Int = R.layout.activity_empty_fragment


    override fun startObserve() {

    }

    /**
     * 注意查看扩展方法的添加fragment
     */
    override fun init() {

        when (mType) {
            1 -> addFragment(R.id.fl_content, JumpLoadingFragment.obtainFragment())
            2 -> addFragment(R.id.fl_content, NormalLoadingFragment.obtainFragment())
            3 -> addFragment(R.id.fl_content, PlaceHolderLoadingFragment.obtainFragment())
            else -> addFragment(R.id.fl_content, JumpLoadingFragment.obtainFragment())
        }


    }

}