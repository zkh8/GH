package com.example.gh;

import android.app.Application;
import android.os.Build;
import android.util.Log;
import android.webkit.WebView;

import com.anythink.core.api.ATSDK;
import com.example.gh.bean.UserInfo;
import com.mango.wakeupsdk.ManGoSDK;
import com.mango.wakeupsdk.open.error.ErrorMessage;
import com.mango.wakeupsdk.open.listener.OnInitListener;
import com.umeng.commonsdk.UMConfigure;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;

public class MainApplication extends Application {

    private static MainApplication mApp;

    //public String APP_URL = "http://192.168.31.34";
//    public String APP_URL = "https://gh.zjinghua.com";
    public String APP_URL = "https://youzan.aiyunlu.cn";
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
    public String YZ_appkey = "eea5475e33ee46a3af7f815a675edce3";
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
        ManGoSDK.getInstance().init(this, "sbnsNtRkyh", "efgQRSTZhijKopqr2345", new OnInitListener() {
            @Override
            public void onSuccess() {
                //初始化成功
            }
            @Override
            public void onFail(ErrorMessage message) {
                //初始化失败，请打印ErrorMessage对象，提供错误码
            }
        });
        UMConfigure.init(getApplicationContext(), "625e29b530a4f67780a97bf7", "Umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
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
