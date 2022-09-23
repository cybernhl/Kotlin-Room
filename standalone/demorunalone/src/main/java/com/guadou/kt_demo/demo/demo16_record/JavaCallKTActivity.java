package com.guadou.kt_demo.demo.demo16_record;


import android.view.View;

import androidx.annotation.NonNull;

import com.guadou.kt_demo.R;
import com.guadou.kt_demo.demo.demo5_network_request.SuccessCallbackImpl;
import com.guadou.kt_demo.demo.demo5_network_request.TestNet;
import com.guadou.kt_demo.demo.demo5_network_request.TestNetKt;
import com.guadou.kt_demo.demo.demo5_network_request.bean.Industry;
import com.guadou.lib_baselib.base.activity.BaseVMActivity;
import com.guadou.lib_baselib.base.vm.EmptyViewModel;
import com.guadou.lib_baselib.ext.ActivityExtKt;
import com.guadou.lib_baselib.ext.CommonExtKt;
import com.guadou.lib_baselib.ext.ToastUtils;
import com.guadou.lib_baselib.utils.CommUtils;
import com.guadou.lib_baselib.utils.log.YYLogUtils;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;


public class JavaCallKTActivity extends BaseVMActivity<EmptyViewModel> {

    @Override
    public int getLayoutIdRes() {
        return R.layout.activity_java_call_kt;
    }

    @Override
    public void startObserve() {

    }

    @Override
    public void init() {

//        KotlinDemo demo = new KotlinDemo();
//        demo.getAge();
//        demo.setName("123");
//        demo.getName();


        KotlinDemo.Companion.callStaticMethod1();
        KotlinDemo.callStaticMethod2();

        KotlinDemo.Companion.getSchool();
        KotlinDemo.Companion.setSchool("11");
        KotlinDemo.industry = "xx";

    }

    public void demo1(View view) {
        KotlinDemoKt.topLevelFun();

        int px = CommonExtKt.dp2px(getApplicationContext(), 20f);

//        ActivityExtKt
    }

    public void demo2(View view) {

        KotlinDemo demo = new KotlinDemo();

        KotlinDemoKt.setValueCallback1("test", new Function2<String, Integer, Unit>() {
            @Override
            public Unit invoke(String s, Integer integer) {
                YYLogUtils.w("收到回调：s:" + s + "integer:" + integer);
                return null;
            }
        });

        KotlinDemoKt.setValueCallback11("test", new Function2<Industry, Integer, Unit>() {
            @Override
            public Unit invoke(Industry industry, Integer integer) {
                YYLogUtils.w("收到回调：industry:" + this.toString() + " integer:" + integer);
                return null;

            }
        });

        KotlinDemoKt.setValueCallback12("test", new Function2<Industry, Integer, Unit>() {
            @Override
            public Unit invoke(Industry industry, Integer integer) {
                YYLogUtils.w("收到回调：industry:" + this.toString() + " integer:" + integer);
                return null;

            }
        });

        KotlinDemoKt.setValueCallback13("test", new Function2<MyCustomCallback, String, Unit>() {
            @Override
            public Unit invoke(MyCustomCallback myCustomCallback, String s) {

                YYLogUtils.w("收到回调：callback:" + myCustomCallback.toString() + " str:" + s);
                myCustomCallback.onCallback();

                return null;
            }
        });

//        demo.setValueCallback("test", new Function1<Industry, Unit>() {
//            @Override
//            public Unit invoke(Industry industry) {
//
//                YYLogUtils.w("industry:" + industry.toString());
//
//                return null;
//            }
//        });

    }

    public void demo3(View view) {
        KotlinDemo demo = new KotlinDemo();

        demo.setValueCallback2(new Function1<MyCustomCallback, Unit>() {
            @Override
            public Unit invoke(MyCustomCallback myCustomCallback) {

                //调用
                myCustomCallback.onCallback();

                return null;
            }
        });
    }

    public void demo4(View view) {
        KotlinDemo demo = new KotlinDemo();

        demo.setValueCallback3(new Function1<CoroutineScope, Unit>() {
            @Override
            public Unit invoke(CoroutineScope coroutineScope) {

                YYLogUtils.w("在协程中执行？" + Thread.currentThread().getName());

                return null;
            }
        });

//        demo.setValueCallback4(new Function2<CoroutineScope, Continuation<? super Unit>, Object>() {
//            @Override
//            public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
//
//                YYLogUtils.w("在协程中执行suspend？" + Thread.currentThread().getName());
//
//                return null;
//            }
//        });


        demo.callLaunch(new Function2<CoroutineScope, Continuation<? super Unit>, Object>() {
            @Override
            public Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {

                YYLogUtils.w("启动了协程===>做点什么好呢" + Thread.currentThread().getName());


                demo.callWithContext(new Function0<String>() {
                    @Override
                    public String invoke() {

                        YYLogUtils.w("切换线程看看" + Thread.currentThread().getName());

                        return null;
                    }
                }, new Continuation<String>() {
                    @NonNull
                    @Override
                    public CoroutineContext getContext() {
                        return coroutineScope.getCoroutineContext();
                    }

                    @Override
                    public void resumeWith(@NonNull Object o) {
                    }
                });

                return null;
            }
        });

    }

    public void demo5(View view) {

        KotlinDemo demo = new KotlinDemo();

        demo.setSamMethod(new MyCustomCallback() {
            @Override
            public void onCallback() {
                YYLogUtils.w("回调到了");
            }
        });
    }

    public void demo6(View view) {
        TestNet testNet = new TestNet();

        TestNetKt.setOnSuccessCallbackDsl(testNet, new Function1<SuccessCallbackImpl, Unit>() {
            @Override
            public Unit invoke(SuccessCallbackImpl successCallback) {

                successCallback.onSuccess(new Function1<String, String>() {
                    @Override
                    public String invoke(String s) {

                        YYLogUtils.w("str:" + s);

                        return s + "再加一点数据2";
                    }
                });

                successCallback.doSth(new Function0<Unit>() {
                    @Override
                    public Unit invoke() {

                        YYLogUtils.w("可以随便写点什么成功之后的逻辑2");
                        return null;
                    }
                });
                return null;
            }
        });


        testNet.requestDSLCallback();

    }
}
