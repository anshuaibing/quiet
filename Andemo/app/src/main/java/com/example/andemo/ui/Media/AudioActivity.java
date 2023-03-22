package com.example.andemo.ui.media;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.andemo.R;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/*
* 音频部分，本打算实现获取本地音频列表功能，然后实现点击可以播放。现在只能实现播放上一次录制的音频。
*
* */

public class AudioActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnRecord;
    private Button btnStop ;
    private Button btnPlay ;
    private Button btnPlayFast ;
    private Button btnPlaySlow ;
    private Button btnSpeak;
    File soundFile;                // 存放录音的文件
    boolean isRecording;
    int frequency = 11025;
    int inChannelConfig = AudioFormat.CHANNEL_IN_MONO;
    int outChannelConfig = AudioFormat.CHANNEL_OUT_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;//测试MP3，也可PCM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio);
        init();
        soundFile = new File(getExternalFilesDir(null), "andemo.pcm");

    }

    void init(){
        btnRecord=findViewById(R.id.btnRecord);
        btnStop=findViewById(R.id.btnStop);
        btnPlay=findViewById(R.id.btnPlay);
        btnPlayFast=findViewById(R.id.btnPlayFast);
        btnPlaySlow=findViewById(R.id.btnPlaySlow);
        btnSpeak=findViewById(R.id.btnSpeak);
        btnRecord.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnPlayFast.setOnClickListener(this);
        btnPlaySlow.setOnClickListener(this);

        btnSpeak.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent arg1) {
                switch (arg1.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Thread t = new Thread() {
                            public void run() {
                                try {
                                    record();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        };
                        t.start();
                        break;
                    case MotionEvent.ACTION_UP:
                        isRecording = false;
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRecord:

                Thread t = new Thread() {
                    public void run() {
                        try {
                            record();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };

                t.start();
                break;
            case R.id.btnStop:
                isRecording = false;
                break;
            case R.id.btnPlay:

                if (soundFile.exists()) {
                    try {
                        play(frequency);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btnPlayFast:

                if (soundFile.exists()) {
                    try {
                        play(frequency * 4 / 3);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;
            case R.id.btnPlaySlow:

                if (soundFile.exists()) {
                    try {
                        play(frequency * 3 / 4);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                break;

        }

    }

    void record() throws IOException {

        // 动态权限申请
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 1);
            return;
        }

        if (soundFile.exists()) soundFile.delete();
        soundFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(soundFile);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        DataOutputStream dos = new DataOutputStream(bos);

        int bufferSize = AudioRecord.getMinBufferSize(frequency, inChannelConfig, audioFormat);
        short[] buffer = new short[bufferSize];
        AudioRecord audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency, inChannelConfig, audioFormat, bufferSize);
        audioRecord.startRecording();
        isRecording = true;

        while(isRecording){
            int bufferRead = audioRecord.read(buffer, 0, bufferSize);
            for(int i=0; i<bufferRead; i++){
                dos.writeShort(buffer[i]);
            }
        }

        audioRecord.stop();
        audioRecord.release();

        dos.close();
        bos.close();
        fos.close();
    }

    void play(int frq) throws IOException{
        int length = (int)soundFile.length()/2;
        if(!soundFile.exists() || length==0) {
            Toast.makeText(getBaseContext(), "音频文件不存在或为空", Toast.LENGTH_SHORT).show();
            return;
        }
        short[] data = new short[length];
        FileInputStream fis = new FileInputStream(soundFile);
        BufferedInputStream bis = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bis);
        int i=0;
        while(dis.available()>0) {
            data[i] = dis.readShort();
            i++;
        }
        dis.close();
        bis.close();
        fis.close();

        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frq, outChannelConfig, audioFormat, length*2, AudioTrack.MODE_STREAM);
        audioTrack.play();
        audioTrack.write(data, 0, length);
        audioTrack.stop();
        //audioTrack.release();		// 不能马上release，因为write后是异步播放，此时还刚开始播放，release就不播放了
    }

}