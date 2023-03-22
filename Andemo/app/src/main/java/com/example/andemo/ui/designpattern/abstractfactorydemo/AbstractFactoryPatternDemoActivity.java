package com.example.andemo.ui.designpattern.abstractfactorydemo;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andemo.R;

public class AbstractFactoryPatternDemoActivity extends AppCompatActivity {

/**
    简单工厂模式和工厂方法模式都是创建对象的设计模式，但它们的实现方式不同。

    简单工厂模式是将对象的创建逻辑封装在一个工厂类中，该工厂类根据客户端传递的参数来决定创建哪种类型的对象。客户端只需要知道需要创建的对象的名称或类型，无需知道对象的创建细节。简单工厂模式通常只有一个工厂类，它包含一个或多个工厂方法，用于创建不同类型的对象。

    工厂方法模式则是定义一个抽象的工厂接口，该接口包含创建对象的抽象方法，具体的对象创建由实现该接口的具体工厂类来完成。客户端通过调用工厂方法来创建对象，无需知道具体的对象创建过程。每个具体的工厂类都负责创建一种类型的对象。

    简单工厂模式的优点是实现简单，适合对象类型相对稳定的场景。缺点是当需要新增对象类型时，需要修改工厂类的代码，违反了开闭原则。而工厂方法模式可以很好地遵循开闭原则，但需要定义更多的类和接口，增加了代码的复杂度。

    选择哪种模式取决于具体的需求和场景。如果对象类型相对固定，且创建逻辑比较简单，可以使用简单工厂模式；如果对象类型相对复杂且需要满足开闭原则，可以考虑使用工厂方法模式。

 **/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abstract_factory_pattern_demo);

        //获取形状工厂
        AbstractFactory shapeFactory = FactoryProducer.getFactory("SHAPE");

        //获取形状为 Circle 的对象
        Shape shape1 = shapeFactory.getShape("CIRCLE");

        //调用 Circle 的 draw 方法
        shape1.draw();

        //获取形状为 Rectangle 的对象
        Shape shape2 = shapeFactory.getShape("RECTANGLE");

        //调用 Rectangle 的 draw 方法
        shape2.draw();

        //获取形状为 Square 的对象
        Shape shape3 = shapeFactory.getShape("SQUARE");

        //调用 Square 的 draw 方法
        shape3.draw();

        //获取颜色工厂
        AbstractFactory colorFactory = FactoryProducer.getFactory("COLOR");

        //获取颜色为 Red 的对象
        Color color1 = colorFactory.getColor("RED");

        //调用 Red 的 fill 方法
        color1.fill();

        //获取颜色为 Green 的对象
        Color color2 = colorFactory.getColor("GREEN");

        //调用 Green 的 fill 方法
        color2.fill();

        //获取颜色为 Blue 的对象
        Color color3 = colorFactory.getColor("BLUE");

        //调用 Blue 的 fill 方法
        color3.fill();


    }

}