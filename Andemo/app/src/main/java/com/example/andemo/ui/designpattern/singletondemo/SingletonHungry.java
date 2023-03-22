package com.example.andemo.ui.designpattern.singletondemo;

public class SingletonHungry {
    private static SingletonHungry instance = new SingletonHungry();
    private SingletonHungry (){}
    public static SingletonHungry getInstance() {
        return instance;
    }
    public String showMessage(){
        System.out.println("Hello World!");
        return "这是：SingletonHungry";
    }
}
/*
* 这种实现方式是饿汉模式
* 优点：这种模式没有加锁，效率比较快
* 缺点：但是每次加载这个类时就会初始化一次，会造成资源的浪费
* 类装载时，instance就会实例化
* */