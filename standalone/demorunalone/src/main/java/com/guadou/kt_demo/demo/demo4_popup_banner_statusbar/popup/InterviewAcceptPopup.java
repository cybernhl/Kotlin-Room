package com.guadou.kt_demo.demo.demo4_popup_banner_statusbar.popup;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.guadou.lib_baselib.utils.CheckUtil;
import com.lxj.xpopup.core.CenterPopupView;

import java.util.List;

@SuppressLint("ViewConstructor")
public class InterviewAcceptPopup extends CenterPopupView implements View.OnClickListener {

    private TextView mTvDate;
    private OnChoosePiclerListener mListener;
    private List<String> mOptions;
    private Activity mActivity;

    public InterviewAcceptPopup(@NonNull Activity activity, List<String> datas, OnChoosePiclerListener listener) {
        super(activity);
        mOptions = datas;
        mListener = listener;
        mActivity = activity;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.dialog_interview_accept;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView btnYes = findViewById(R.id.btn_y);
        mTvDate = findViewById(R.id.tv_date);
        LinearLayout ll_date = findViewById(R.id.ll_date);


    }

    protected void onShow() {
        super.onShow();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_date) {
            showPickerView();

        } else if (v.getId() == R.id.btn_y) {

            String date = mTvDate.getText().toString();

            if (mListener != null && !CheckUtil.isEmpty(date)) {
                dismiss();
                mListener.OnYesClick(date);
            } else {

            }

        } else if (v.getId() == R.id.iv_dialog_select_delete) {
            dismiss();
        }
    }

    private void showPickerView() {

    }

    public interface OnChoosePiclerListener {

        void OnYesClick(String interviewTime);
    }
}