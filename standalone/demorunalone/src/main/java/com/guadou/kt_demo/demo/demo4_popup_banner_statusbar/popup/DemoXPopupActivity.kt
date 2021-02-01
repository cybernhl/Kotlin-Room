package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import com.guadou.kt_demo.R
import com.guadou.kt_demo.BR
import com.guadou.kt_demo.databinding.ActivityDemoXpopupBinding
import com.guadou.lib_baselib.base.activity.BaseVDBActivity
import com.guadou.lib_baselib.base.vm.EmptyViewModel
import com.guadou.lib_baselib.bean.DataBindingConfig
import com.guadou.lib_baselib.ext.checkEmpty
import com.guadou.lib_baselib.ext.commContext
import com.guadou.lib_baselib.ext.toast
import com.guadou.lib_baselib.utils.CommUtils
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.lxj.xpopup.interfaces.OnConfirmListener
import com.lxj.xpopup.interfaces.OnSelectListener
import com.lxj.xpopup.interfaces.SimpleCallback
import com.lxj.xpopup.util.XPermission

/**
 * Xpopup的示例
 */
class DemoXPopupActivity : BaseVDBActivity<EmptyViewModel, ActivityDemoXpopupBinding>() {

    companion object {
        fun startInstance() {
            commContext().let {
                it.startActivity(Intent(it, DemoXPopupActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
            }
        }
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_demo_xpopup)
            .addBindingParams(BR.click, ClickProxy())
    }


    override fun startObserve() {

    }

    override fun init() {
    }

    /**
     * DataBinding事件处理
     */
    inner class ClickProxy {

        //默认弹窗
        fun normalPopup() {

            val popupView = XPopup.Builder(mActivity)
                .autoOpenSoftInput(false)
                .hasShadowBg(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗
                .setPopupCallback(object : SimpleCallback() {
                    override fun onCreated(popupView: BasePopupView) {
                        Log.e("tag", "弹窗创建了")
                    }

                    override fun onShow(popupView: BasePopupView) {
                        Log.e("tag", "onShow")
                    }

                    override fun onDismiss(popupView: BasePopupView) {
                        Log.e("tag", "onDismiss")
                    }

                    override fun beforeDismiss(popupView: BasePopupView) {
                        Log.e("tag", "准备消失的时候执行")
                    }

                    //如果你自己想拦截返回按键事件，则重写这个方法，返回true即可
                    override fun onBackPressed(popupView: BasePopupView): Boolean {
                        toast("我拦截的返回按键，按返回键XPopup不会关闭了")
                        return true
                    }
                }).asConfirm(
                    "哈哈", "床前明月光，疑是地上霜；举头望明月，低头思故乡。",
                    "取消", "确定",
                    OnConfirmListener { toast("click confirm") }, null, false
                )

            //设置内容的颜色
            popupView.contentTextView.setTextColor(Color.BLACK)
            popupView.show()
        }

        //居中弹窗
        fun centerPopup() {
            XPopup.Builder(mActivity)
                .autoOpenSoftInput(false)
                .hasShadowBg(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗
                .asCustom(
                    InterviewAcceptPopup(mActivity, null) { faceTime: String? ->
                        toast(faceTime)
                    }
                )
                .show()
        }

        //底部的弹窗
        fun bottomPopup() {
            XPopup.Builder(mActivity)
                .autoOpenSoftInput(false)
                .hasShadowBg(true)
                .moveUpToKeyboard(false) //如果不加这个，评论弹窗会移动到软键盘上面
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .enableDrag(true)
//              .isThreeDrag(true) //是否开启三阶拖拽，如果设置enableDrag(false)则无效
                .asCustom(ZhihuCommentPopup(mActivity))
                .show()

        }

        //依附布局
        fun attchPopup(view: View) {

            XPopup.Builder(mActivity)
                .hasShadowBg(false)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                //                        .isDarkTheme(true)
                //                        .popupAnimation(PopupAnimation.NoAnimation) //NoAnimation表示禁用动画
                //                        .isCenterHorizontal(true) //是否与目标水平居中对齐
                //                        .offsetY(-60)
                //                        .offsetX(80)
                //                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
                .atView(view) // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(arrayOf("分享", "编辑编辑编辑编辑编", "不带icon", "分享"),
                    intArrayOf(R.mipmap.ic_launcher_round, R.mipmap.ic_launcher_round),
                    object : OnSelectListener {
                        override fun onSelect(position: Int, text: String) {
                            toast("click $text")
                        }
                    })
                .show()
        }

        //底部全屏弹窗
        fun fullBottomPopup() {
            XPopup.Builder(mActivity)
                .hasShadowBg(false)
                .hasStatusBarShadow(true)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true)  //自动打开软键盘
                .moveUpToKeyboard(false) //在软键盘上面
                .asCustom(CustomFullScreenPopup(mActivity))
                .show()
        }

        //输入法布局上面弹窗
        fun softInputPopup() {
            // 弹出新的弹窗用来输入
            val textBottomPopup = CustomEditTextBottomPopup(mActivity)
            XPopup.Builder(mActivity)
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .autoOpenSoftInput(true) //自动打开软键盘
                .moveUpToKeyboard(true) //在软键盘上面
                .hasShadowBg(false)
                .setPopupCallback(object : SimpleCallback() {
                    override fun onShow(popupView: BasePopupView) {}
                    override fun onDismiss(popupView: BasePopupView) {
                        val comment = textBottomPopup.comment
                        if (!comment.checkEmpty()) {
                            toast("发布的内容：$comment")
                            //或者在Popup中加一个回调专门用于发布的事件
                        }
                    }
                })
                .asCustom(textBottomPopup)
                .show()
        }

        //自定义父容器展示
        fun customPopup() {
            XPopup.Builder(mActivity)
                .hasShadowBg(false)
                .hasStatusBarShadow(false) //启用状态栏阴影
                .hasBlurBg(false)  //高斯模糊背景
                .autoOpenSoftInput(false)
                .moveUpToKeyboard(false) //如果不加这个
                .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                .enableDrag(false)
                .offsetY(-CommUtils.dip2px(100))
                .asCustom(BottomCartPopup(mActivity))
                .show()
        }

        //最上层显示的权限
        fun backgoundPopup() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                XPopup.requestOverlayPermission(mActivity, object : XPermission.SimpleCallback {
                    override fun onGranted() {

                    }

                    override fun onDenied() {

                    }
                })
            }
        }

        //大图的浏览相册模式
        fun bigImagePopup() {
            DemoImagePreviewActivity.startInstance()
        }

    }

}