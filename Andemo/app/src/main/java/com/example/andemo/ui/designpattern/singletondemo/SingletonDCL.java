package com.example.andemo.ui.designpattern.singletondemo;

public class SingletonDCL {
    private volatile static SingletonDCL instance;
    private SingletonDCL (){}
    public static SingletonDCL getSingletonDCL() {
        if (instance == null) {
            synchronized (SingletonDCL.class) {
                if (instance == null) {
                    instance = new SingletonDCL();
                }
            }
        }
        return instance;
    }
    public String showMessage(){
        System.out.println("Hello World!");
        return "这是：SingletonDCL";
    }
}

/*
* 双检锁模式
*顾名思义，这种模式加了两把锁，一把锁负责判断类是否是第一次装载，第二把锁是判断instance是否被实例化
* 优点：安全，更加节约资源，能保证高性能
* 缺点：实现起来比较复杂，费人
*
*/
