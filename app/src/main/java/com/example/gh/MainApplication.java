package com.example.gh;

import android.app.Application;
import android.util.Log;

import com.example.gh.bean.UserInfo;

import java.util.UUID;

public class MainApplication extends Application {


    private static MainApplication mApp;

    //public String APP_URL = "http://192.168.31.34";
    public String APP_URL = "https://gh.zjinghua.com";
    public String APP_TOKEN;

    public UserInfo userInfo;
    public String UUID;

    public int login_times = 3600 * 24 * 7;

    public String YZ_url_index = "https://shop44797445.youzan.com/v2/showcase/homepage?alias=VcRyCRk4Eq";
    public String YZ_url_wd = "https://shop44797445.youzan.com/wscuser/membercenter?kdt_id=44605277";
    public String YZ_url_jfsc = "https://shop44797445.m.youzan.com/wscshop/showcase/feature?alias=MuqZyIRB3H";
    public String YZ_clientId = "302fab4a3ee5401887";
    public String YZ_appkey = "2481426c4c1742369552fc7e0f4b0a63";
    public int YZ_url_type = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        Log.d("MainApplication", "onCreate");
    }

    public static MainApplication getInstance(){

        Log.d("MainApplication", "getInstance");

        return mApp;
    }

}
