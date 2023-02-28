package com.example.andemo.ui.recycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.andemo.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ListviewTest extends AppCompatActivity {

    RecyclerView mRecyclerView;
    MyAdapter mMyAdapter ;
    List<Person> mPersonList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_test);
        mRecyclerView = findViewById(R.id.recyclerview);
        // 得到本地json文本内容
        String fileName = "person.json";   // 创建的json文件名称
        String personJson = LocalJsonResolutionUtils.getJson(this, fileName);// 调用getJson方法，获取json文件
        Type type = new TypeToken<List<Person>>(){}.getType();// 获取列表元素的类型
        mPersonList = new Gson().fromJson(personJson,type);// 将json文件转换为列表
        mMyAdapter = new MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);//为recyclerview设置适配器
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));// 在item之间设置分割线
        LinearLayoutManager layoutManager = new LinearLayoutManager(ListviewTest.this);// 初始化管理器
        mRecyclerView.setLayoutManager(layoutManager);// 为列表添加管理器
    }

    class MyAdapter extends RecyclerView.Adapter<MyViewHoder> {//重写适配器

        @NonNull
        @Override
        public MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// 初始化适配器
//            View view = View.inflate(ListviewTest.this, R.layout.list_item, null);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            MyViewHoder myViewHoder = new MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHoder holder, int position) {// 为容器绑定组件
            Person person = mPersonList.get(position);
            holder.mTitleName.setText(person.name);
            holder.mTitleMobile.setText(person.mobile);
        }

        @Override
        public int getItemCount() {
            return mPersonList.size();
        }

    }

    class MyViewHoder extends RecyclerView.ViewHolder {// 初始化组件,这是容器里的内容
        TextView mTitleName;
        TextView mTitleMobile;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            mTitleName = itemView.findViewById(R.id.name);
            mTitleMobile = itemView.findViewById(R.id.mobile);
        }

    }
}




