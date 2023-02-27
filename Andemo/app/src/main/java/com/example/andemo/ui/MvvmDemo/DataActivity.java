package com.example.andemo.ui.MvvmDemo;





import androidx.appcompat.app.AppCompatActivity;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ObservableField;


import android.annotation.SuppressLint;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.andemo.DataBean;

import com.example.andemo.R;
import com.example.andemo.databinding.ActivityDataBinding;


public class DataActivity extends AppCompatActivity {

    private EditText editText;
    private TextView textView;
    private Button button;
    private String dataString;
    private String dataVMstring;
    private DataBean dataBean;

    @SuppressLint({"SetTextI18n", "InlinedApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
//        ActivityDataBinding activityDataBinding=DataBindingUtil.setContentView(this,R.layout.activity_data);
        ActivityDataBinding activityDataBinding=DataBindingUtil.setContentView(this,R.layout.activity_data);
//        peopleBean = new PeopleBean("NorthernBrain", 25);

        dataBean = new DataBean(new ObservableField<String>(""));
        activityDataBinding.setDataBean(dataBean);
//        activityDataBinding.setDataBean(dataBean);
//        activityDataBinding.dataVMTView.setText(dataVMstring);


        editText =(EditText) findViewById(R.id.dataEdit);
        textView =(TextView) findViewById(R.id.dataTView);
        button =(Button) findViewById(R.id.dataBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataString =editText.getText().toString();
                textView.setText(dataString);
            }
        });


    }
}