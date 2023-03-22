package com.example.andemo.ui.designpattern.singletondemo;

public enum SingletonEnum {
    INSTANCE;
    public String showMessage(){
        System.out.println("Hello World!");
        return "这是：SingletonEnum";
    }
}
/*
* 这种实现方式还没有被广泛采用，但这是实现单例模式的最佳方法。它更简洁，自动支持序列化机制，绝对防止多次实例化。
*
*/