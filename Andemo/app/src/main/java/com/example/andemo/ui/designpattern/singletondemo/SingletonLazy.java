package com.example.andemo.ui.designpattern.singletondemo;

public class SingletonLazy {
    private static SingletonLazy instance ;

    //让构造函数为 private，这样该类就不会被实例化
    private SingletonLazy() {
    }

    //获取唯一可用的对象
    public static synchronized SingletonLazy getInstance() {
        if ( instance == null) {//加一个锁，确保只进行一次实例化类
            instance = new SingletonLazy();
        }
        return instance;
    }

    public String showMessage(){
        System.out.println("Hello World!");
        return "这是：SingletonLazy";
    }
}

/*
 *
 * 懒汉模式
 * 适合多线程，效率低，加载类时会进行一次判断，如果instance已经实例化，就不会在此进行实例化，避免了资源浪费。
 * 优点：第一次调用才初始化，避免内存浪费。
 * 缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率。
 *
 */