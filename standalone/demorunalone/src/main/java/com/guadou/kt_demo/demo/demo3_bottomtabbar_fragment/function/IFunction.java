package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function;

public abstract class IFunction {

    public String functionName;

    public IFunction(String functionName) {

        this.functionName = functionName;
    }

    protected abstract void function();

}