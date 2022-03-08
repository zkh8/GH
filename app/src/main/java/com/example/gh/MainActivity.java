package com.example.gh;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gh.util.IntenetUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdk.YouzanToken;
import com.youzan.androidsdk.event.AbsAuthEvent;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;

public class MainActivity extends AppCompatActivity {

    final String yz_m_Url = "https://shop44797445.youzan.com/v2/showcase/homepage?alias=VcRyCRk4Eq";

    private String Tag = MainActivity.class.getSimpleName();

    private boolean needLogin = true;
    private YouzanBrowser mView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        YouzanSDK.init(this, "302fab4a3ee5401887","aad3d4a1e9f34e54a03ed4854e1144dd", new YouZanSDKX5Adapter());
        YouzanSDK.isDebug(true);

        mView = findViewById(R.id.view);
        mView.needLoading(true);
        ProgressBar pb = new ProgressBar(this);
        mView.setLoadingView(pb);

        loadUrl(yz_m_Url);

        mView.setWebViewClient(new WebViewClient(){

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);

                Log.d(Tag, "----getPageType " + mView.getPageType() + " getTitle: " + mView.getTitle());
            }
        });
    }

    private void loadUrl(String url){

        mView.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(Tag, "----onResume");

        if(0 == IntenetUtil.getNetworkState(this)){

            findViewById(R.id.nettip).setVisibility(View.VISIBLE);
        }else{

            findViewById(R.id.nettip).setVisibility(View.GONE);
        }

        load();
    }

    private void load(){

        if (YouzanSDK.isReady()){

            loadUrl(yz_m_Url);
        }else{
            YouzanSDK.init(this, "302fab4a3ee5401887","aad3d4a1e9f34e54a03ed4854e1144dd", new YouZanSDKX5Adapter());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    Toast.makeText(getApplicationContext(),"开始重试",Toast.LENGTH_SHORT).show();
                    loadUrl(yz_m_Url);
                }
            };

            mView.postDelayed(runnable, 2000);

            Toast.makeText(this,"验证失败，再次重试",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (!mView.pageGoBack()) {
            super.onBackPressed();
        }
    }
}