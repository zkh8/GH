package com.example.gh;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;

public class GrzxFragment extends BaseFragment {

    private String Tag = GrzxFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;
    private YouzanBrowser mView;

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

            load();
        }
        return rootView;
    }

    private void load(){

        if (YouzanSDK.isReady()){

            mView.loadUrl(mainApplication.YZ_url_wd);
        }else{

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId,mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //myToast("开始重试");
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
}