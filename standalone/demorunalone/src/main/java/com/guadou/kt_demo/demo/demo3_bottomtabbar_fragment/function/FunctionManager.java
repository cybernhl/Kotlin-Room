package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function;

import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;

import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.HashMap;

public class FunctionManager {

    private static FunctionManager functionManager;

    private static HashMap<String, IFunction> mFunctionMap;

    public FunctionManager() {
        mFunctionMap = new HashMap<>();

    }

    public static FunctionManager get() {
        if (functionManager == null) {
            functionManager = new FunctionManager();
        }
        return functionManager;
    }


    /**
     * 添加方法
     */
    public FunctionManager addFunction(IFunction function) {
        if (mFunctionMap != null) {
            mFunctionMap.put(function.functionName, function);
        }
        return this;
    }


    /**
     * 执行方法
     */
    public void invokeFunction(String key) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (mFunctionMap != null) {
            IFunction function = mFunctionMap.get(key);

            if (function != null) {
                function.function();
                //用完移除掉
                removeFunction(key);
            } else {
                try {
                    throw new RuntimeException("function not found");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * 使用之后移除相关的缓存
     */
    public void removeFunction(String key) {
        if (mFunctionMap != null) {
            mFunctionMap.remove(key);
        }
    }


    // =======  LiveData的方式 ===================

    public void addLoginCallback(LifecycleOwner owner, ILoginCallback callback) {
        LiveEventBus.get("login", Boolean.class).observe(owner, aBoolean -> {
            if (aBoolean != null && aBoolean) {
                callback.callback();
            }
        });
    }

    public interface ILoginCallback {
        void callback();
    }

    public void finishLogin() {
        LiveEventBus.get("login").post(true);
    }
}
