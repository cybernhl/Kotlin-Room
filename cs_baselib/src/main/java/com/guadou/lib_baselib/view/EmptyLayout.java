package com.guadou.lib_baselib.view;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.guadou.basiclib.R;


/**
 * 显示Loading和Error的View
 */
public class EmptyLayout extends LinearLayout {
    //加载成功 不显示emptylayout了
    public static final int HIDE_LAYOUT = 4;
    //网络没有连接
    public static final int NETWORK_ERROR = 1;
    //正在加载数据
    public static final int NETWORK_LOADING = 2;
    //没有数据
    public static final int NODATA = 3;
    public static final int NODATA_ENABLE_CLICK = 5;

    private ProgressBar animProgress;
    //	private boolean clickEnable = true;
    private final Context context;
    public ImageView img;
    private android.view.View.OnClickListener listener;
    private int mErrorState;
    private String strNoDataContent = "";
    private TextView tv;

    public EmptyLayout(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public EmptyLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        View view = View.inflate(context, R.layout.view_error_layout, null);
        img = (ImageView) view.findViewById(R.id.img_error_layout);
        tv = (TextView) view.findViewById(R.id.tv_error_layout);
        RelativeLayout mLayout = (RelativeLayout) view.findViewById(R.id.pageerrLayout);
        animProgress = (ProgressBar) view.findViewById(R.id.animProgress);
        setBackgroundColor(-1);
        addView(view);
        changeErrorLayoutBgMode(context);
    }

    public void changeErrorLayoutBgMode(Context context1) {
        // mLayout.setBackgroundColor(SkinsUtil.getColor(context1,
        // "bgcolor01"));
        // tv.setTextColor(SkinsUtil.getColor(context1, "textcolor05"));
    }

    public void dismiss() {
        mErrorState = HIDE_LAYOUT;
        setVisibility(View.GONE);
    }

    public int getErrorState() {
        return mErrorState;
    }

    public boolean isLoadError() {
        return mErrorState == NETWORK_ERROR;
    }

    public boolean isLoading() {
        return mErrorState == NETWORK_LOADING;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        onSkinChanged();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void onSkinChanged() {
    }

    public void setErrorMessage(String msg) {
        tv.setText(msg);
    }

    /**
     * 新添设置背景
     *
     * @param imgResource 图片的id
     * @param msg         图片下面的textView显示的文字
     */
    public void setErrorImag(int imgResource, String msg) {
        try {
            img.setBackgroundResource(imgResource);
            tv.setText(msg);
        } catch (Exception e) {
        }
    }

    public void setErrorType(int i) {
        setVisibility(View.VISIBLE);
        switch (i) {
            case NETWORK_ERROR:
                mErrorState = NETWORK_ERROR;
                if (isConnectivity(context)) {
                    tv.setText(R.string.error_view_load_error_click_to_refresh);
                    tv.setVisibility(View.VISIBLE);
                    img.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.loading_error));
                } else {
                    img.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.page_icon_network));
                    tv.setText(R.string.error_view_network_error_click_to_refresh);
                    tv.setVisibility(View.GONE);

                    img.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (!isConn(context)) {
                                setNetworkMethod(context);
                            }
                        }
                    });
                }
                img.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
//			clickEnable = true;
                break;
            case NETWORK_LOADING:
                mErrorState = NETWORK_LOADING;
                animProgress.setVisibility(View.VISIBLE);
                img.setVisibility(View.GONE);
                tv.setVisibility(View.VISIBLE);
                tv.setText(R.string.error_view_loading);
//			clickEnable = false;
                break;
            case NODATA:
                mErrorState = NODATA;
                img.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.loading_error));
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
//			clickEnable = true;
                break;
            case HIDE_LAYOUT:
                setVisibility(View.GONE);
                break;
            case NODATA_ENABLE_CLICK:
                mErrorState = NODATA_ENABLE_CLICK;
                img.setImageDrawable(getContext().getResources().getDrawable(R.mipmap.loading_error));
                img.setVisibility(View.VISIBLE);
                tv.setVisibility(View.VISIBLE);
                animProgress.setVisibility(View.GONE);
                setTvNoDataContent();
//			clickEnable = true;
                break;
            default:
                break;
        }
    }

    public void setNoDataContent(String noDataContent) {
        strNoDataContent = noDataContent;
    }

    public void setOnLayoutClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public void setTvNoDataContent() {
        if (!strNoDataContent.equals(""))
            tv.setText(strNoDataContent);
        else
            tv.setText(R.string.error_view_no_data);
    }

    @Override
    public void setVisibility(int visibility) {
        if (visibility == View.GONE)
            mErrorState = HIDE_LAYOUT;
        super.setVisibility(visibility);
    }


    /**
     * 描述：是否有网络连接.androidbase中AbWifiUtil中的方法
     */
    public static boolean isConnectivity(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((connectivityManager.getActiveNetworkInfo() != null && connectivityManager
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || telephonyManager
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /*
    * 判断网络连接是否已开
    *true 已打开  false 未打开
    * */
    public static boolean isConn(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /*
     * 打开设置网络界面
     * */
    public void setNetworkMethod(final Context context) {
        //提示对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getContext().getResources().getString(R.string.network_alert)).setMessage(getContext().getResources().getString(R.string.is_goto_setting))
                .setPositiveButton(R.string.setting, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = null;
                                //判断手机系统的版本  即API大于10 就是3.0或以上版本
                                if (Build.VERSION.SDK_INT > 10) {
                                    intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                                } else {
                                    intent = new Intent();
                                    ComponentName component = new ComponentName("com.android.settings", "com.android.settings" +
                                            ".WirelessSettings");
                                    intent.setComponent(component);
                                    intent.setAction("android.intent.action.VIEW");
                                }
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(intent);
                            }
                        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
