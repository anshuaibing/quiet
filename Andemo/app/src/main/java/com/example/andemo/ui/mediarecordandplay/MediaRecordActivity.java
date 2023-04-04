package com.example.andemo.ui.mediarecordandplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.andemo.R;

import java.io.File;
import java.io.IOException;

public class MediaRecordActivity extends AppCompatActivity implements View.OnClickListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    private TextureView textureView;
    private VideoView videoView;
    private MediaPlayer mediaPlayer;
    private Button btn_record;
    private EditText fileName1;
    private EditText fileName2;
    private Button btn_play;
    private Button btn_ctrl;
    private MediaRecorder mediaRecorder;
    private  Camera camera;
    private StringBuilder  strName = new StringBuilder ("");


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_record);
//        nameform();
        textureView = (TextureView) findViewById(R.id.textureView);
        videoView =  findViewById(R.id.videoPlay);
        fileName1 = findViewById(R.id.fileName1);
        fileName2 = findViewById(R.id.fileName2);
        btn_record = findViewById(R.id.btn_record);
        btn_record.setOnClickListener(this);
        btn_play = findViewById(R.id.btn_play);
        btn_play.setOnClickListener(this);
        btn_ctrl = findViewById(R.id.btn_ctrl);
        btn_ctrl.setOnClickListener(this);
    }

    void nameform(){
        final EditText inputServer = new EditText(this);
        inputServer.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("请输入视频名称：").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                .setNegativeButton("取消", null);

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String _sign = inputServer.getText().toString();
                strName=new StringBuilder(_sign);

                if (_sign != null && !_sign.isEmpty()) {
                        chageFileName(_sign);
//                    Toast.makeText(MediaRecordActivity.this, strName, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MediaRecordActivity.this, "命名为空", Toast.LENGTH_SHORT).show();
                }

            }
        });

        builder.show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_record:
                textureView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                CharSequence text = btn_record.getText();
                if (TextUtils.equals(text, "开始录制")) {
                    btn_record.setText("结束录制");
                    //配置相机方向
                    camera = Camera.open();
                    camera.setDisplayOrientation(90);
                    camera.unlock();
                    mediaRecorder = new MediaRecorder();
                    mediaRecorder.setCamera(camera);
                    mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC); //设置音频源 麦克风
                    mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);//设备视频源 摄像头
                    mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);//指定视频文件格式
                    mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);//设置音频编码格式
                    mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);//设置视频编码格式
                    mediaRecorder.setOrientationHint(90);//设置画面方向
                    //设置视频输出文件
                    mediaRecorder.setOutputFile(new File(getExternalFilesDir(""), "a.mp4").getAbsolutePath());//将视频保存到文件夹，命名为a.mp4
                    mediaRecorder.setVideoSize(640, 480);//设置视频分辨率
                    mediaRecorder.setPreviewDisplay(new Surface(textureView.getSurfaceTexture()));//将画面显示在TextureView上

                    //mediaRecorder生命周期管理
                    try {
                        mediaRecorder.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    mediaRecorder.start();

                } else {
                    btn_record.setText("开始录制");

                    mediaRecorder.stop();
                    mediaRecorder.release();
                    camera.stopPreview();
                    camera.release();
                    nameform();


                }


                break;
            case R.id.btn_play:
                textureView.setVisibility(View.VISIBLE);
                videoView.setVisibility(View.GONE);
                CharSequence textplay = btn_play.getText();

                if (TextUtils.equals(textplay, "开始播放")) {
                    //指定视频源
                    String strFileName1 = fileName1.getText().toString();
                    if (!TextUtils.isEmpty(strFileName1) ) {
                        try {
                            btn_play.setText("结束播放");
                            mediaPlayer = new MediaPlayer();
                            //设置准备监听
                            mediaPlayer.setOnPreparedListener(this);
                            // 设置播放完成监听
                            mediaPlayer.setOnCompletionListener(this);
                            mediaPlayer.setDataSource(new File(getExternalFilesDir(""), strFileName1+".mp4").getAbsolutePath());
                            //设置画布
                            mediaPlayer.setSurface(new Surface(textureView.getSurfaceTexture()));
                            mediaPlayer.prepareAsync();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                            Toast.makeText(this, "文件名为空！", Toast.LENGTH_SHORT).show();
                       }
                } else {
                    btn_play.setText("开始播放");
                    mediaPlayer.stop();
                    mediaPlayer.release(); // 结束
                }

                break;

            case R.id.btn_ctrl:
                textureView.setVisibility(View.GONE);
                videoView.setVisibility(View.VISIBLE);
                MediaController mediaController = new MediaController(this);
                mediaController.setPrevNextListeners(this,this);
                videoView.setMediaController(mediaController);

                String strFileName2 = fileName2.getText().toString();
                if (!TextUtils.isEmpty(strFileName2) ) {
                    videoView.setVideoPath(new File(getExternalFilesDir(""), strFileName2+".mp4").getAbsolutePath());
                }
                else {
                    Toast.makeText(this, "文件名为空！", Toast.LENGTH_SHORT).show();
                }
                videoView.start();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        btn_play.setText("开始播放");
        mediaPlayer.release(); // 结束
    }

    public void chageFileName(String reName){
//        File file = new File(filePath);
        //前面路径必须一样才能修改成功
        File file = new File("/sdcard/Android/data/com.example.andemo/files/a.mp4");
        String oldPath="/sdcard/Android/data/com.example.andemo/files/a.mp4";

        String path = oldPath.substring(0, oldPath.lastIndexOf("/")+1)+reName+".mp4";
        File newFile = new File(path);
        file.renameTo(newFile);
        File fileOld = new File("/sdcard/Android/data/com.example.andemo/files/a.mp4");
        fileOld.delete();
    }

}
