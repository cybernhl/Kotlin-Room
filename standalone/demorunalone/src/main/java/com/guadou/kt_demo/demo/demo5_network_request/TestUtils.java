package com.guadou.kt_demo.demo.demo5_network_request;


import com.guadou.lib_baselib.utils.Log.YYLogUtils;
import com.guadou.lib_baselib.utils.ThreadPoolUtils;

import java.util.Random;

public class TestUtils {

    private static TestUtils _instance = new TestUtils();

    TestUtils(){}

    public static TestUtils getInstance(){
        return _instance;
    }

//    private static TestUtils instance = null;
//
//    private TestUtils () {
//    }
//
//    public static TestUtils getInstance () {
//        if (instance == null) {
//            synchronized (TestUtils.class) {
//                if (instance == null) {
//                    instance = new TestUtils();
//                }
//            }
//        }
//        return instance ;
//    }

    private int isEnable = 0;

    public synchronized void setValue(int value) {

        ThreadPoolUtils.getCachedThreadPool().execute(() -> {

            try {
                Thread.sleep(new Random().nextInt(1000));
                YYLogUtils.w("value:"+value);
                isEnable = value;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

    public synchronized int getValue() {
        return isEnable;
    }

}
