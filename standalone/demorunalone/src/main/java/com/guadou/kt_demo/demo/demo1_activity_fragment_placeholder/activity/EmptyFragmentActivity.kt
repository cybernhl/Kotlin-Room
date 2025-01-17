package com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.activity

import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.fragment.JumpLoadingFragment
import com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.fragment.PlaceHolderLoadingFragment
import com.guadou.kt_demo.demo.demo1_activity_fragment_placeholder.fragment.RoteLoadingFragment
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.addFragment
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import dagger.hilt.android.AndroidEntryPoint


/**
 * 加载空的fragment
 * (忽略xml的databing生成)
 */
@AndroidEntryPoint
class EmptyFragmentActivity : BaseVMActivity<EmptyViewModel>() {

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

    override fun getLayoutIdRes(): Int = R.layout.activity_empty_fragment


    override fun startObserve() {

    }

    /**
     * 注意查看扩展方法的添加fragment
     */
    override fun init() {
        toast("ViewModel: $mViewModel")

        when (mType) {
            1 -> addFragment(R.id.fl_content,
                JumpLoadingFragment.obtainFragment()
            )
            2 -> addFragment(R.id.fl_content,
                RoteLoadingFragment.obtainFragment()
            )
            3 -> addFragment(R.id.fl_content,
                PlaceHolderLoadingFragment.obtainFragment()
            )
            else -> addFragment(R.id.fl_content,
                JumpLoadingFragment.obtainFragment()
            )
        }


    }

}