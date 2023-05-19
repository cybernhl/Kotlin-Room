package com.guadou.kt_demo.demo.demo18_customview.takevideo1

import android.os.Bundle
import android.os.PersistableBundle
import com.guadou.kt_demo.R
import com.guadou.lib_baselib.base.activity.BaseVMActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.gotoActivity
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.StatusBarUtils


class RecoderVideo1Activity : BaseVMActivity<EmptyViewModel>() {

    lateinit var mRecorderVideoView: RecorderVideoView

    companion object {
        fun startInstance() {
            commContext().gotoActivity<RecoderVideo1Activity>()
        }
    }

    override fun getLayoutIdRes(): Int = R.layout.activity_recode_video1

    override fun startObserve() {

    }

    override fun init() {
        StatusBarUtils.immersive(this)

        mRecorderVideoView = findViewById(R.id.recoder_video_view)
        mRecorderVideoView.setOnUserSureCompleteListener {
            toast("用户已经录制完成")
        }
    }

    override fun onResume() {
        super.onResume()
        mRecorderVideoView.clearWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
        mRecorderVideoView.destoryAll()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        mRecorderVideoView.stop();
    }
}