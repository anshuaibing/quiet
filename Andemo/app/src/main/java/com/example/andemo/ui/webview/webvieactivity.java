package com.example.andemo.ui.webview;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andemo.R;
import com.example.andemo.ui.home.HomeFragment;

public class webvieactivity extends AppCompatActivity
{
    private FragmentManager fragmentManager = getFragmentManager();
    private EditText edit;
    private String urlstr;
    private TextView textView;
    private EditText editText;
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webvieactivity);
        editText=(EditText) findViewById(R.id.editurl);
        button=(Button) findViewById(R.id.btnurl);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                urlstr=editText.getText().toString();
                WebView mw = (WebView) findViewById(R.id.myweb);
                if (urlstr.equals(""))
                {
                    urlstr="https://www.baidu.com";
                }

                // 调用成员函数访问网页，加载资源
//                mw.loadUrl("https://www.baidu.com");

                mw.loadUrl(urlstr);
                // //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
                mw.setWebViewClient(new WebViewClient(){
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        // 重写WebViewClient的shouldOverrideUrlLoading()方法
                        //使用WebView加载显示url
                        view.loadUrl(url);
                        //返回true
                        return true;


                    }
                });
            }
        });

    }


}

