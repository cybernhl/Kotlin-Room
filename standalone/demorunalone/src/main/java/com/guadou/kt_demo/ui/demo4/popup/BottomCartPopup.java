package com.guadou.kt_demo.ui.demo4.popup;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

/**
 * 底部弹起的购物车详情弹窗
 * 弹起在指定的容器位置
 */
@SuppressLint("ViewConstructor")
public class BottomCartPopup extends BottomPopupView {

    private Context mContext;

    public BottomCartPopup(@NonNull Context context) {
        super(context);
        mContext = context;

    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_bottom_cart_detail;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
    }

    @Override
    protected int getPopupWidth() {
        return XPopupUtils.getWindowWidth(getContext());
    }

    @Override
    protected int getMaxWidth() {
        return XPopupUtils.getWindowWidth(getContext());
    }


}
