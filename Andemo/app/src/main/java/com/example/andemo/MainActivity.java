package com.example.andemo;

import android.content.IntentFilter;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.andemo.databinding.ActivityMainBinding;
import com.example.andemo.greendao.DaoManager;
import com.example.andemo.ui.broadcastreceiver.MyBRReceiver;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends AppCompatActivity {
    MyBRReceiver myReceiver;
    private ActivityMainBinding binding;
//    MySQLiteOpenHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);


        DaoManager.getInstance().initGreenDao(this);
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        myReceiver = new MyBRReceiver();
        IntentFilter itFilter = new IntentFilter();
        itFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(myReceiver, itFilter);
//        helper=new MySQLiteOpenHelper(  MainActivity.this);
//        SQLiteDatabase db=helper.getWritableDatabase();
    }


    //别忘了将广播取消掉哦~
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myReceiver);
    }


}