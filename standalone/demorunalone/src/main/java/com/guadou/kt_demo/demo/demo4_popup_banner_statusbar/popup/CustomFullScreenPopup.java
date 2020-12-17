package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup;

import android.content.Context;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.lxj.xpopup.impl.FullScreenPopupView;


/**
 * Description: 自定义全屏弹窗
 */
public class CustomFullScreenPopup extends FullScreenPopupView {

    public CustomFullScreenPopup(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.custom_fullscreen_popup;
    }
}
