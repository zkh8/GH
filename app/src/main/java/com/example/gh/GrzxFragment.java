package com.example.gh;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;

public class GrzxFragment extends BaseFragment {

    private String Tag = GrzxFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;
    private YouzanBrowser mView;
    private int PAGE_TYPE_UNKNOWN = 0x0;
    private int loadstatus = 0;


    public GrzxFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(rootView == null){

            rootActivity = (AppCompatActivity) getActivity();

            rootView = inflater.inflate(R.layout.fragment_grzx, container, false);

            mainApplication = MainApplication.getInstance();

            mView = rootView.findViewById(R.id.id_webview);
            mView.needLoading(true);

            if(!YouzanSDK.isReady()){

                YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
                YouzanSDK.isDebug(true);
            }

            mView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);

                    loadstatus = 1;
                }

                @Override
                public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                    super.onPageStarted(webView, s, bitmap);
                }
            });

            load();
        }
        return rootView;
    }

    private void load(){

        if (YouzanSDK.isReady()){

            loadstatus = 1;
            mView.loadUrl(mainApplication.YZ_url_wd);
        }else{

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //myToast("开始重试");
                    loadstatus = 1;
                    mView.loadUrl(mainApplication.YZ_url_wd);
                }
            };

            mView.postDelayed(runnable, 50);

            //myToast("验证失败，再次重试");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    public boolean onBack(){

        if(mView.canGoBack()){

            mView.goBack();

            return true;
        }
        return false;
    }
}