//package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment;
//
//
//import android.content.Intent;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.annotation.NonNull;
//
//import com.guadou.kt_demo.R;
//import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.Login;
//import com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginManager;
//import com.guadou.lib_baselib.base.fragment.BaseVMFragment;
//import com.guadou.lib_baselib.base.vm.EmptyViewModel;
//import com.guadou.lib_baselib.ext.SharedPrefExtKt;
//import com.guadou.lib_baselib.ext.ToastUtils;
//import com.luck.picture.lib.tools.SPUtils;
//
//public class Demo3One1Fragment extends BaseVMFragment<EmptyViewModel> {
//
//    private Button mBtnCleanToken;
//    private Button mBtnProfile;
//
//    public static Demo3One1Fragment obtainFragment() {
//        return new Demo3One1Fragment();
//    }
//
//    @Override
//    public int getLayoutIdRes() {
//        return R.layout.fragment_demo3_page;
//    }
//
//    @Override
//    public void startObserve() {
//
//    }
//
//    @Override
//    public void init() {
//
//        mBtnCleanToken.setOnClickListener(v -> {
//            LoginManager.cleanToken();
//            ToastUtils.INSTANCE.makeText(mActivity, "清除成功");
//        });
//
//
//        mBtnProfile.setOnClickListener(v -> {
//            gotoProfilePage();
//        });
//    }
//
//    @Login
//    private void gotoProfilePage() {
//        startActivity(new Intent(mActivity, ProfileDemoActivity.class));
//    }
//
//    @Override
//    protected void initViews(@NonNull View view) {
//        super.initViews(view);
//        mBtnCleanToken = view.findViewById(R.id.btn_clean_token);
//        mBtnProfile = view.findViewById(R.id.btn_profile);
//    }
//
//}
