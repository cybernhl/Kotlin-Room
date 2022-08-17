package com.guadou.kt_demo.demo.demo3_bottomtabbar_fragment.function;

public abstract class Function {

    public String functionName;

    public Function(String functionName) {

        this.functionName = functionName;
    }

    protected abstract void function();

}