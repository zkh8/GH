package com.example.gh;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gh.bean.UserInfo;
import com.example.gh.util.DateUtil;
import com.mango.bidding.ManGoMobi;
import com.mango.wakeupsdk.ManGoSDK;
import com.mango.wakeupsdk.open.error.ErrorMessage;
import com.mango.wakeupsdk.open.listener.OnInterstitialAdListener;
import com.mango.wakeupsdk.provider.SdkProviderType;
import com.tencent.smtt.sdk.WebViewCallbackClient;
import com.tencent.smtt.sdk.WebViewClient;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdk.YouzanToken;
import com.youzan.androidsdk.YzLoginCallback;
import com.youzan.androidsdk.event.AbsAuthEvent;
import com.youzan.androidsdk.event.AbsShareEvent;
import com.youzan.androidsdk.event.AbsStateEvent;
import com.youzan.androidsdk.model.goods.GoodsShareModel;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;
import com.youzan.androidsdkx5.YouzanBrowser;
import com.youzan.androidsdkx5.plugin.WebClientWrapper;
import com.youzan.x5web.WebViewClientWrapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GrzxFragment extends BaseFragment implements View.OnClickListener {

    private String Tag = GrzxFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;
    private YouzanBrowser mView;
    private int PAGE_TYPE_UNKNOWN = 0x0;
    private int loadstatus = 0;

    private int lloadtime = 0;
    private int lloadDataInterval = 10;

    private ImageView btn_back;
    private int url_type = 0;
    private String c_url = "";

    private boolean loginStatus = false;

    public GrzxFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        interstitialAd();
    }

    private void interstitialAd() {
        ManGoMobi.getInstance().interstitialAd(getActivity(), "7477721173899665", new com.mango.bidding.listener.OnInterstitialAdListener() {
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
            public void onPlayFinished() {

            }

            @Override
            public void onLeftApplication() {

            }

            @Override
            public void onClose() {

            }

            @Override
            public void onError(ErrorMessage errorMessage) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {

            rootActivity = (AppCompatActivity) getActivity();

            rootView = inflater.inflate(R.layout.fragment_grzx, container, false);

            mainApplication = MainApplication.getInstance();

            btn_back = rootView.findViewById(R.id.id_btn_back);
            btn_back.setOnClickListener(this);

            mView = rootView.findViewById(R.id.id_webview);
            mView.needLoading(true);


            if (!YouzanSDK.isReady()) {

                YouzanSDK.init(rootActivity, mainApplication.YZ_clientId, mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
                YouzanSDK.isDebug(true);
            }

            mView.addJavascriptInterface(this, "injectedObject");

            mView.subscribe(new AbsAuthEvent() {
                @Override
                public void call(Context context, boolean b) {

                    Log.d(Tag, "---subscribe---" + b);
                }
            });

            mView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(com.tencent.smtt.sdk.WebView webView, String s) {

                    Log.d(Tag, "---onPageFinished---" + s + "---" + webView.getTitle().toString());

                    if (webView.getTitle().equals("签到有礼！开启你的签到之旅~")) {

                        //mView.goBack();
                        //rootActivity.findViewById(R.id.id_tab_2).callOnClick();
                    } else {

                        mView.loadUrl("javascript:function a(){" +
                                "var objs = document.getElementsByClassName(\"user-info__sign\");" +
                                "for(var i=0;i<objs.length;i++){" +
                                "objs[i].onclick=false;" +
                                "objs[i].onclick=function(ev){window.injectedObject.jscall();};" +
                                "}" +
                                "}" +
                                "setTimeout('a()', 50);"
                        );
                    }

                    super.onPageFinished(webView, s);


                    if (!s.equals(mainApplication.YZ_url_wd) && (mView.canGoBack() || url_type == 1)) {

                        btn_back.setVisibility(View.VISIBLE);
                    } else {

                        btn_back.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onPageStarted(com.tencent.smtt.sdk.WebView webView, String s, Bitmap bitmap) {
                    super.onPageStarted(webView, s, bitmap);

                    Log.d(Tag, "---onPageStarted---" + s + "---" + webView.getTitle().toString());

                    if (webView.getTitle().equals("签到有礼！开启你的签到之旅~")) {

                        mView.goBack();
                        rootActivity.findViewById(R.id.id_tab_2).callOnClick();
                    }
                }

                @Override
                public boolean shouldOverrideUrlLoading(com.tencent.smtt.sdk.WebView webView, String s) {

                    Log.d(Tag, "---shouldOverrideUrlLoading---" + s + "---" + webView.getTitle().toString());


                    if (s.indexOf("checkin") > 0) {


                        mView.goBack();
                        rootActivity.findViewById(R.id.id_tab_2).callOnClick();

                        return true;
                    }
                    return super.shouldOverrideUrlLoading(webView, s);
                }

                @Override
                public void onScaleChanged(com.tencent.smtt.sdk.WebView webView, float v, float v1) {
                    super.onScaleChanged(webView, v, v1);
                }

            });


            rootView.findViewById(R.id.id_btn_set).setOnClickListener(this);

            //load();

            //yzlogin();
        }
        return rootView;
    }

    private void load() {

        if (YouzanSDK.isReady()) {

            loadstatus = 1;
            mView.loadUrl(c_url);
        } else {

            YouzanSDK.init(rootActivity, mainApplication.YZ_clientId, mainApplication.YZ_appkey, new YouZanSDKX5Adapter());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {

                    //myToast("开始重试");
                    loadstatus = 1;
                    mView.loadUrl(c_url);
                }
            };

            mView.postDelayed(runnable, 50);

            //myToast("验证失败，再次重试");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (url_type != mainApplication.YZ_url_type) {

            url_type = mainApplication.YZ_url_type;

            if (url_type == 0) {

                c_url = mainApplication.YZ_url_wd;
            } else if (url_type == 1) {

                c_url = mainApplication.YZ_url_jfmx;
            }

            if (mView.canGoBack()) {

                mView.clearHistory();
            }
        } else if (c_url.isEmpty()) {

            c_url = mainApplication.YZ_url_wd;
        }


        if (url_type == 1) {

            btn_back.setVisibility(View.VISIBLE);
        }


        load();

        if (!loginStatus) {

            yzlogin();
        }
    }


    public boolean onBack() {

        if (url_type == 1) {

            mView.clearHistory();
            rootActivity.findViewById(R.id.id_tab_2).callOnClick();
            return true;
        }

        if (mView.canGoBack()) {

            mView.goBack();
            return true;
        }


        return false;
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.id_btn_set:

                Intent intent = new Intent();
                intent.setClass(getActivity(), SetActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_back:


                onBack();

                if (mView.canGoBack()) {

                    //mView.goBack();
                } else {

                    btn_back.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void yzlogin() {

        String openUserId = mainApplication.userInfo.yz_account_id;

        Log.d(Tag, "yzlogin phone : " + mainApplication.userInfo.phone);
        Log.d(Tag, "yzlogin yz_account_id : " + mainApplication.userInfo.yz_account_id);
        Log.d(Tag, "yzlogin yz_account_id : " + mainApplication.userInfo.yz_account_id);

        if (openUserId.isEmpty()) {

            openUserId = mainApplication.userInfo.phone;
        }

        /**
         * 用户登陆
         * @param openUserId 必传，开发者自身系统的用户ID，是三方App帐号在有赞的唯一标示符，如更换将导致原用户数据丢失16750626536
         * @param avatar 非必传，用户头像，建议传入https的url，如果不传入请使用 ""（空字符串）
         * @param extra  非必传，用户额外参数，如果不传入请使用 ""（空字符串）
         * @param nickName 非必传，用户昵称，如果不传入请使用 ""（空字符串）
         * @param gender 非必传，性别 0（保密）1（男）2（女），如果不传入请使用 ""（空字符串）
         * @param yzLoginCallback 登陆完成后回调，登陆成功 YouzanToken 会返回有赞openId
         */
        YouzanSDK.yzlogin(openUserId,
                "",
                "",
                "",
                "",
                new YzLoginCallback() {
                    @Override
                    public void onSuccess(YouzanToken data) {

                        Log.d(Tag, "---1--" + data.toString());

                        mView.post(new Runnable() {
                            @Override
                            public void run() {

                                Log.d(Tag, "---2--" + data.getAccessToken());
                                Log.d(Tag, "---2--" + data.getCookieKey());
                                Log.d(Tag, "---2--" + data.getCookieValue());
                                Log.d(Tag, "---2--" + data.getYzOpenId());


                                try {


                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                loginStatus = true;

                                mView.sync(data);
                            }
                        });
                    }

                    @Override
                    public void onFail(String message) {

                        Log.d(Tag, "-----onFail----" + message);
                    }
                });
    }


    private void loadData() {

        if (lloadtime > DateUtil.getTimes()) {

            return;
        }
        lloadtime = DateUtil.getTimes() + lloadDataInterval;

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/my";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("sid", mainApplication.UUID).build();

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", mainApplication.APP_TOKEN)
                .url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                rootActivity.runOnUiThread(() -> {

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    myToast(1000);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, url);
                Log.d(Tag, resp);

                rootActivity.runOnUiThread(() -> {

                    try {

                        LoadingTimes(-1);
                        LoadingDialog(false);

                        if (resp.equals("login")) {

                            sessLogout();
                            return;
                        }

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        int code = jsonObject.getInt("code");

                        if (code == 1000) {
                            sessLogout(code);
                            return;
                        }

                        if (status.equals("success")) {

                            JSONObject data = jsonObject.getJSONObject("data");

                        } else {

                            if (message.isEmpty()) {
                                myToast(1002);
                            } else {
                                myToast(message);
                            }
                        }
                    } catch (Exception e) {

                        e.printStackTrace();

                        Log.d(Tag, "onResponse " + e.getMessage());

                        myToast(1001);
                    }
                });
            }
        });
    }

    @JavascriptInterface
    public void jscall() {

        Log.d(Tag, "--jscall--");
    }
}