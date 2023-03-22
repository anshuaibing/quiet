package com.example.andemo.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andemo.MainActivity;
import com.example.andemo.R;
import com.example.andemo.Regist;
import com.example.andemo.data.databases.Users;
import com.example.andemo.greendao.DaoManager;
import com.example.andemo.greendao.UsersDao;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
//    private MySQLiteOpenHelper mSQLiteOpenHelper;
    private EditText username;
    private EditText password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        mSQLiteOpenHelper = new MySQLiteOpenHelper(this);
        initView();
        DaoManager.getInstance().initGreenDao(this);//初始化
    }

    private void initView() {
        // 初始化控件
        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        // 设置点击事件监听器
    }

    public void onClick(View view) {
        switch (view.getId()) {
            //一键登录
            case R.id.login_loginDirect:
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            // 跳转到注册界面
            case R.id.login_regist:
                startActivity(new Intent(this, Regist.class));
                finish();
                break;
            case R.id.login_login:
                String name = username.getText().toString().trim();//.trim（）删除两边的空格
                String password1 = password.getText().toString().trim();

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password1)) {//TextUtils.isEmpty（）输入框是空值或者你就敲了几下空格键该方法都会返回true
//                    ArrayList<User> data = mSQLiteOpenHelper.getAllData();// sqlite：data为获取的user表内的user信息
                    UsersDao usersDao = DaoManager.getInstance().getDaoSession().getUsersDao();// greendao：data为获取的user表内的user信息
                    List<Users> userList = usersDao.loadAll();
                    boolean match = false;

                    for (int i = 0; i < userList.size(); i++) {//遍历比较
                        Users user = userList.get(i);//获取userList里的第i个user信息
                        if (name.equals(user.getName()) && password1.equals(user.getPassword())) {//将信息与输入的信息进行对比
                            match = true;
                            break;
                        } else {match = false;}
                    }

                    if (match) {
                        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        finish();//销毁此Activity
                    } else {Toast.makeText(this, "用户名或密码不正确，请重新输入", Toast.LENGTH_SHORT).show();}
                } else {Toast.makeText(this, "请输入你的用户名或密码", Toast.LENGTH_SHORT).show();}
                break;
        }
    }


}