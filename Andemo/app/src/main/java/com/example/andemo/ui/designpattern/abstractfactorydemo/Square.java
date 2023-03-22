package com.example.andemo.ui.designpattern.abstractfactorydemo;

public class Square implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Square::draw() method.");
    }
}
