package com.example.andemo.ui.notifications;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.andemo.R;
import com.example.andemo.databinding.FragmentNotificationsBinding;
import com.example.andemo.ui.recycleview.LocalJsonResolutionUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private Context context;
    RecyclerView mRecyclerView;
    NotificationsFragment.MyAdapter mMyAdapter ; ;
    List<MySet> mySets = new ArrayList<>();
    private FragmentNotificationsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        View view=inflater.inflate(R.layout.fragment_notifications,container,false);
        context=view.getContext();
        mRecyclerView=view.findViewById(R.id.setrecycle);
        // 得到本地json文本内容
        String fileName = "mysetconfig.json";   // 创建的json文件名称
        String setJson = LocalJsonResolutionUtils.getJson(context, fileName);// 调用getJson方法，获取json文件
        Type type = new TypeToken<List<MySet>>(){}.getType();// 获取列表元素的类型
        mySets = new Gson().fromJson(setJson,type);// 将json文件转换为列表
        mMyAdapter = new NotificationsFragment.MyAdapter();
        mRecyclerView.setAdapter(mMyAdapter);//为recyclerview设置适配器
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);// 初始化管理器
        layoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(layoutManager);// 为列表添加管理器
        return view;
    }

    class MyAdapter extends RecyclerView.Adapter<NotificationsFragment.MyViewHoder> {//重写适配器
        @NonNull
        @Override
        public NotificationsFragment.MyViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {// 初始化适配器
//            View view = View.inflate(ListviewTest.this, R.layout.list_item, null);//这个会出现list不适配问题
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set_item, parent, false);
            NotificationsFragment.MyViewHoder myViewHoder = new NotificationsFragment.MyViewHoder(view);
            return myViewHoder;
        }

        @Override
        public void onBindViewHolder(@NonNull NotificationsFragment.MyViewHoder holder, int position) {// 为容器绑定组件
            MySet mysets = mySets.get(position);
            int resID = getContext().getResources().getIdentifier(mysets.img, "drawable", getContext().getPackageName());
            holder.timgsrc.setImageResource(resID);
            holder.tname.setText(mysets.name);
            holder.ttag.setText(mysets.tag);
        }

        @Override
        public int getItemCount() {
            return mySets.size();
        }

    }

    class MyViewHoder extends RecyclerView.ViewHolder {// 初始化组件,这是容器里的内容
        TextView tname;
        TextView ttag;
        ImageView timgsrc;

        public MyViewHoder(@NonNull View itemView) {
            super(itemView);
            tname = itemView.findViewById(R.id.setname);
            ttag = itemView.findViewById(R.id.settag);
            timgsrc = itemView.findViewById(R.id.setimg);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}