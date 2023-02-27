package com.example.andemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.andemo.data.DataBases.MySQLiteOpenHelper;
import com.example.andemo.ui.login.LoginActivity;

public class Regist extends AppCompatActivity {
    private MySQLiteOpenHelper mSQLiteOpenHelper;

    private EditText Username;
    private EditText tel;



    private EditText Password;
    private EditText RePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        initView();
        mSQLiteOpenHelper = new MySQLiteOpenHelper(this);
    }
    private void initView(){
        Username = findViewById(R.id.regist_username);
        Password = findViewById(R.id.regist_password);
        tel = findViewById(R.id.regist_tel);
        RePassword = findViewById(R.id.repassword);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_login: //返回登录页面
                Intent intent1 = new Intent(this, LoginActivity.class);
                startActivity(intent1);
                finish();
                break;

            case R.id.regist_regist:    //注册按钮
                //获取用户输入的用户名、密码、验证码
                String musername = Username.getText().toString().trim();
                String mpassword = Password.getText().toString().trim();
                Integer mtel= Integer.valueOf(tel.getText().toString().trim());
                String mrepassword = RePassword.getText().toString().trim();

                //注册验证

                if (!TextUtils.isEmpty(musername) && !TextUtils.isEmpty(mpassword)) {
                    if(mpassword.equals(mrepassword)){

                   mSQLiteOpenHelper.add(musername, mpassword,mtel);//将用户名和密码加入到数据库的表内中
                    Intent intent2 = new Intent(this, LoginActivity.class);
                    startActivity(intent2);
                    finish();
                    Toast.makeText(this, "验证通过，注册成功", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, "密码输入不一致！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "未完善信息，注册失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
}