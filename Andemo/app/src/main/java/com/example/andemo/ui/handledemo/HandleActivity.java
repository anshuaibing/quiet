package com.example.andemo.ui.handledemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.andemo.R;

import java.util.Timer;
import java.util.TimerTask;

public class HandleActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handle);
        final ImageView imgchange = (ImageView) findViewById(R.id.imgchange);

        //使用定时器,每隔200毫秒让handler发送一个空信息
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                myHandler.sendEmptyMessage(0x123);

            }
        }, 0,200);
    }

    //定义切换的图片的数组id
    int imgids[] = new int[]{
            R.drawable.p1, R.drawable.p2,R.drawable.p3,
            R.drawable.p4,R.drawable.p5,R.drawable.p6,
            R.drawable.p7,R.drawable.p4
    };
    int imgstart = 0;

    final Handler myHandler = new Handler()
    {
        @Override
        //重写handleMessage方法,根据msg中what的值判断是否执行后续操作
        public void handleMessage(Message msg) {
            if(msg.what == 0x123)
            {
                final ImageView imgchange = (ImageView) findViewById(R.id.imgchange);
                imgchange.setImageResource(imgids[imgstart++ % 8]);
            }
        }
    };
}