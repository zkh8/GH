package com.example.gh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.gh.util.TextUtil;
import com.example.gh.util.ViewUtil;
import com.mango.wakeupsdk.ManGoSDK;
import com.mango.wakeupsdk.open.error.ErrorMessage;
import com.mango.wakeupsdk.open.listener.OnInterstitialAdListener;
import com.mango.wakeupsdk.provider.SdkProviderType;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TxActivity extends BaseActivity implements View.OnClickListener {

    private String Tag = TxActivity.class.getSimpleName();

    private MainApplication mainApplication;

    private PopupWindow popupWindow_ss = null;
    private WindowManager.LayoutParams params;

    private EditText et_name, et_card, et_money;
    private TextView tv_ktxje, btn_save, tv_rule;

    private int money_max = 0;
    private Double money = 0.0;
    private String card_name = "";
    private String card_id = "";

    private String[] txvarr = {"10", "33", "88"};
    private String[] txvarr1 = {"0.1", "30", "88"};
    private String[] txvarr2 = {"33", "88", "0"};

    private LinearLayout btn_tx_1, btn_tx_2, btn_tx_3;
    private TextView tv_txt_1, tv_txv_1, tv_txt_2, tv_txv_2, tv_txt_3, tv_txv_3, btn_symx;
    private int txi = 0;
    private int first = 0;

    private boolean posting = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx);

        mainApplication = MainApplication.getInstance();

        params = getWindow().getAttributes();

        iniBtn();
        interstitialAd();
    }

    private void interstitialAd() {
        ManGoSDK.getInstance().interstitialAd(this, "10362", new
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
                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void iniBtn() {

        et_name = findViewById(R.id.id_name);
        et_card = findViewById(R.id.id_card);
        et_money = findViewById(R.id.id_txje);
        et_money.addTextChangedListener(new TxActivity.MyTextWatcher(et_money));

        tv_ktxje = findViewById(R.id.id_ktxje);

        btn_symx = findViewById(R.id.id_btn_symx);
        btn_symx.setOnClickListener(this);

        findViewById(R.id.id_btn_back).setOnClickListener(this);
        findViewById(R.id.id_btn_qbtx).setOnClickListener(this);
        btn_save = findViewById(R.id.id_btn_save);
        btn_save.setOnClickListener(this);


        btn_tx_1 = findViewById(R.id.id_btn_tx_1);
        btn_tx_2 = findViewById(R.id.id_btn_tx_2);
        btn_tx_3 = findViewById(R.id.id_btn_tx_3);
        btn_tx_1.setOnClickListener(this);
        btn_tx_2.setOnClickListener(this);
        btn_tx_3.setOnClickListener(this);

        tv_txt_1 = findViewById(R.id.id_txt_1);
        tv_txt_2 = findViewById(R.id.id_txt_2);
        tv_txt_3 = findViewById(R.id.id_txt_3);

        tv_txv_1 = findViewById(R.id.id_txv_1);
        tv_txv_2 = findViewById(R.id.id_txv_2);
        tv_txv_3 = findViewById(R.id.id_txv_3);

        tv_rule = findViewById(R.id.tv_rule);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.id_btn_back:
                finish();
                break;
            case R.id.id_btn_symx:


                Intent intent = new Intent();
                intent.setClass(this, SyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

                break;
            case R.id.id_btn_qbtx:

                et_money.setText(money_max + "");
                break;
            case R.id.id_btn_save:
                save();
                break;

            case R.id.id_btn_tx_1:
                chgtxv(1);
                break;
            case R.id.id_btn_tx_2:
                chgtxv(2);
                break;
            case R.id.id_btn_tx_3:
                chgtxv(3);
                break;
        }
    }

    private void chgtxv(int i) {

        txi = i;

        if (i == 1) {

            btn_tx_1.setSelected(true);
            btn_tx_2.setSelected(false);
            btn_tx_3.setSelected(false);
            tv_txt_1.setTextColor(getResources().getColor(R.color.bg_r_5));
            tv_txv_1.setTextColor(getResources().getColor(R.color.bg_r_5));
            tv_txt_2.setTextColor(getResources().getColor(R.color.black));
            tv_txv_2.setTextColor(getResources().getColor(R.color.black));
            tv_txt_3.setTextColor(getResources().getColor(R.color.black));
            tv_txv_3.setTextColor(getResources().getColor(R.color.black));
        } else if (i == 2) {

            btn_tx_1.setSelected(false);
            btn_tx_2.setSelected(true);
            btn_tx_3.setSelected(false);
            tv_txt_1.setTextColor(getResources().getColor(R.color.black));
            tv_txv_1.setTextColor(getResources().getColor(R.color.black));
            tv_txt_2.setTextColor(getResources().getColor(R.color.bg_r_5));
            tv_txv_2.setTextColor(getResources().getColor(R.color.bg_r_5));
            tv_txt_3.setTextColor(getResources().getColor(R.color.black));
            tv_txv_3.setTextColor(getResources().getColor(R.color.black));
        } else if (i == 3) {

            btn_tx_1.setSelected(false);
            btn_tx_2.setSelected(false);
            btn_tx_3.setSelected(true);
            tv_txt_1.setTextColor(getResources().getColor(R.color.black));
            tv_txv_1.setTextColor(getResources().getColor(R.color.black));
            tv_txt_2.setTextColor(getResources().getColor(R.color.black));
            tv_txv_2.setTextColor(getResources().getColor(R.color.black));
            tv_txt_3.setTextColor(getResources().getColor(R.color.bg_r_5));
            tv_txv_3.setTextColor(getResources().getColor(R.color.bg_r_5));
        }
    }

    private void save() {

        Log.d(Tag, "save");

        String str_name = et_name.getText().toString().trim();
        String str_card = et_card.getText().toString().trim();
        String str_money = et_money.getText().toString().trim();

        if (txi > 0) {

            str_money = txvarr[txi - 1];
        } else {

            myToast("请选择提现金额");
            return;
        }

        if (str_name.isEmpty()) {

            myToast(et_name.getHint().toString());
            return;
        }

        if (str_card.isEmpty()) {

            myToast(et_card.getHint().toString());
            return;
        }

        double txje = TextUtil.convertToDouble(str_money, 0.0);

        Log.d(Tag, txi + str_money + "--" + txje);


        if (str_money.isEmpty() || txje < 0.1) {

            myToast("提现金额不能 小于 0.1");
            return;
        }

        if (txje > money) {

            myToast("提现金额不能 大于 可提现金额 " + money);
            return;
        }


        posting = true;

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/withdrawal/save";

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("money", "" + txje)
                .add("cardname", "" + str_name)
                .add("cardid", "" + str_card)
                .add("sid", mainApplication.UUID).build();

        Request request = new Request.Builder()
                .post(body)
                .header("Authorization", mainApplication.APP_TOKEN)
                .url(url).build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                runOnUiThread(() -> {

                    posting = false;

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    myToast(1000);
                    Log.d(Tag, e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(() -> {

                    posting = false;

                    try {

                        LoadingTimes(-1);
                        LoadingDialog(false);

                        if (resp.equals("login")) {

                            sessLogout();
                            return;
                        }

                        et_money.setText("");

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");
                        int code = jsonObject.getInt("code");

                        if (status.equals("success")) {

                            JSONObject data = jsonObject.getJSONObject("data");

                            money = data.getDouble("money");
                            card_name = data.getString("card_name");
                            card_id = data.getString("card_id");
                            String txmoney = data.getString("txmoney");

                            int tx_status = data.getInt("status");

                            et_name.setText(card_name);
                            et_card.setText(card_id);
                            tv_ktxje.setText(money + "");

                            if (!message.isEmpty()) {

                                myToast(message);
                            } else if (tx_status == 3) {

                                myToast("提现失败");
                            } else if (tx_status == 2) {

                                myToast("已成功提现");
                                ss_popupWindow_view(txmoney);
                            } else if (tx_status == 1) {

                                myToast("提现已取消");
                            } else {

                                myToast("提现已提交审核");
                            }
                        } else {

                            if (message.isEmpty()) {

                                myToast(1002);
                            } else {

                                myToast(message);
                            }

                            if (code == 1002) {

                                loadData();
                            }
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });
    }

    private void loadData() {

        LoadingTimes(1);
        LoadingDialog(true);

        String url = mainApplication.APP_URL + "/api/app/withdrawal";

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

                runOnUiThread(() -> {

                    LoadingTimes(-1);
                    LoadingDialog(false);
                    myToast(1000);
                    Log.d(Tag, e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {

                String resp = response.body().string();

                Log.d(Tag, resp);

                runOnUiThread(() -> {

                    LoadingTimes(-1);
                    LoadingDialog(false);

                    if (resp.equals("login")) {

                        sessLogout();
                        return;
                    }

                    try {

                        JSONObject jsonObject = new JSONObject(resp);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("message");

                        if (status.equals("success")) {

                            JSONObject data = jsonObject.getJSONObject("data");

                            money = data.getDouble("money");
                            money_max = Integer.parseInt(Math.round(Math.floor(money)) + "", 10);
                            card_name = data.getString("card_name");
                            card_id = data.getString("card_id");

                            et_name.setText(card_name);
                            et_card.setText(card_id);
                            tv_ktxje.setText(money + "");


                            first = data.getInt("first");

                            if (first == 1) {

                                txvarr = txvarr1;

                                if (data.has("tx_money_item")) {

                                    JSONArray txv = data.getJSONArray("tx_money_item");

                                    for (int i = 0; i < txv.length(); i++) {

                                        if (i > 2) {
                                            break;
                                        }
                                        txvarr[i] = txv.getString(i);
                                    }
                                }
                            } else {
                                txvarr = txvarr2;

                                if (data.has("tx_money_item")) {

                                    JSONArray txv = data.getJSONArray("tx_money_item");

                                    for (int i = 0; i < txv.length(); i++) {

                                        if (i > 2) {
                                            break;
                                        }
                                        txvarr[i] = txv.getString(i);
                                    }
                                }
                            }

                            if (!txvarr[0].equals("0")) {

                                tv_txv_1.setText(txvarr[0]);
                                btn_tx_1.setVisibility(View.VISIBLE);
                            } else {
                                btn_tx_1.setVisibility(View.GONE);
                            }

                            if (!txvarr[1].equals("0")) {

                                tv_txv_2.setText(txvarr[1]);
                                btn_tx_2.setVisibility(View.VISIBLE);
                            } else {
                                btn_tx_2.setVisibility(View.GONE);
                            }

                            if (!txvarr[2].equals("0")) {

                                tv_txv_3.setText(txvarr[2]);
                                btn_tx_3.setVisibility(View.VISIBLE);
                            } else {
                                btn_tx_3.setVisibility(View.GONE);
                            }


                            if (data.has("rules")) {
                                tv_rule.setText(data.getString("rules"));
                            }

                        } else {

                            if (message.isEmpty()) {

                                myToast(1002);
                            } else {
                                myToast(message);
                            }

                            finish();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();

                        Log.d(Tag, e.getMessage());
                        myToast(1001);
                    }
                });
            }
        });
    }

    private class MyTextWatcher implements TextWatcher {
        private EditText mView; // 声明一个编辑框对象
        private int mMinLength = 6; // 声明一个最小长度变量
        private int mMaxLength = 16; // 声明一个最大长度变量

        public MyTextWatcher(EditText v, int minLength, int maxLength) {
            super();
            mView = v;
            mMinLength = minLength;
            mMaxLength = maxLength;
        }

        public MyTextWatcher(EditText v) {
            super();
            mView = v;
        }

        // 在编辑框的输入文本变化前触发
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        // 在编辑框的输入文本变化时触发
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        // 在编辑框的输入文本变化后触发
        public void afterTextChanged(Editable s) {
            String str = s.toString();

            if (mView.getId() == R.id.id_txje) {

                if (str.length() > 0) {

                    if (Integer.parseInt(str, 10) > money_max) {

                        mView.setText(money_max + "");
                    }
                }
            }
        }

        public void chgV(View v1, View v2, boolean visible) {


            if (visible) {

                v1.setVisibility(View.VISIBLE);

                if (v2 != null) {

                    v2.setVisibility(View.VISIBLE);
                }
            } else {

                v1.setVisibility(View.GONE);

                if (v2 != null) {

                    v2.setVisibility(View.GONE);
                }
            }
        }

        public void hideOneInputMethod() {

            ViewUtil.hideOneInputMethod(TxActivity.this, mView);
        }
    }

    long waitTime = 2000;
    long touchTime = 0;

    @Override
    public void onBackPressed() {

        long currentTime = System.currentTimeMillis();

        if (posting) {

            if (currentTime - touchTime > waitTime) {

                myToast("再按一次退出");
            } else {

                finish();
            }
            touchTime = currentTime;
        } else {
            finish();
        }
    }


    private void ss_popupWindow_view(String value) {

        if (isFinishing()) {

            Log.d(Tag, "ss_popupWindow_view --activity des");
            return;
        }

        View popupView = getLayoutInflater().inflate(R.layout.tx_item, null);
        popupWindow_ss = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        popupWindow_ss.setTouchable(true);
        popupWindow_ss.setFocusable(true);
        popupWindow_ss.setOutsideTouchable(false);
        popupWindow_ss.setBackgroundDrawable(null);

        popupWindow_ss.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {

                params.alpha = 1f;
                getWindow().setAttributes(params);
            }
        });

        popupView.findViewById(R.id.id_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow_ss.dismiss();
            }
        });


        popupView.findViewById(R.id.id_btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow_ss.dismiss();
            }
        });

        ((TextView) popupView.findViewById(R.id.id_txmoney)).setText(value);

        params.alpha = 0.3f;
        getWindow().setAttributes(params);
        popupWindow_ss.showAtLocation(findViewById(R.id.id_tx_main), Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (popupWindow_ss != null && popupWindow_ss.isShowing()) {

            popupWindow_ss.dismiss();
        }
    }
}