package com.example.andemo.greendao;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andemo.R;
import com.example.andemo.data.DataBases.Users;

import java.util.ArrayList;
import java.util.List;

public class GreeenDaoDemo extends AppCompatActivity {

    private TextView green_tv;
    private EditText green_username;
    private EditText green_password;
    private EditText green_tel;
    private Button green_add;
    private Button green_del;
    private Button green_change;
    private Button green_check;
    boolean allboolean=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeen_dao_demo);
        DaoManager.getInstance().initGreenDao(this);
        initView();

    }

    private void initView(){
        green_tv=findViewById(R.id.greendao_tv);
        green_username=findViewById(R.id.green_username);
        green_password=findViewById(R.id.green_password);
        green_tel=findViewById(R.id.green_tel);
        green_add=findViewById(R.id.green_add);
        green_del=findViewById(R.id.green_del);
        green_change=findViewById(R.id.green_change);
        green_check=findViewById(R.id.green_check);
    }


    public void onClick(View view) {
        UsersDao usersDao = DaoManager.getInstance().getDaoSession().getUsersDao();// greendao：data为获取的user表内的user信息
        switch(view.getId()){

            case R.id.green_add:
                String username=green_username.getText().toString();
                String password=green_password.getText().toString();
                String tel=green_tel.getText().toString();

                if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)&& !TextUtils.isEmpty(tel)){
                    Users usersidemo = new Users();
                    usersidemo.setName(username);
                    usersidemo.setPassword(password);
                    usersidemo.setTel(tel);
                    DaoManager.getInstance().getDaoSession().insert(usersidemo);
                    green_tv.setText("添加用户："+username+"，成功！");
                }else {
                    green_tv.setText("添加用户失败，请完善所有信息！");
                }

                break;

            case R.id.green_del:
                String username_del=green_username.getText().toString();

                if (!TextUtils.isEmpty(username_del)){
                    List<Users> user_del=usersDao.queryBuilder().where(UsersDao.Properties.Name.eq(username_del)).list();
                    usersDao.deleteInTx(user_del);
                    green_tv.setText("删除用户："+username_del+"，成功！");
                    green_username.setText("");
                }else {
                    green_tv.setText("删除用户失败，请输入用户名！");
                }

                break;

            case R.id.green_change:
                String username_change=green_username.getText().toString();
                String password_change=green_password.getText().toString();
                String tel_change=green_tel.getText().toString();

                if (!TextUtils.isEmpty(username_change) && !TextUtils.isEmpty(password_change)&& !TextUtils.isEmpty(tel_change)){
                    List<Users> user_cahnge1=usersDao.queryBuilder().where(UsersDao.Properties.Name.eq(username_change)).list();
                    usersDao.deleteInTx(user_cahnge1);
                    Users user_change2 = new Users();
                    user_change2.setName(username_change);
                    user_change2.setPassword(password_change);
                    user_change2.setTel(tel_change);
                    usersDao.insert(user_change2);
                    green_tv.setText("更新用户："+username_change+"成功！");
                }else {
                    green_tv.setText("更新用户："+username_change+"失败，请完善所有信息！");
                }
                break;

            case R.id.green_check:
                String strusname=green_username.getText().toString();

                if(strusname.equals("")){
                    List<Users> userList = usersDao.loadAll();
                    usersDao.detachAll();
                    green_tv.setText("");

                    for (int i = 0; i < userList.size(); i++) {//遍历比较
                        Users user = userList.get(i);//获取userList里的第i个user信息
                        green_tv.append("姓名："+user.getName()+"密码："+user.getPassword()+"电话："+user.getTel()+"\n");
                    }

                }else {
                    String strname1=green_username.getText().toString();
                    Users user1 = usersDao.queryBuilder().where(UsersDao.Properties.Name.eq(strname1)).unique();

                    green_tv.setText("姓名："+user1.getName()+"密码："+user1.getPassword()+"电话："+user1.getTel()+"\n");
                }
                
                break;

        }
    }
}