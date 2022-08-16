package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.guadou.lib_baselib.utils.log.YYLogUtils;
import com.jeremyliao.liveeventbus.LiveEventBus;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

@Aspect
public class LoginAspect {

    @Pointcut("@annotation(com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.Login)")
    public void Login() {
    }

    @Pointcut("@annotation(com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.aop.LoginCallback)")
    public void LoginCallback() {
    }

    //带回调的注解处理
    @Around("LoginCallback()")
    public void loginCallbackJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        YYLogUtils.w("走进AOP方法-LoginCallback()");
        Signature signature = joinPoint.getSignature();

        if (!(signature instanceof MethodSignature)){
            throw new RuntimeException("该注解只能用于方法上");
        }

        LoginCallback loginCallback = ((MethodSignature) signature).getMethod().getAnnotation(LoginCallback.class);
        if (loginCallback == null) return;

        //判断当前是否已经登录
        if (LoginManager.isLogin()) {
            joinPoint.proceed();

        } else {
            LifecycleOwner lifecycleOwner = (LifecycleOwner) joinPoint.getTarget();

            LiveEventBus.get("login").observe(lifecycleOwner, new Observer<Object>() {
                @Override
                public void onChanged(Object integer) {
                    try {
                        joinPoint.proceed();
                        LiveEventBus.get("login").removeObserver(this);

                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                        LiveEventBus.get("login").removeObserver(this);
                    }
                }
            });

            LoginManager.gotoLoginPage();
        }
    }

    //不带回调的注解处理
    @Around("Login()")
    public void loginJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        YYLogUtils.w("走进AOP方法-Login()");
        Signature signature = joinPoint.getSignature();

        if (!(signature instanceof MethodSignature)){
            throw new RuntimeException("该注解只能用于方法上");
        }

        Login login = ((MethodSignature) signature).getMethod().getAnnotation(Login.class);
        if (login == null) return;

        //判断当前是否已经登录
        if (LoginManager.isLogin()) {
            joinPoint.proceed();
        } else {
            //如果未登录，去登录页面
            LoginManager.gotoLoginPage();
        }


    }
}
