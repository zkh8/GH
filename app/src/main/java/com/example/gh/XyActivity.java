package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class XyActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xy);


        ImageView btn_back = findViewById(R.id.id_btn_back);
        btn_back.setOnClickListener(this);

        initView("file:///android_asset/gh.html");


    }


    private void initView(String url){

        WebView webView = findViewById(R.id.id_content);

        webView.getSettings().setDefaultTextEncodingName("utf-8");//文本编码

        //支持javascript
        webView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        //webView.getSettings().setBuiltInZoomControls(false);
        //扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
        //自适应屏幕
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.getSettings().setTextZoom(250);

        //如果不设置WebViewClient，请求会跳转系统浏览器
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url.toString().contains("baidu.cn")){

                    return true;
                }

                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request)
            {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (request.getUrl().toString().contains("baidu.com")){

                        return true;
                    }
                }

                return false;
            }

        });
        webView.loadUrl(url);
        webView.getSettings().setDisplayZoomControls(false);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.id_btn_back:
                finish();
                break;
        }
    }
}