package com.example.gh;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.anythink.core.api.ATAdInfo;
import com.anythink.core.api.ATNetworkConfirmInfo;
import com.anythink.core.api.AdError;
import com.anythink.interstitial.api.ATInterstitial;
import com.anythink.interstitial.api.ATInterstitialListener;
import com.anythink.rewardvideo.api.ATRewardVideoAd;
import com.anythink.rewardvideo.api.ATRewardVideoExListener;
import com.example.gh.bean.ArticleListBean;
import com.example.gh.util.DateUtil;
import com.example.gh.view.NativeView;
import com.mango.wakeupsdk.ManGoSDK;
import com.mango.wakeupsdk.open.error.ErrorMessage;
import com.mango.wakeupsdk.open.listener.OnInterstitialAdListener;
import com.mango.wakeupsdk.open.listener.OnRewardVideoListener;
import com.mango.wakeupsdk.provider.SdkProviderType;
import com.mob.MobSDK;
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.youzan.androidsdk.YouzanSDK;
import com.youzan.androidsdkx5.YouZanSDKX5Adapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.moments.WechatMoments;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class QdjlFragment extends BaseFragment implements View.OnClickListener {

    private String Tag = QdjlFragment.class.getSimpleName();

    private MainApplication mainApplication;
    private View rootView;

    private ArticleListBean mArticleListBean;


    private PopupWindow popupWindow_ss = null;
    private WindowManager.LayoutParams params;

    private List<Boolean> qd_list = new ArrayList<Boolean>();

    private List<LinearLayout> qd_layoutList = new ArrayList<LinearLayout>();
    private List<TextView> qd_tvlist = new ArrayList<TextView>();
    private List<TextView> qd_tvvlist = new ArrayList<TextView>();
    private List<ImageView> qd_ivlist = new ArrayList<ImageView>();
    private LinearLayout btn_tx;

    private RelativeLayout mFriendCircleLayout;
    private View mFriendCircleLine;

    private TextView tv_wdjf;
    private TextView tv_xjsy;
    private TextView tv_ggtip;
    private TextView tv_gg;
    private TextView tv_lxqdts;
    private TextView btn_ljqd;

    private TextView tv_jf1, tv_jf2;

    private int lucks = 0;
    private int c_gg = 0, c_gg_max = 0;

    private ATRewardVideoAd mRewardVideoAd;

    private String mBxghWxMiniprogram;

    private int lloadtime = 0;
    private int lloadDataInterval = 5;

    private int signtime = 0;
    private int signDataInterval = 10;

    private int lsynctime = 0;
    private int lsyncInterval = 10;

    private int lposttime = 0;
    private int lpostInterval = 10;

    private int qdgztype = 0;
    private String qdgzstr = "";

    private int qd_point = 100;

    public QdjlFragment() {
    }

    private void interstitialAd() {
        ManGoSDK.getInstance().interstitialAd(getActivity(), "10362", new
                OnInterstitialAdListener() {
                    @Override
                    public void onLoad(SdkProviderType sdkProviderType) {
                        //广告加载成功
                    }

                    @Override
                    public void onShow(SdkProviderType sdkProviderType, int i) {
                        //广告曝光
                    }

                    @Override
                    public void onClick(SdkProviderType sdkProviderType, int i) {
                        //广告点击
                    }

                    @Override
                    public void onPlayFinished(SdkProviderType sdkProviderType, int i) {
                        //视频广告播放完成
                    }

                    @Override
                    public void onDownloadFinished(SdkProviderType sdkProviderType, int i) {
                        //应用下载完成
                    }

                    @Override
                    public void onInstallFinished(SdkProviderType sdkProviderType, int i) {
                        //应用安装完成
                    }

                    @Override
                    public void onLeftApplication(SdkProviderType sdkProviderType, int i) {
                        //点击广告后跳转至第三方应用
                    }

                    @Override
                    public void onClose(SdkProviderType sdkProviderType) {
                        //广告关闭
                    }

                    @Override
                    public void onError(SdkProviderType sdkProviderType, ErrorMessage
                            errorMessage) {
                        //广告拉取失败，请打印ErrorMessage对象，提供错误码
                        Log.e("xiatao", "++++" + errorMessage.toString());
                    }
                });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mArticleListBean = new ArticleListBean();
        interstitialAd();
        ATRewardVideoAd rewardVideoAd = new ATRewardVideoAd(getContext(), "b6228432ecaf07");
        rewardVideoAd.load();
        rewardVideoAd.setAdListener(new ATRewardVideoExListener() {
            @Override
            public void onDeeplinkCallback(ATAdInfo atAdInfo, boolean b) {
                Log.i(Tag, "onDeeplinkCallback:" + atAdInfo.toString() + "--status:" + b);
            }

            @Override
            public void onDownloadConfirm(Context context, ATAdInfo atAdInfo, ATNetworkConfirmInfo atNetworkConfirmInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayStart(ATAdInfo atAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayEnd(ATAdInfo atAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayFailed(AdError adError, ATAdInfo atAdInfo) {

            }

            @Override
            public void onRewardedVideoAdAgainPlayClicked(ATAdInfo atAdInfo) {

            }

            @Override
            public void onAgainReward(ATAdInfo atAdInfo) {

            }

            @Override
            public void onRewardedVideoAdLoaded() {
                Log.i(Tag, "onRewardedVideoAdLoaded");
                mRewardVideoAd = rewardVideoAd;
            }

            @Override
            public void onRewardedVideoAdFailed(AdError adError) {
                Log.i(Tag, "onRewardedVideoAdFailed error:" + adError.getFullErrorInfo());
            }

            @Override
            public void onRewardedVideoAdPlayStart(ATAdInfo atAdInfo) {
                Log.i(Tag, "onRewardedVideoAdPlayStart:\n" + atAdInfo.toString());
            }

            @Override
            public void onRewardedVideoAdPlayEnd(ATAdInfo atAdInfo) {
                Log.i(Tag, "onRewardedVideoAdPlayEnd:\n" + atAdInfo.toString());
            }

            @Override
            public void onRewardedVideoAdPlayFailed(AdError adError, ATAdInfo atAdInfo) {
                Log.i(Tag, "onRewardedVideoAdPlayFailed error:" + adError.getFullErrorInfo());
            }

            @Override
            public void onRewardedVideoAdClosed(ATAdInfo atAdInfo) {
                Log.i(Tag, "onRewardedVideoAdClosed:\n" + atAdInfo.toString());
                //广告加载成功回调
                rewardVideoAd.load();
                savePoint(atAdInfo.toString());
            }

            @Override
            public void onRewardedVideoAdPlayClicked(ATAdInfo atAdInfo) {
                Log.i(Tag, "onRewardedVideoAdPlayClicked:\n" + atAdInfo.toString());
            }

            @Override
            public void onReward(ATAdInfo atAdInfo) {
                Log.e(Tag, "onReward:\n" + atAdInfo.toString());
            }
        });


    }

    private void savePoint(String arg) {

        if (lposttime > DateUtil.getTimes()) {

            return;
        }
        lposttime = DateUtil.getTimes() + lpostInterval;

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/advert/point";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("advert", arg)
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

                            tv_wdjf.setText("" + data.getInt("point"));
                            tv_xjsy.setText("" + data.getDouble("money"));

                            c_gg = data.getInt("gg");
                            c_gg_max = data.getInt("gg_max");

                            tv_gg.setText("(" + data.getInt("gg") + "/" + data.getInt("gg_max") + ")");

                            if (data.getInt("addlucks") > 0) {

                                myToast("恭喜您获得 " + data.getInt("addlucks") + " 次抽奖机会");

                                //rootView.findViewById(R.id.id_btn_cj).callOnClick();

                                /*
                                Intent intent;
                                intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("fltype", 0);
                                intent.putExtras(bundle);
                                intent.setClass(rootActivity, CjActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                */
                            }

                            if (data.getInt("addpoints") > 0) {

                                loadData();
                            }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {

            rootActivity = (AppCompatActivity) getActivity();

            rootView = inflater.inflate(R.layout.fragment_qdjl, container, false);

            mainApplication = MainApplication.getInstance();

            params = getActivity().getWindow().getAttributes();

            new NativeView(getContext(), (FrameLayout) rootView.findViewById(R.id.banner_layout));

            iniBtn();
        }
        return rootView;
    }


    private void iniBtn() {

        rootView.findViewById(R.id.id_btn_qdgz).setOnClickListener(this);

        for (int i = 0; i < 8; i++) {

            qd_list.add(false);
        }

        mFriendCircleLayout = rootView.findViewById(R.id.share_friend_circle_layout);
        mFriendCircleLine = rootView.findViewById(R.id.share_friend_circle_line);


        btn_tx = rootView.findViewById(R.id.id_btn_tx);
        btn_tx.setOnClickListener(this);

        tv_ggtip = rootView.findViewById(R.id.tv_ggtip);
        tv_lxqdts = rootView.findViewById(R.id.tv_lxqdts);
        btn_ljqd = rootView.findViewById(R.id.id_btn_ljqd);
        btn_ljqd.setOnClickListener(this);
        btn_ljqd.setSelected(false);

        tv_wdjf = rootView.findViewById(R.id.id_wdjf);
        tv_wdjf.setOnClickListener(this);
        tv_xjsy = rootView.findViewById(R.id.id_xjsy);
        tv_gg = rootView.findViewById(R.id.id_btn_gg);


        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_1));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_2));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_3));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_4));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_5));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_6));
        qd_layoutList.add(rootView.findViewById(R.id.id_qd_ll_7));

        qd_tvlist.add(rootView.findViewById(R.id.id_tv_1));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_2));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_3));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_4));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_5));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_6));
        qd_tvlist.add(rootView.findViewById(R.id.id_tv_7));

        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_1));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_2));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_3));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_4));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_5));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_6));
        qd_tvvlist.add(rootView.findViewById(R.id.id_tvv_7));

        qd_ivlist.add(rootView.findViewById(R.id.id_iv_1));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_2));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_3));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_4));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_5));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_6));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_7));
        qd_ivlist.add(rootView.findViewById(R.id.id_iv_7_1));

        rootView.findViewById(R.id.id_btn_gg).setOnClickListener(this);
        rootView.findViewById(R.id.id_btn_cj).setOnClickListener(this);
        rootView.findViewById(R.id.share_friend_circle).setOnClickListener(this);
        rootView.findViewById(R.id.id_btn_ljdh).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        Intent intent;
        IndexActivity indexActivity;

        switch (v.getId()) {
            case R.id.id_btn_qdgz:
                ss_popupWindow_view(0, 0, 0);
                break;
            case R.id.id_btn_gg:
                if (c_gg == c_gg_max) {

                    myToast("当天观看视频机会已用完，请明天再来");
                    return;
                }
                //savePoint("看广告");
                if (c_gg > 1) {
                    ManGoSDK.getInstance().rewardVideo(getActivity(), "10312", new OnRewardVideoListener() {
                        @Override
                        public void onLoad(SdkProviderType type) {
                            System.out.println(type.getName() + "==============>onLoad");
                        }

                        @Override
                        public void onShow(SdkProviderType type, int sdkId) {
                        }

                        @Override
                        public void onClick(SdkProviderType type, int sdkId) {
                        }

                        @Override
                        public void onPlayFinished(SdkProviderType type, int sdkId) {
                            System.out.println(type.getName() + "==============>onPlayFinished");
                        }

                        @Override
                        public void onDownloadFinished(SdkProviderType type, int sdkId) {
                        }

                        @Override
                        public void onInstallFinished(SdkProviderType type, int sdkId) {
                        }

                        @Override
                        public void onLeftApplication(SdkProviderType type, int sdkId) {
                        }

                        @Override
                        public void onClose(SdkProviderType type) {
                            savePoint(type.getName());
                        }

                        @Override
                        public void onReward(SdkProviderType type) {
                        }

                        @Override
                        public void onError(SdkProviderType type, ErrorMessage message) {
                        }
                    });
                } else {
                    if (mRewardVideoAd != null && mRewardVideoAd.isAdReady()) {
                        mRewardVideoAd.show(getActivity());
                    } else {
                        ManGoSDK.getInstance().rewardVideo(getActivity(), "10312", new OnRewardVideoListener() {
                            @Override
                            public void onLoad(SdkProviderType type) {
                                System.out.println(type.getName() + "==============>onLoad");
                            }

                            @Override
                            public void onShow(SdkProviderType type, int sdkId) {
                            }

                            @Override
                            public void onClick(SdkProviderType type, int sdkId) {
                            }

                            @Override
                            public void onPlayFinished(SdkProviderType type, int sdkId) {
                                System.out.println(type.getName() + "==============>onPlayFinished");
                            }

                            @Override
                            public void onDownloadFinished(SdkProviderType type, int sdkId) {
                            }

                            @Override
                            public void onInstallFinished(SdkProviderType type, int sdkId) {
                            }

                            @Override
                            public void onLeftApplication(SdkProviderType type, int sdkId) {
                            }

                            @Override
                            public void onClose(SdkProviderType type) {
                                savePoint(type.getName());
                            }

                            @Override
                            public void onReward(SdkProviderType type) {
                            }

                            @Override
                            public void onError(SdkProviderType type, ErrorMessage message) {
                                myToast("视频准备中，请稍后再试");
                            }
                        });
//                        myToast("视频准备中，请稍后再试");
                    }
                }


                break;
            case R.id.id_btn_tx:

                intent = new Intent();
                intent.setClass(rootActivity, TxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_ljqd:

                sign();
                break;

            case R.id.share_friend_circle:
                shareWechatCircle();
                break;
            case R.id.id_btn_cj:

                if (lucks < 1) {

                    if (c_gg == c_gg_max) {

                        myToast("当天抽奖次数已用完，请明天再来");
                    } else {

                        myToast("观看 视频广告 可获取抽奖机会");
                    }
                    return;
                }
                intent = new Intent();
                intent.setClass(rootActivity, CjtActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.id_btn_ljdh:

                indexActivity = (IndexActivity) getActivity();
                indexActivity.onClick(v);
                break;
            case R.id.id_wdjf:

                indexActivity = (IndexActivity) getActivity();
                indexActivity.onClick(v);
                break;
        }
    }

    private void toWechatApplet(String wechatId) {
        String appId = "wxcf44b583c7d026d6"; // 填移动应用(App)的 AppId，非小程序的 AppID
        IWXAPI api = WXAPIFactory.createWXAPI(getContext(), appId);

        WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
        req.userName = wechatId; // 填小程序原始id
        req.path = "";                  ////拉起小程序页面的可带参路径，不填默认拉起小程序首页，对于小游戏，可以只传入 query 部分，来实现传参效果，如：传入 "?foo=bar"。
        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
        api.sendReq(req);

//        ActivityInfoUtils.getInstance().doRecord(LoginUtils.getToken(), String.valueOf(WX_MINI));
    }

    private void shareWechatCircle() {
        if (mArticleListBean == null) {
            return;
        }
        OnekeyShare oks = new OnekeyShare();
        //指定分享的平台，如果为空，还是会调用九宫格的平台列表界面
        oks.setPlatform(WechatMoments.NAME);
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(mArticleListBean.getTitle());

        oks.setImageUrl(mArticleListBean.getImg());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(mArticleListBean.getJump_url());

        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Toast.makeText(getContext(), "分享成功了", Toast.LENGTH_SHORT);
//                ActivityInfoUtils.getInstance().doRecord(LoginUtils.getToken(), String.valueOf(SHARE_NEWS));
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Toast.makeText(getContext(), "分享失败", Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Toast.makeText(getContext(), "取消分享", Toast.LENGTH_SHORT);
            }
        });
        //启动分享
        oks.show(MobSDK.getContext());
    }

    private void sign() {

        if (signtime > DateUtil.getTimes()) {

            myToast("签到中，请稍后");
            return;
        }
        signtime = DateUtil.getTimes() + signDataInterval;

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/sign";

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

                            JSONArray qd_arr = data.getJSONArray("qd_list");

                            for (int i = 0; i < qd_arr.length(); i++) {

                                if (i > 6) {
                                    break;
                                }

                                if (qd_arr.getInt(i) == 1) {

                                    qd_layoutList.get(i).setSelected(true);
                                    qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.white));
                                    qd_tvvlist.get(i).setVisibility(View.GONE);
                                    qd_ivlist.get(i).setVisibility(View.VISIBLE);
                                } else {

                                    if (i < 6) {

                                        qd_layoutList.get(i).setSelected(false);
                                        qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.black));
                                        qd_tvvlist.get(i).setVisibility(View.VISIBLE);
                                        qd_ivlist.get(i).setVisibility(View.GONE);
                                    }
                                }
                            }

                            lucks = data.getInt("lucks");


                            tv_wdjf.setText("" + data.getInt("point"));
                            tv_xjsy.setText("" + data.getDouble("money"));
                            tv_gg.setText("(" + data.getInt("gg") + "/" + data.getInt("gg_max") + ")");

                            if (true || data.getInt("qd_today") == 1) {

                                btn_ljqd.setEnabled(false);
                                btn_ljqd.setText(rootActivity.getResources().getString(R.string.tv_jryqd));

                                ss_popupWindow_view(1, data.getInt("gd_point"), data.getInt("gg_point"));
                            } else if (data.getInt("qd_today") == 7) {

                                btn_ljqd.setEnabled(false);
                                btn_ljqd.setText(rootActivity.getResources().getString(R.string.tv_jryqd));

                                //rootView.findViewById(R.id.id_btn_cj).callOnClick();

                                Intent intent;
                                intent = new Intent();
                                Bundle bundle = new Bundle();
                                bundle.putInt("fltype", 1);
                                intent.putExtras(bundle);
                                intent.setClass(rootActivity, CjtActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                            if (data.getInt("qd_lxts") > 0) {

                                tv_lxqdts.setText("您已连续签到 " + data.getInt("qd_lxts") + " 天");
                            } else {

                                tv_lxqdts.setText("");
                            }

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


    private void loadData() {

        if (lloadtime > DateUtil.getTimes()) {

            return;
        }
        lloadtime = DateUtil.getTimes() + lloadDataInterval;

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/chksign";

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

                            JSONArray qd_arr = data.getJSONArray("qd_list");

                            for (int i = 0; i < qd_arr.length(); i++) {

                                if (i == 6) {

                                    if (qd_arr.getInt(i) == 1) {

                                        qd_layoutList.get(i).setSelected(true);
                                        qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.white));
                                        //qd_tvvlist.get(i).setVisibility(View.GONE);
                                        qd_ivlist.get(i).setVisibility(View.GONE);
                                        qd_ivlist.get(i + 1).setVisibility(View.VISIBLE);
                                    } else {

                                        qd_layoutList.get(i).setSelected(false);
                                        qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.black));
                                        //qd_tvvlist.get(i).setVisibility(View.VISIBLE);
                                        qd_ivlist.get(i).setVisibility(View.VISIBLE);
                                        qd_ivlist.get(i + 1).setVisibility(View.GONE);
                                    }

                                    break;
                                }

                                if (qd_arr.getInt(i) == 1) {

                                    qd_layoutList.get(i).setSelected(true);
                                    qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.white));
                                    qd_tvvlist.get(i).setVisibility(View.GONE);
                                    qd_ivlist.get(i).setVisibility(View.VISIBLE);
                                } else {

                                    qd_layoutList.get(i).setSelected(false);
                                    qd_tvlist.get(i).setTextColor(rootActivity.getResources().getColor(R.color.black));
                                    qd_tvvlist.get(i).setVisibility(View.VISIBLE);
                                    qd_ivlist.get(i).setVisibility(View.GONE);
                                }
                            }

                            lucks = data.getInt("lucks");

                            tv_wdjf.setText("" + data.getInt("point"));
                            tv_xjsy.setText("" + data.getDouble("money"));

                            c_gg = data.getInt("gg");
                            c_gg_max = data.getInt("gg_max");

                            tv_gg.setText("(" + data.getInt("gg") + "/" + data.getInt("gg_max") + ")");

                            if (data.getInt("qd_today") == 1) {

                                btn_ljqd.setEnabled(false);
                                btn_ljqd.setText(rootActivity.getResources().getString(R.string.tv_jryqd));
                                Log.d(Tag, "---------v 1-----");
                            } else {
                                Log.d(Tag, "---------v 0-----");
                                btn_ljqd.setEnabled(true);
                                btn_ljqd.setText(rootActivity.getResources().getString(R.string.tv_ljqd));
                            }


                            if (data.getInt("qd_lxts") > 0) {

                                tv_lxqdts.setText("您已连续签到 " + data.getInt("qd_lxts") + " 天");
                            } else {

                                tv_lxqdts.setText("");
                            }


                            tv_ggtip.setText("奖励" + data.getInt("gg_zjf") + "积分，以及免费抽奖次数");


                            if (data.has("rules")) {

                                qdgzstr = data.getString("rules");
                            }


                            if (data.has("sync")) {
                                //积分未同步
                                if (data.getInt("sync") == 0) {

                                    loadSync();
                                }
                            }

                            if (data.has("qd_point")) {

                                if (qd_point != data.getInt("qd_point")) {

                                    qd_point = data.getInt("qd_point");

                                    for (int i = 0; i < 7; i++) {

                                        qd_tvvlist.get(i).setText("" + qd_point);
                                    }
                                }
                            }
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

    private void getBxghArticleList() {


        String url = "http://59.110.13.31/api/activity/bxghArticleList";

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                rootActivity.runOnUiThread(() -> {
                    myToast(1000);
                    mFriendCircleLayout.setVisibility(View.GONE);
                    mFriendCircleLine.setVisibility(View.GONE);
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, url);
                Log.d(Tag, resp);

                rootActivity.runOnUiThread(() -> {

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        JSONArray data = jsonObject.getJSONArray("data");
                        int code = jsonObject.getInt("code");

                        if (code != 0) {
                            return;
                        }

                        if (data != null && !data.isNull(0)) {
                            mFriendCircleLayout.setVisibility(View.VISIBLE);
                            mFriendCircleLine.setVisibility(View.VISIBLE);

                            JSONObject o = data.getJSONObject(0);
                            mArticleListBean.setTitle(o.getString("title"));
                            mArticleListBean.setImg(o.getString("img"));
                            mArticleListBean.setJump_url(o.getString("jump_url"));

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

    private void getWxMiniProgram() {


        String url = "http://59.110.13.31/api/activity/wxMiniProgram";

        OkHttpClient client = new OkHttpClient();


        Request request = new Request.Builder()
                .url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                rootActivity.runOnUiThread(() -> {
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

                        JSONObject jsonObject = new JSONObject(resp);
                        JSONObject data = jsonObject.getJSONObject("data");
                        int code = jsonObject.getInt("code");

                        if (code != 0) {
                            return;
                        }

                        if (data != null && !data.isNull("bxgh_wx_miniprogram")) {
                            mBxghWxMiniprogram = data.getString("bxgh_wx_miniprogram");
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


    private void loadSync() {

        if (lsynctime > DateUtil.getTimes()) {

            return;
        }
        lsynctime = DateUtil.getTimes() + lsyncInterval;

        //LoadingTimes(1);
        //LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/point/sync";

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

                    //LoadingTimes(-1);
                    //LoadingDialog(false);
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

                        //LoadingTimes(-1);
                        //LoadingDialog(false);

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

    @Override
    public void onResume() {
        super.onResume();
        loadData();
        getWxMiniProgram();
        getBxghArticleList();

    }

    private void ss_popupWindow_view(int ptype, int jf1, int jf2) {

        int lid = R.layout.qdgz_item;

        int vlpW = ViewGroup.LayoutParams.MATCH_PARENT;
        int vlpH = ViewGroup.LayoutParams.WRAP_CONTENT;
        if (ptype == 1) {

            vlpH = ViewGroup.LayoutParams.WRAP_CONTENT;
            lid = R.layout.qd_item;
        }

        View popupView = getLayoutInflater().inflate(lid, null);
        popupWindow_ss = new PopupWindow(popupView, vlpW, vlpH);

        popupWindow_ss.setTouchable(true);
        popupWindow_ss.setFocusable(true);
        popupWindow_ss.setOutsideTouchable(false);
        popupWindow_ss.setBackgroundDrawable(null);

        popupWindow_ss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params.alpha = 1f;
                getActivity().getWindow().setAttributes(params);
            }
        });


        if (ptype == 1) {


            popupView.findViewById(R.id.id_btn_ok).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow_ss.dismiss();
                }
            });

            popupView.findViewById(R.id.id_btn_ksp).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (mRewardVideoAd != null && mRewardVideoAd.isAdReady()) {
                        mRewardVideoAd.show(getActivity());
                    } else {
                        myToast("视频准备中，请稍后再试");
                    }
                    popupWindow_ss.dismiss();
                }
            });

            tv_jf1 = popupView.findViewById(R.id.id_jf1);
            tv_jf2 = popupView.findViewById(R.id.id_jf2);
            new NativeView(getContext(), (FrameLayout) popupView.findViewById(R.id.sign_banner_layout));

            tv_jf1.setText("+" + jf1);
            tv_jf2.setText("" + jf2);
        } else {


            WebView webView = popupView.findViewById(R.id.id_webview);
            TextView tv_qdgz = popupView.findViewById(R.id.tv_qdgz);

            popupView.findViewById(R.id.id_btn_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    popupWindow_ss.dismiss();
                }
            });


            if (qdgzstr.isEmpty()) {

                webView.setVisibility(View.VISIBLE);
                tv_qdgz.setVisibility(View.GONE);

                //支持javascript
                webView.getSettings().setJavaScriptEnabled(true);
                // 设置可以支持缩放
                webView.getSettings().setSupportZoom(true);
                // 设置出现缩放工具
                webView.getSettings().setBuiltInZoomControls(true);
                //扩大比例的缩放
                webView.getSettings().setUseWideViewPort(true);
                //自适应屏幕
                webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
                webView.getSettings().setLoadWithOverviewMode(true);


                //如果不设置WebViewClient，请求会跳转系统浏览器
                webView.setWebViewClient(new WebViewClient() {

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {

                        if (url.toString().contains("baidu.cn")) {

                            view.loadUrl("https://item.jd.com/100027401298.html");
                            return true;
                        }

                        return false;
                    }

                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            if (request.getUrl().toString().contains("baidu.com")) {
                                view.loadUrl("https://item.jd.com/100027401298.html");
                                return true;
                            }
                        }

                        return false;
                    }

                });

                webView.loadUrl(mainApplication.APP_URL + "/api/signa/greement");
                //webView.loadUrl("https://item.jd.com/100027401298.html");
                webView.getSettings().setDisplayZoomControls(false);

            } else {

                webView.setVisibility(View.GONE);
                tv_qdgz.setVisibility(View.VISIBLE);
                tv_qdgz.setText(qdgzstr);
            }
        }


        params.alpha = 0.3f;
        getActivity().getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(rootView.findViewById(R.id.id_qd_main), Gravity.CENTER, 0, 0);
    }
}