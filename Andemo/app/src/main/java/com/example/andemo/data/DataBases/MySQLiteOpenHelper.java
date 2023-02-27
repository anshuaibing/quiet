package com.example.andemo.data.DataBases;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
    private SQLiteDatabase db;
    public MySQLiteOpenHelper(Context context){
        super(context,"user.db",null,3);
        db = getReadableDatabase();
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE user(id INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR(20),password VARCHAR(20),tel INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void add(String name,String password,Integer tel){//重写添加
        db.execSQL("INSERT INTO user (name,password,tel) VALUES(?,?,?)",new Object[]{name,password,tel});
    }
    public void delete(Integer id  ,String name,String password,Integer tel){//重写删除
        db.execSQL("DELETE FROM user WHERE id= AND name = AND password = AND tel="+id+name+password+tel);
    }
    public void updata(String password){//重写更新
        db.execSQL("UPDATE user SET password = ?",new Object[]{password});
    }



    public ArrayList<User> getAllData(){//将表内信息返回成一个list

        ArrayList<User> list = new ArrayList<User>();
        @SuppressLint("Recycle") Cursor cursor = db.query("user",null,null,null,null,null,"name DESC");//1表名，2列，3行，4行，5指定列进行过滤，6进一步过滤。7得到的信息进行排序（desc逆序）
        while(cursor.moveToNext()){//一行一行遍历
            Integer id = cursor.getColumnIndex("id");
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));//移动到name列，读取出来
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            Integer tel = cursor.getColumnIndex("tel");
            list.add(new User(id,name,password,tel));//添加到user 的list中
        }
        return list;//把list返回
    }


}
