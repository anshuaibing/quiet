package com.example.andemo.ui.designpattern.abstractfactorydemo;

public class Red implements Color {

    @Override
    public void fill() {
        System.out.println("Inside Red::fill() method.");
    }
}