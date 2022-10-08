package com.guadou.kt_demo.demo.demo17_softinput


import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.guadou.kt_demo.R
import com.guadou.kt_demo.demo.demo17_softinput.utils.ReviewDialog
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.bindData
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.vertical
import com.guadou.lib_baselib.utils.log.YYLogUtils


class SoftInputMomentActivity : BaseVMActivity<EmptyViewModel>() {

    private lateinit var rvList: RecyclerView

    override fun getLayoutIdRes(): Int {
        return R.layout.activity_soft_input_monent
    }

    override fun startObserve() {

    }

    override fun init() {

        val datas = mutableListOf<String>()
        for (i in 1..20) {
            datas.add(i.toString())
        }

        rvList = findViewById<RecyclerView>(R.id.rv_list)

        rvList.vertical()
            .bindData(datas, R.layout.item_soft_input_demo) { holder, t, position ->
                holder.getView<TextView>(R.id.tv_review).click {
                    showReviewDialog(it, position)
                }
            }

    }

    private fun showReviewDialog(view: View, position: Int) {
        val rvReviewY = getY(view)
        val rvReviewHeight = view.height

        val dialog = ReviewDialog(mActivity)
        dialog.show()

        view.postDelayed({
            //等待弹窗弹起自后再获取到Y的高度，就是加上了软键盘之后的高度了
            val etReviewY = getY(dialog.findViewById<LinearLayout>(R.id.dialog_layout_comment))

            val offsetY = rvReviewY - (etReviewY - rvReviewHeight)

            rvList.smoothScrollBy(0, offsetY)

        }, 350)
    }

    private fun getY(view: View): Int {
        val rect = IntArray(2)
        view.getLocationOnScreen(rect)
        return rect[1]
    }
}