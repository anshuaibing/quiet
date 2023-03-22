package com.example.andemo.ui.designpattern.singletondemo;

public class SingletonStatic {
    private static class SingletonHolder {
        private static final SingletonStatic INSTANCE = new SingletonStatic();
    }
    private SingletonStatic (){}
    public static final SingletonStatic getInstance() {
        return SingletonHolder.INSTANCE;
    }
    public String showMessage(){
        System.out.println("Hello World!");
        return "这是：SingletonStatic";
    }
}
/*
* 静态模式，达到类似双检锁的效果。利用调用SingletonHolder类来实现第二把锁的功能：实例化INSTANCE，
* 适用于静态域需要延迟实例化的情况下使用
* 优点：相比较双检锁方式，静态方式实现起来比较简单
* 缺点：
*
* */
