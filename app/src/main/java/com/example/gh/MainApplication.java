package com.example.gh;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.anythink.core.api.ATSDK;

import com.example.gh.bean.UserInfo;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;

import java.util.UUID;

public class MainApplication extends Application {

    private static MainApplication mApp;

    //public String APP_URL = "http://192.168.31.34";
    public String APP_URL = "https://gh.zjinghua.com";
    public String APP_TOKEN;

    public UserInfo userInfo;
    public String UUID;

    public int login_times = 3600 * 24 * 1800;
    public int loadDataInterval = 30;

    public String YZ_url_index = "https://shop44797445.youzan.com/v2/showcase/homepage?alias=VcRyCRk4Eq";
    public String YZ_url_wd = "https://shop44797445.youzan.com/wscuser/membercenter?kdt_id=44605277";
    public String YZ_url_jfsc = "https://shop44797445.m.youzan.com/wscshop/showcase/feature?alias=MuqZyIRB3H";
    public String YZ_url_jfmx = "https://shop44797445.youzan.com/wscump/pointstore/pointdetails?kdt_id=44605277";


    public String YZ_clientId = "fcec8ddb56f16a25e4";
    public String YZ_client_secret = "189e26122e1709421110f82edfdf2fda";
    public String YZ_appkey = "161a6bd242274e6595dc0d844a31ffe7";
    public int YZ_url_type = 0;

    public static final String appid = "a62284309a1462";
    public static final String appKey = "a1ec046c3a21998d901f034bb3d80f88";

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName();
            if (!getPackageName().equals(processName)) {
                WebView.setDataDirectorySuffix(processName);
            }
        }
        ATSDK.setNetworkLogDebug(true);//SDK日志功能，集成测试阶段建议开启，上线前必须关闭
        ATSDK.init(this, appid, appKey);

        ATSDK.integrationChecking(this);

        YouzanSDK.init(this, YZ_clientId, YZ_appkey, new YouZanSDKX5Adapter());
        YouzanSDK.isDebug(true);

        Log.d("MainApplication", "onCreate");
    }

    public static MainApplication getInstance(){

        Log.d("MainApplication", "getInstance");

        return mApp;
    }

}
