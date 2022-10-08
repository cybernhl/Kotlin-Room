package com.guadou.kt_demo.demo.demo17_softinput.utils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.guadou.kt_demo.demo.demo16_record.softinput.Keyboard3Utils;

@SuppressLint("ClickableViewAccessibility")
public class ReviewDialog extends Dialog {

    public ReviewDialog(Context context) {
        this(context, R.style.quick_option_dialog);
    }

    //两个参数的构造方法实现具体的逻辑
    public ReviewDialog(Context context, int themeResId) {
        super(context, themeResId);

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_review, null);

        EditText etComment = view.findViewById(R.id.et_comment);

        //自动弹出弹窗
        etComment.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(etComment, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

        requestWindowFeature(Window.FEATURE_NO_TITLE);  //设置没有标题
        //设置触摸一下整个View.让其可取消。触摸（不是点击）对话框任意地方取消对话框
        view.setOnTouchListener((View view1,  MotionEvent motionEvent) -> {
            dismiss();
            return true;  //消费掉此次触摸事件
        });

        //View设置完成，赋值给dialog对话框
        super.setContentView(view);

    }


    /**
     * 对话框被创建调用的方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置对话框显示的位置.在底部显示
        getWindow().setGravity(Gravity.BOTTOM);
        //设置对话框的宽度，填充屏幕
        WindowManager wm = getWindow().getWindowManager();
        Display display = wm.getDefaultDisplay();
        int width = display.getWidth();
        //获取对话框的属性
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = width; //和屏幕一样宽
        getWindow().setAttributes(params);
    }
}
