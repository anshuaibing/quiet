package com.example.andemo.ui.designpattern.singletondemo;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andemo.R;

public class SingletonActivity extends AppCompatActivity {
    private Button btnDCL;
    private Button btnEnum;
    private Button btnHungry;
    private Button btnLazy;
    private Button btnStatic;
    private TextView tvSingleton;

    SingletonDCL singletonDCL = SingletonDCL.getSingletonDCL();
    SingletonEnum singletonEnum = SingletonEnum.INSTANCE;
    SingletonHungry singletonHungry = SingletonHungry.getInstance();
    SingletonLazy singletonLazy =  SingletonLazy.getInstance();
    SingletonStatic singletonStatic = SingletonStatic.getInstance();

    String DCLStr=singletonDCL.showMessage();
    String enumStr=singletonEnum.showMessage();
    String hungryStr=singletonHungry.showMessage();
    String lazyStr=singletonLazy.showMessage();
    String staticStr=singletonStatic.showMessage();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleton);
        init ();
    }

    void init (){
        btnDCL=findViewById(R.id.btnDCL);
        btnEnum=findViewById(R.id.btnEnum);
        btnHungry=findViewById(R.id.btnHungry);
        btnLazy=findViewById(R.id.btnLazy);
        btnStatic=findViewById(R.id.btnStatic);
        tvSingleton=findViewById(R.id.tvSingleton);
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnDCL:
                tvSingleton.setText(DCLStr);
                break;
            case R.id.btnEnum:
                tvSingleton.setText(enumStr);
                break;
            case R.id.btnHungry:
                tvSingleton.setText(hungryStr);
                break;
            case R.id.btnLazy:
                tvSingleton.setText(lazyStr);
                break;
            case R.id.btnStatic:
                tvSingleton.setText(staticStr);
                break;
        }
    }

}