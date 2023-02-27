package com.example.andemo.ui.webview;


import android.os.Bundle;

import com.example.andemo.R;

import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

public class PicActivity  extends AppCompatActivity implements View.OnClickListener {
    private Button showBtn;
    private ImageView showImg;
    private ArrayList<String> urls;
    private int curPos = 0;
    private PictureLoader loader;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic);
        loader = new PictureLoader();
        initData();
        initUI();
    }

    private void initData() {
        urls = new ArrayList<>();
        urls.add("https://alifei04.cfp.cn/creative/vcg/800/new/VCG211245953577.jpg");
        urls.add("https://tenfei04.cfp.cn/creative/vcg/800/new/VCG41N1448724908.jpg");
        urls.add("https://tenfei01.cfp.cn/creative/vcg/800/version23/VCG41643897797.jpg");
        urls.add("https://alifei04.cfp.cn/creative/vcg/800/new/VCG211245953577.jpg");
        urls.add("https://tenfei04.cfp.cn/creative/vcg/800/new/VCG41N1448724908.jpg");
        urls.add("https://tenfei01.cfp.cn/creative/vcg/800/version23/VCG41643897797.jpg");
        urls.add("https://alifei04.cfp.cn/creative/vcg/800/new/VCG211245953577.jpg");
        urls.add("https://tenfei04.cfp.cn/creative/vcg/800/new/VCG41N1448724908.jpg");
        urls.add("https://tenfei01.cfp.cn/creative/vcg/800/version23/VCG41643897797.jpg");
        urls.add("https://tenfei04.cfp.cn/creative/vcg/800/new/VCG41N1448724908.jpg");
    }

    private void initUI() {
        showBtn = (Button) findViewById(R.id.btn_show);
        showImg = (ImageView) findViewById(R.id.img_show);
        showBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_show:
                if (curPos > 9) {
                    curPos = 0;
                }
                loader.load(showImg, urls.get(curPos));
                curPos++;
                break;
        }
    }
}