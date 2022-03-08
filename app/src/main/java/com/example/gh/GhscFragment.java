package com.example.gh;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.gh.util.IntenetUtil;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;

public class GhscFragment extends BaseFragment {

    private String Tag = GhscFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;
    private YouzanBrowser mView;
    private int url_type = 0;

    private String c_url = "";


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

            mView = rootView.findViewById(R.id.id_webview);
            mView.needLoading(true);

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            YouzanSDK.isDebug(true);

            c_url = mainApplication.YZ_url_index;
            load();
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

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

                    //myToast("开始重试");
                    mView.loadUrl(c_url);
                }
            };

            mView.postDelayed(runnable, 50);

            //myToast("验证失败，再次重试");
        }
    }
}