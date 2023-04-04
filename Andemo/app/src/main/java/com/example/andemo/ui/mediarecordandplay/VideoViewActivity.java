package com.example.andemo.ui.mediarecordandplay;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.andemo.R;
import java.io.File;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener {
    private StringBuilder  strname = new StringBuilder ("");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        nameform();
        VideoView videoView =  findViewById(R.id.videoView);
        MediaController mediaController = new MediaController(this);
        mediaController.setPrevNextListeners(this,this);
        videoView.setMediaController(mediaController);
        videoView.setVideoPath(new File(getExternalFilesDir(""), strname.toString()+".mp4").getAbsolutePath());
        videoView.start();
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
                if (_sign != null && !_sign.isEmpty()) {
                    strname.append(_sign);
                    Toast.makeText(VideoViewActivity.this, strname, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VideoViewActivity.this, "命名为空", Toast.LENGTH_SHORT).show();
                }
            }

        });
        builder.show();
    }
    @Override
    public void onClick(View v) {
        Log.i("VideoView", "onClick:========== ");
    }
}
