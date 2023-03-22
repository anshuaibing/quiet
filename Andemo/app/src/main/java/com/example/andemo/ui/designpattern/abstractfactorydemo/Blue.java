package com.example.andemo.ui.designpattern.abstractfactorydemo;

public class Blue implements Color {

    @Override
    public void fill() {
        System.out.println("Inside Blue::fill() method.");
    }
}