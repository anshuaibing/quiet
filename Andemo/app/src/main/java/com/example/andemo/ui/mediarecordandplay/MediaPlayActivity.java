package com.example.andemo.ui.mediarecordandplay;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.andemo.R;

public class MediaPlayActivity extends AppCompatActivity {
private Button button1;
private Button button2;
private Button button3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_play);
        //动态申请权限
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO},100);
        button1=(Button) findViewById(R.id.button1);
        button3=(Button) findViewById(R.id.button3);

        button1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(getApplicationContext(),MediaRecordActivity.class);
                startActivity(intent1);
            }
        });

        button3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3=new Intent(getApplicationContext(),AudioActivity.class);
                startActivity(intent3);
            }
        });

    }
}