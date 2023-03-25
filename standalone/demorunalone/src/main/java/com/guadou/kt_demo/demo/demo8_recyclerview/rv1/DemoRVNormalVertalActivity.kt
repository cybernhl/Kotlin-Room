package com.guadou.kt_demo.demo.demo8_recyclerview.rv1

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.guadou.cs_cptservices.binding.BaseDataBindingAdapter
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemoRvNormalBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.*
import com.guadou.lib_baselib.utils.log.YYLogUtils


/**
 * 普通的垂直的或者水平的直接用扩展的方法
 */
class DemoRVNormalVertalActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoRvNormalBinding>() {

    private val mAdapter by lazy { BaseDataBindingAdapter<String>(R.layout.item_vertal_text, BR.text) }

    var smoothScrolling = false
    var smoothScrollPosition = -1

    private val bgColors = arrayOf(
        Color.parseColor("#F44336"),
        Color.parseColor("#E91E63"),
        Color.parseColor("#9C27B0"),
        Color.parseColor("#3F51B5"),
        Color.parseColor("#2196F3"),
        Color.parseColor("#03A9F4"),
        Color.parseColor("#00BCD4"),
        Color.parseColor("#009688"),
        Color.parseColor("#4CAF50"),
        Color.parseColor("#CDDC39"),
        Color.parseColor("#FFEB3B"),
        Color.parseColor("#FFC107"),
        Color.parseColor("#FF9800"),
        Color.parseColor("#FF5722"),
        Color.parseColor("#FF00FF")
    )

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoRVNormalVertalActivity::class.java).apply {
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
        val datas = arrayListOf<String>()
        for (i in 0..99) {
            datas.add("Item 内容 $i")
        }

        //使用RecyclerView的扩展方法
        val layoutManager = SmoothLinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        val random = Random()
        mBinding.recyclerView.apply {
            setLayoutManager(layoutManager)
            bindData(datas, R.layout.item_custom_jobs) { holder, t, _ ->
                holder.setText(R.id.tv_job_text, t)
//                holder.getView<View>(R.id.ll_root).setBackgroundColor(bgColors[random.nextInt(bgColors.size)])
            }
            divider(Color.BLACK)
            scrollToPosition(0)
        }

        //测试滚动到底部
        mBinding.easyTitle.addRightText("To-Bottom") {
            mBinding.recyclerView.scrollToPosition(datas.size - 1)
        }


        mBinding.btnScollTo.click {

//            layoutManager.scrollToPositionWithOffset(75, 0)

//            rvScrollToPosition(mBinding.recyclerView, layoutManager, 75)

//            mBinding.recyclerView.scrollToPosition(75)

//            mBinding.recyclerView.smoothScrollToPosition(75)

//            smoothScrolling = true
//            smoothScrollPosition = 75

            RVScrollUtils.rvSmoothScrollToPosition(mBinding.recyclerView, layoutManager, 75)

        }

        mBinding.btnSpeed.click {
//            layoutManager.setSpeedSlow(100F)
        }

//        mBinding.recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//
//                if (smoothScrolling || newState == SCROLL_STATE_IDLE) {
//
//                    val lastPos: Int = layoutManager.findLastVisibleItemPosition()
//
//                    if (smoothScrollPosition >= 0 && lastPos == smoothScrollPosition) {
//
//                        val childAt: View? = layoutManager.findViewByPosition(lastPos)
//                        var top = childAt?.top ?: 0
//                        recyclerView.scrollBy(0, top)
//
//                        mBinding.recyclerView.removeOnScrollListener(this)
//                        smoothScrollPosition = -1
//
//                    }
//                    smoothScrolling = false
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//
//            }
//        })
//
    }


}