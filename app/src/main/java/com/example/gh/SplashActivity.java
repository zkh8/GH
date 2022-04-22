package com.example.gh;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATMediationRequestInfo;
import com.anythink.core.api.AdError;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdListener;
import com.anythink.splashad.api.IATSplashEyeAd;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity implements ATSplashAdListener {
    private static final String TAG = "SplashActivity";
    private ATSplashAd splashAd;
    private FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container = findViewById(R.id.splash_ad_container);
//            ManGoSDK.getInstance().init(getApplicationContext(), mango_appid, mango_appKey, new OnInitListener() {
//                @Override
//                public void onSuccess() {
//                    Log.e(TAG,"初始化成功==============>");
//                    ManGoSDK.getInstance().splashAdTwice(SplashActivity.this, container, "10253", new OnSplashAdListener() {
//                        @Override
//                        public void onLoad(SdkProviderType type) {
//
//                        }
//
//                        @Override
//                        public void onShow(SdkProviderType type, int sdkId) {
//
//                        }
//
//                        @Override
//                        public void onClick(SdkProviderType type, int sdkId) {
//
//                        }
//
//                        @Override
//                        public void onDownloadFinished(SdkProviderType type, int sdkId) {
//
//                        }
//
//                        @Override
//                        public void onInstallFinished(SdkProviderType type, int sdkId) {
//
//                        }
//
//                        @Override
//                        public void onLeftApplication(SdkProviderType type, int sdkId) {
//
//                        }
//
//                        @Override
//                        public void onClose(SdkProviderType type) {
//                            container.removeAllViews();
//                            jumpToMainActivity();
//                        }
//
//                        @Override
//                        public void onError(SdkProviderType type, ErrorMessage message) {
//                            jumpToMainActivity();
//                            Log.e(TAG,"==============>" + message.toString());
//                        }
//                    });
//                }
//
//                @Override
//                public void onFail(ErrorMessage message) {
//                    Log.e(TAG,"初始化失败==============>");
//                }
//            });

        String placementId = "b625e3330752f3";
        ViewGroup.LayoutParams layoutParams = container.getLayoutParams();
        Configuration cf = getResources().getConfiguration();

        int ori = cf.orientation;

        /**You should set size to the layout param.**/
        if (ori == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            layoutParams.width = (int) (getResources().getDisplayMetrics().widthPixels * 0.9);
            layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        } else if (ori == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            layoutParams.width = getResources().getDisplayMetrics().widthPixels;
            layoutParams.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.85);
        }

        ATMediationRequestInfo atMediationRequestInfo = null;

        splashAd = new ATSplashAd(this, placementId, atMediationRequestInfo, this, 5000);

        Map<String, Object> localMap = new HashMap<>();
        localMap.put(ATAdConst.KEY.AD_WIDTH, layoutParams.width);
        localMap.put(ATAdConst.KEY.AD_HEIGHT, layoutParams.height);

        // Only for GDT (true: open download dialog, false: download directly)
        localMap.put(ATAdConst.KEY.AD_CLICK_CONFIRM_STATUS, true);

        splashAd.setLocalExtra(localMap);

        if (splashAd.isAdReady()) {
            Log.i(TAG, "SplashAd is ready to show.");
            splashAd.show(this, container);
        } else {
            Log.i(TAG, "SplashAd isn't ready to show, start to request.");
            splashAd.loadAd();
        }
        ATSplashAd.checkSplashDefaultConfigList(this, placementId, null);

    }


    private void toNext() {
        Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
        Log.e(TAG, "Intent pares:" + mainIntent.toString());
        startActivity(mainIntent);
    }


    @Override
    public void onAdLoaded() {
        splashAd.show(this, container);
    }

    @Override
    public void onNoAdError(AdError adError) {
        Log.i(TAG, "onNoAdError---------:" + adError.getFullErrorInfo());
        jumpToMainActivity();
    }

    @Override
    public void onAdShow(ATAdInfo entity) {
        Log.i(TAG, "onAdShow:\n" + entity.toString());
    }

    @Override
    public void onAdClick(ATAdInfo entity) {
        Log.i(TAG, "onAdClick:\n" + entity.toString());
    }

    @Override
    public void onAdDismiss(ATAdInfo atAdInfo, IATSplashEyeAd iatSplashEyeAd) {
        Log.i(TAG, "onAdDismiss:\n" + atAdInfo.toString());
        jumpToMainActivity();
    }


    boolean hasHandleJump = false;

    public void jumpToMainActivity() {
        if (!hasHandleJump) {
            hasHandleJump = true;
            toNext();
            finish();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (splashAd != null) {
            splashAd.onDestory();
        }

    }

}