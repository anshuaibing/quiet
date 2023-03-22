package com.example.andemo.ui.designpattern.abstractfactorydemo;

public class Circle implements Shape {

    @Override
    public void draw() {
        System.out.println("Inside Circle::draw() method.");
    }
}