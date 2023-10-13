package com.example.gh;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.anythink.core.api.ATAdConst;
import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATMediationRequestInfo;
import com.anythink.core.api.AdError;
import com.anythink.splashad.api.ATSplashAd;
import com.anythink.splashad.api.ATSplashAdExtraInfo;
import com.anythink.splashad.api.ATSplashAdListener;
import com.mango.bidding.ManGoMobi;
import com.mango.wakeupsdk.ManGoSDK;
import com.mango.wakeupsdk.open.error.ErrorMessage;
import com.mango.wakeupsdk.open.listener.OnInitListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SplashActivity extends Activity implements ATSplashAdListener {
    private static final String TAG = "SplashActivity";
    private ATSplashAd splashAd;
    private FrameLayout container;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container = findViewById(R.id.splash_ad_container);
        Random rd = new Random();
//        int temp = rd.nextInt(2);
        int temp = 1;

        if (temp == 1) {
            ManGoMobi.getInstance().splashAd(SplashActivity.this, container, "6964474586296109", new com.mango.bidding.listener.OnSplashAdListener() {
                @Override
                public void onLoad() {

                }

                @Override
                public void onShow() {

                }

                @Override
                public void onClick() {

                }

                @Override
                public void onClose() {
                    container.removeAllViews();
                    jumpWhenCanClick();
                }

                @Override
                public void onError(ErrorMessage errorMessage) {
                    jumpToMainActivity();
                    Log.e("xiatao","+++"+errorMessage.message);
                    Log.e(TAG, "==============>" + errorMessage.toString());
                }
            });
        }


    }


    public boolean canJumpImmediately = false;

    private void jumpWhenCanClick() {
        Log.d("BeiZisDemo", "jumpWhenCanClick canJumpImmediately== " + canJumpImmediately);
        if (canJumpImmediately) {
            startActivity(new Intent(SplashActivity.this, StartActivity.class));
            finish();
        } else {
            canJumpImmediately = true;
        }
    }

    private void toNext() {
        Intent mainIntent = new Intent(SplashActivity.this, StartActivity.class);
        Log.e(TAG, "Intent pares:" + mainIntent.toString());
        startActivity(mainIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJumpImmediately) {
            jumpToMainActivity();
        }
        canJumpImmediately = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJumpImmediately = false;
    }

    @Override
    public void onAdLoaded(boolean b) {
        splashAd.show(this, container);
        jumpWhenCanClick();
    }

    @Override
    public void onAdLoadTimeout() {

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
    public void onAdDismiss(ATAdInfo atAdInfo, ATSplashAdExtraInfo atSplashAdExtraInfo) {
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