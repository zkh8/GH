package com.example.gh;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gh.util.IntenetUtil;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdk.event.AbsShareEvent;
import com.youzan.androidsdk.model.goods.GoodsShareModel;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;

public class GhscFragment extends BaseFragment {

    private String Tag = GhscFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;
    private YouzanBrowser mView;
    private int url_type = 0;

    private String c_url = "";

    private int PAGE_TYPE_UNKNOWN = 0x0;
    private int loadstatus = 0;


    public GhscFragment() {
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

            rootView = inflater.inflate(R.layout.fragment_ghsc, container, false);

            mainApplication = MainApplication.getInstance();

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            YouzanSDK.isDebug(true);


            mView = rootView.findViewById(R.id.id_webview);
            mView.needLoading(true);


            mView.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                    super.onPageStarted(webView, s, bitmap);

                    Log.d(Tag, "onPageStarted-----");
                }
            });


            mView.subscribe(new AbsShareEvent() {
                @Override
                public void call(Context context, GoodsShareModel goodsShareModel) {

                    Log.d(Tag, "AbsShareEvent-----");
                }
            });


            c_url = mainApplication.YZ_url_index;
            load();
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        Log.d(Tag, "onResume");

        if(url_type != mainApplication.YZ_url_type){

            url_type = mainApplication.YZ_url_type;

            if(url_type == 0){

                c_url = mainApplication.YZ_url_index;
            }else if(url_type == 1){

                c_url = mainApplication.YZ_url_jfsc;
            }
            load();
        }
    }

    private void load(){

        if (YouzanSDK.isReady()){

            mView.loadUrl(c_url);
        }else{

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    Log.d(Tag, "try");
                    //myToast("开始重试");
                    mView.loadUrl(c_url);
                }
            };

            mView.postDelayed(runnable, 50);

            Log.d(Tag, "err");
            //myToast("验证失败，再次重试");
        }
    }

    public boolean onBack(){


        if(mView.canGoBack()){

            mView.goBack();

            if(!mView.canGoBack()){

                if(url_type != 0){

                    mainApplication.YZ_url_type = 0;
                    url_type = mainApplication.YZ_url_type;
                }
            }

            return true;
        }
        return false;
    }

}