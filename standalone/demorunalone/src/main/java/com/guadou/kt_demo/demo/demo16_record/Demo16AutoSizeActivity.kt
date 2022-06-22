package com.guadou.kt_demo.demo.demo16_record

import android.annotation.SuppressLint
import android.content.Intent
import com.guadou.kt_demo.R
import com.guadou.kt_demo.databinding.ActivityDemo16AutosizeBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.click
import com.guadou.lib_baselib.ext.commContext
import dagger.hilt.android.AndroidEntryPoint

/**
 * 录制
 */
@AndroidEntryPoint
class Demo16AutoSizeActivity : BaseVDBActivity<EmptyViewModel, ActivityDemo16AutosizeBinding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, Demo16AutoSizeActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo16_autosize)
    }

    @SuppressLint("SetTextI18n")
    override fun startObserve() {
    }

    @SuppressLint("ObjectAnimatorBinding", "Recycle")
    override fun init() {
        mBinding.tvIcon.text = "\ue6cc"


//        val mhandler = Handler()
//        mhandler.post() {
//            mBinding.ivAnim.animate().scaleX(2f).scaleY(2f).translationX(200f).translationY(200f).setDuration(1000).start()
//        }


//        val animatorscaleX = ObjectAnimator.ofFloat(mBinding.ivAnim, "scaleX", 2f)
//        val animatorscaleY = ObjectAnimator.ofFloat(mBinding.ivAnim, "scaleY", 2f)
//        val animatortranslationX = ObjectAnimator.ofFloat(mBinding.ivAnim, "translationX", 200f)
//        val animatortranslationY = ObjectAnimator.ofFloat(mBinding.ivAnim, "translationY", 200f)
//
//        val set = AnimatorSet()
//        set.setDuration(1000).play(animatorscaleX).with(animatorscaleY).with(animatortranslationX).with(animatortranslationY)
//        set.start()

//        val looperThread = MyLooperThread(this, mBinding.tvRMsg)
//        looperThread.start()

        mBinding.ivAnim.click {

//            looperThread.handler.obtainMessage(200, "test set tv'msg").sendToTarget()

            //试试子线程执行动画看看
//            looperThread.handler.post {
//                mBinding.ivAnim.animate().scaleX(2f).scaleY(2f).translationX(200f).translationY(200f).setDuration(1000).start()
//            }

            //试试HandlerThread执行动画
            val anim = mBinding.ivAnim.animate()
                .scaleX(2f)
                .scaleY(2f)
                .translationXBy(200f)
                .translationYBy(200f)
                .setDuration(2000)

            AsynAnimUtil.instance.startAnim(this, anim)

        }

        //退出looper
//        looperThread.looper.quit()

    }

}